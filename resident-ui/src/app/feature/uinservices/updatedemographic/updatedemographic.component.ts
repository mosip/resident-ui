import { Component, OnInit, OnDestroy } from "@angular/core";
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from "@ngx-translate/core";
import { Subscription } from "rxjs";
import { Router } from "@angular/router";
import { AppConfigService } from 'src/app/app-config.service';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { MatDialog } from '@angular/material';
import Utils from "src/app/app.utils";
import { InteractionService } from "src/app/core/services/interaction.service";
import { AuditService } from "src/app/core/services/audit.service";
import defaultJson from "src/assets/i18n/default.json";
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AutoLogoutService } from "src/app/core/services/auto-logout.service";
import { DateAdapter } from '@angular/material/core';

@Component({
  selector: "app-demographic",
  templateUrl: "updatedemographic.component.html",
  styleUrls: ["updatedemographic.component.css"],
})
export class UpdatedemographicComponent implements OnInit, OnDestroy {
  userInfo: any;
  static actualData: any;
  schema: any;
  subscriptions: Subscription[] = [];
  buildJSONData: any = {};
  langCode: string = localStorage.getItem("langCode");
  dropDownValues: any = {};
  supportedLanguages: Array<string>;
  locationFieldNameList: string[] = [];
  locCode = 0;
  initialLocationCode: any = "";
  dynamicFieldValue = {};
  dynamicDropDown = {};
  files: any[] = [];
  filesPOA: any[] = [];
  proofOfIdentity: any = {};
  proofOfAddress: any = {};
  transactionID: any;
  userId: any;
  userIdEmail: string = "";
  userIdPhone: string = "";
  clickEventSubscription: Subscription;
  popupMessages: any;
  pdfSrc = "";
  pdfSrcPOA = "";
  confirmEmailContact: string = "";
  confirmPhoneContact: string = "";
  sendOtpDisable: boolean = true;
  showPreviewPage: boolean = false;
  userInfoClone: any = {};
  userInfoAddressClone: any = {};
  userPrefLang: any = {};
  buildCloneJsonData: any = {};
  uploadedFiles: any[] = [];
  previewDisabled: boolean = true;
  pdfSrcInPreviewPage = "";
  previewDisabledInAddress: boolean = true;
  selectedDate: any;
  message: any;
  errorCode: any;
  selectedLanguage: any;
  defaultJsonValue: any;
  newLangArr: any = [];
  perfLangArr: any = {};
  newNotificationLanguages: any = [];
  matTabLabel: string = "Identity";
  matTabIndex: number = 0;
  contactTye: string = "";
  width: string;
  cols: number;
  message2: any;
  displayPOIUpload: boolean = false;
  displayPOAUpload: boolean = false;
  maxdate: Date = new Date();
  isValidFileFormatPOI: boolean = false;
  isValidFileFormatPOA: boolean = false;
  warningMessage: string;
  langJson: any;
  selectedPOIFileForPreview: string = "";
  selectedPOAFileForPreview: string = "";
  selectedFileInPreviewPage: string = "";
  isLoading: boolean = true;
  showNotMatchedMessageEmail: boolean = false;
  showNotMatchedMessagePhone: boolean = false;
  email: any;
  phone: any;
  userInputValues: any = {};
  finalUserCloneData: any;
  updatingtype: string;

  constructor(private autoLogout: AutoLogoutService, private interactionService: InteractionService, private dialog: MatDialog, private dataStorageService: DataStorageService, private translateService: TranslateService, private router: Router, private appConfigService: AppConfigService, private auditService: AuditService, private breakpointObserver: BreakpointObserver, private dateAdapter: DateAdapter<Date>) {
    this.clickEventSubscription = this.interactionService.getClickEvent().subscribe((id) => {
      if (id === "updateMyData") {
        this.updateDemographicData();
      } else if (id === "resend") {
        this.reGenerateOtp();
      } else if (id !== 'string' && id.type === 'otp') {
        this.verifyupdatedData(id.otp);
      }
    })
    this.breakpointObserver.observe([
      Breakpoints.XSmall,
      Breakpoints.Small,
      Breakpoints.Medium,
      Breakpoints.Large,
      Breakpoints.XLarge,
    ]).subscribe(result => {
      if (result.matches) {
        if (result.breakpoints[Breakpoints.XSmall]) {
          this.cols = 1;
          this.width = "95%";
        }
        if (result.breakpoints[Breakpoints.Large]) {
          this.cols = 4;
          this.width = "50%";
        }
        if (result.breakpoints[Breakpoints.XLarge]) {
          this.cols = 4;
          this.width = "40%";
        }
        if (result.breakpoints[Breakpoints.Medium]) {
          this.cols = 2;
          this.width = "75%";
        }
      }
    });
    this.dateAdapter.setLocale('en-GB');
  }

  async ngOnInit() {
    this.defaultJsonValue = { ...defaultJson }
    this.initialLocationCode = "MOR";
    this.locCode = 5;
    this.translateService.use(localStorage.getItem("langCode"));
    this.supportedLanguages = ["eng"];
    this.translateService
      .getTranslation(localStorage.getItem("langCode"))
      .subscribe(response => {
        this.langJson = response.updatedemographic
        this.popupMessages = response;
      });

    let supportedLanguages = this.appConfigService.getConfig()['supportedLanguages'].split(',');
    supportedLanguages.forEach(data => {
      let newObj = { "code": data, "name": this.defaultJsonValue['languages'][data]['nativeName'] }
      this.newNotificationLanguages.push(newObj)
    })

    this.getUpdateMyDataSchema();
    this.getUserInfo();

    const subs = this.autoLogout.currentMessageAutoLogout.subscribe(
      (message) => (this.message2 = message) //message =  {"timerFired":false}
    );

    this.subscriptions.push(subs);

    if (!this.message2["timerFired"]) {
      this.autoLogout.getValues(this.langCode);
      this.autoLogout.setValues();
      this.autoLogout.keepWatching();
    } else {
      this.autoLogout.getValues(this.langCode);
      this.autoLogout.continueWatching();
    }

  }


  async getUpdateMyDataSchema() {
      this.isLoading = true;
    await new Promise((resolve) =>{  
      this.dataStorageService
        .getUpdateMyDataSchema('update-demographics')
        .subscribe((response) => {
          this.schema = response;
        });
    })
  }


  getUserInfo() {
      this.dataStorageService
        .getUserInfo('update-demographics')
        .subscribe((response) => {
          if (response["response"]) {
            this.userInfo = response["response"];
            UpdatedemographicComponent.actualData = response["response"];
            if(this.schema && this.userInfo){
              this.buildData()
              this.isLoading = false;
            }else{
              console.log("Testing")
              this.getUpdateMyDataSchema();
              this.getUserInfo();
            }
          } else {
          this.showErrorPopup(response['errors'])
        }
      });
  }

  buildData() {
    try {
      let self = this;
      for (var schema of self.schema['identity']) {
        if (self.userInfo[schema.attributeName]) {
          if ((schema.attributeName !== "proofOfIdentity") && (schema.attributeName !== "proofOfAddress")) {
            if (typeof self.userInfo[schema.attributeName] === "string") {
              self.buildJSONData[schema.attributeName] = self.userInfo[schema.attributeName];
            } else {
              self.buildJSONData[schema.attributeName] = {};
             
              if (self.userInfo[schema.attributeName].length) {
                self.supportedLanguages.forEach((language) => {
                  let value = self.userInfo[schema.attributeName].filter(function (data) {
                    if (data.language.trim() === language.trim()) {
                      return data.value.trim()
                    }
                  });
                  if(value[0]){
                    self.buildJSONData[schema.attributeName][language] = value[0].value;
                 }
                });
              }
            }
          }
        }
      }
      
     
      this.getGender();
      this.getLocationHierarchyLevel();
      this.getDocumentType("POI", "proofOfIdentity"); this.getDocumentType("POA", "proofOfAddress");
      this.isLoading = false;
    } catch (ex) {
      console.log("Exception>>>" + ex.message);
    }

    if(this.buildJSONData['preferredLang']){
      let perfLangs = this.buildJSONData['preferredLang'].split(',');
      perfLangs.forEach(data => {
        this.perfLangArr[data] = defaultJson['languages'][data]['nativeName']
      })
      this.buildJSONData['preferredLang'] = this.perfLangArr[localStorage.getItem("langCode")];
    }
  }

  changedBuildData(finaluserInfoClone: any) {
    this.buildCloneJsonData = {};
    let self = this;
    for (var schema of self.schema['identity']) {
      if (finaluserInfoClone[schema.attributeName]) {
        if (typeof finaluserInfoClone[schema.attributeName] === "string") {
          self.buildCloneJsonData[schema.attributeName] = finaluserInfoClone[schema.attributeName];
        } else {
          self.buildCloneJsonData[schema.attributeName] = {};
          self.supportedLanguages.forEach((language) => {
            let value = finaluserInfoClone[schema.attributeName].filter(
              function (data) {
                if (data.language) {
                  if (data.language.trim() === language.trim()) {
                    return data.value.trim()
                  }
                }
              });
            if (value.length > 0) {
              self.buildCloneJsonData[schema.attributeName][language] = value[0].value;
            }
          });
        }
      }
    }
  }

  addingAddessData() {
    Object.keys(this.userInfo).forEach(data => {
      Object.keys(this.dynamicFieldValue).forEach(item => {
        let changedItem = item === "Postal Code" ? "postalCode" : item.split(" ").join("").toLowerCase();
        if (changedItem === data) {
          if (this.dynamicFieldValue[item] !== "") {
            if (typeof this.userInfo[data] === "string") {
              this.userInfoAddressClone[changedItem] = this.dynamicFieldValue[item].name
            } else {
              let newData = this.userInfo[changedItem].map(newItem => {
                newItem["value"] = this.dynamicFieldValue[item].name
                return newItem
              })
              this.userInfoAddressClone[changedItem] = newData
            }

          }
        }
      })
    })
  }

  previewBtn(issue: any) {
    if (issue === "address") {
      this.auditService.audit('RP-028', 'Update my data', 'RP-Update my data', 'Update my data', 'User clicks on "submit" button in update my address');
      this.changedBuildData(this.userInfoAddressClone);
      this.finalUserCloneData = this.userInfoAddressClone;
      this.uploadedFiles = this.filesPOA;
    } else if (issue === "identity") {
      this.changedBuildData(this.userInfoClone);
      this.finalUserCloneData = this.userInfoClone;
      this.uploadedFiles = this.files;
      this.auditService.audit('RP-027', 'Update my data', 'RP-Update my data', 'Update my data', 'User clicks on "submit" button in update my data');
    }
    this.showPreviewPage = true;
    this.updatingtype = issue;
  }


  getDocumentType(type: string, id: string) {
    this.dataStorageService.getDataForDropDown("/proxy/masterdata/documenttypes/" + type + "/" + localStorage.getItem("langCode")).subscribe(response => {
      this.dropDownValues[id] = response["response"]["documents"];
    });
  }

  getGender() {
    this.dataStorageService.getDataForDropDown("/auth-proxy/masterdata/dynamicfields/gender" + "/" + localStorage.getItem("langCode") + "?withValue=true").subscribe(response => {
      this.dropDownValues["gender"] = response["response"]['values'];
    });
  }

  getLocationHierarchyLevel() {
    let self = this;
    let fieldNameData = {};
    self.locationFieldNameList = [];
    self.dataStorageService.getLocationHierarchyLevel(self.langCode).subscribe(response => {
      response["response"]["locationHierarchyLevels"].forEach(function (value) {
        if (value.hierarchyLevel != 0)
          if (value.hierarchyLevel <= self.locCode)
            self.locationFieldNameList.push(value.hierarchyLevelName);
      });
      for (let value of self.locationFieldNameList) {
        self.dynamicDropDown[value] = [];
        self.dynamicFieldValue[value] = "";
      }
      self.loadLocationDataDynamically("", 0);
    });
  }

  loadLocationDataDynamically(event: any, index: any) {
    let locationCode = "";
    let fieldName = "";
    let self = this;
    if (event === "") {
      fieldName = this.locationFieldNameList[parseInt(index)];
      locationCode = this.initialLocationCode;
    } else {
      fieldName = this.locationFieldNameList[parseInt(index)];
      locationCode = event.value.code;
      this.dynamicFieldValue[this.locationFieldNameList[parseInt(index) - 1]] = event.value;
    }
    this.dataStorageService.getImmediateChildren(locationCode, this.langCode)
      .subscribe(response => {
        if (response['response'])
          self.dynamicDropDown[fieldName] = response['response']['locations'];
      });
    this.addingAddessData()
  }


  sendOTPBtn(id: any) {
    if (id === "email") {
      this.userIdPhone = "";
      this.confirmPhoneContact = "";
      this.auditService.audit('RP-029', 'Update my data', 'RP-Update my data', 'Update my data', 'User clicks on "Send OTP" button in update email Id');
    } else if (id === "phone") {
      this.userIdEmail = "";
      this.confirmEmailContact = "";
      this.auditService.audit('RP-030', 'Update my data', 'RP-Update my data', 'Update my data', 'User clicks on "Send OTP" button in update phone number');
    }

    this.generateOtp()
  }

  generateOtp() {
    this.transactionID = window.crypto.getRandomValues(new Uint32Array(1)).toString();
    if (this.transactionID.length < 10) {
      let diffrence = 10 - this.transactionID.length;
      for (let i = 0; i < diffrence; i++) {
        this.transactionID = this.transactionID + i
      }
    }
    const request = {
      "id": "mosip.resident.contact.details.send.otp.id",
      "version": this.appConfigService.getConfig()['mosip.resident.request.response.version'],
      "requesttime": Utils.getCurrentDate(),
      "request": {
        "userId": this.userId,
        "transactionId": this.transactionID
      }
    }
    this.dataStorageService.generateOtpForDemographicData(request).subscribe(response => {
      if (response["response"]) {
        this.showOTPPopup()
      } else {
        this.showErrorPopup(response["errors"])
      }
    }, error => {
      console.log(error)
    })
  }

  reGenerateOtp() {
    this.transactionID = window.crypto.getRandomValues(new Uint32Array(1)).toString();
    if (this.transactionID.length < 10) {
      let diffrence = 10 - this.transactionID.length;
      for (let i = 0; i < diffrence; i++) {
        this.transactionID = this.transactionID + i
      }
    }

    const request = {
      "id": "mosip.resident.contact.details.send.otp.id",
      "version": this.appConfigService.getConfig()['mosip.resident.request.response.version'],
      "requesttime": Utils.getCurrentDate(),
      "request": {
        "userId": this.userId,
        "transactionId": this.transactionID
      }
    }
    this.dataStorageService.generateOtpForDemographicData(request).subscribe(response => {
      if (response["response"]) {

      } else {
        this.showErrorPopup(response["errors"])
      }
    }, error => {
      console.log(error)
    })
  }

  verifyupdatedData(otp: any) {
    this.dialog.closeAll();
    this.isLoading = true;
    const request = {
      "id": "mosip.resident.contact.details.update.id",
      "version": this.appConfigService.getConfig()['mosip.resident.request.response.version'],
      "requesttime": Utils.getCurrentDate(),
      "request": {
        "userId": this.userId,
        "transactionId": this.transactionID,
        "otp": otp
      }
    }
    this.dataStorageService.verifyUpdateData(request).subscribe(response => {
      if (response.body['response']) {
        let eventId = response.headers.get("eventid")
        this.message = this.contactTye === 'email' ? this.popupMessages.genericmessage.updateMyData.emailSuccessMsg.replace("$eventId", eventId) : this.popupMessages.genericmessage.updateMyData.phoneNumberSuccessMsg.replace("$eventId", eventId);
        this.isLoading = false;
        this.showMessage(this.message, eventId);
        this.sendOtpDisable = true;
        this.contactTye = "";
        this.router.navigate(['uinservices/dashboard']);
      } else {
        this.isLoading = false;
        this.showErrorPopup(response.body["errors"]);
        this.sendOtpDisable = true;
        this.contactTye = "";
      }
    }, error => {
      console.log(error);
    })
  }

  captureValue(event: any, formControlName: string, language: string, currentValue: any) {
    let self = this;
    if (event.target.value !== '' && event.target.value !== currentValue) {
      if (event.target.value === "" && this.userInfoClone[formControlName]) {
        this.userInfoClone[formControlName].forEach(item => {
          if (item.language === language) {
            let index = this.userInfoClone[formControlName].findIndex(data => data === item)
            this.userInfoClone[formControlName].splice(index, 1)
          }
        })
      } else {
        if ((formControlName !== "proofOfIdentity") && (formControlName !== "proofOfAddress")) {
          if (typeof self.userInfo[formControlName] === "string") {
            self.userInfo[formControlName] = event.target.value;
          } else {
            let index = self.userInfo[formControlName].findIndex(data => data.language.trim() === language.trim());
            let newData = { "language": language, "value": event.target.value };
            if (formControlName in this.userInfoClone) {
              this.userInfoClone[formControlName].forEach(item => {
                if (item['language'] === language) {
                  item['value'] = event.target.value;
                } else {
                  if (item['language']) {
                    if (this.userInfoClone[formControlName]) {
                      this.userInfoClone[formControlName] = this.userInfoClone[formControlName].concat(newData);
                    } else {
                      this.userInfoClone[formControlName] = [].concat(newData);
                    }
                  }
                }
              })
            } else {
              this.userInfoClone[formControlName] = [].concat(newData);
            }
          }
        } else {
          self[formControlName]["documentreferenceId"] = event.target.value;
        }
      }
    }
    this.userInputValues[formControlName] = event.target.value;
  }

  captureDatePickerValue(event: any, formControlName: string, currentValue: any) {
    let self = this;
    let dateFormat = new Date(event.target.value);
    let formattedDate = dateFormat.getFullYear() + "/" + ("0" + (dateFormat.getMonth() + 1)).slice(-2) + "/" + ("0" + dateFormat.getDate()).slice(-2);
    this.selectedDate = dateFormat;
    if (formattedDate !== currentValue) {
      if (event.target.value === null && this.userInfoClone["dateOfBirth"]) {
        delete this.userInfoClone["dateOfBirth"]
      } else {
        self.userInfoClone[formControlName] = formattedDate;
      }
    }
    this.userInputValues[formControlName] = formattedDate;
  }

  captureDropDownValue(event: any, formControlName: string, language: string, dataType: string, currentValue: any) {
    let self = this;
    if (event.source.selected && event.source.viewValue !== currentValue) {
      if ((formControlName !== "proofOfIdentity") && (formControlName !== "proofOfAddress")) {
        if (dataType === "string") {
          this.userInfoClone[formControlName] = event.source.viewValue;
        } else {
          let newData = { "language": language, "value": event.source.viewValue }
          if (formControlName in this.userInfoClone) {
            this.userInfoClone[formControlName].forEach(item => {
              if (item['language'] === language) {
                item['value'] = event.source.viewValue;
              } else {
                if (item['language']) {
                  if (this.userInfoClone[formControlName]) {
                    this.userInfoClone[formControlName] = this.userInfoClone[formControlName].concat(newData);
                  } else {
                    this.userInfoClone[formControlName] = [].concat(newData);
                  }
                }
              }
            })
          } else {
            this.userInfoClone[formControlName] = [].concat(newData);
          }
        }
      } else {
        if (formControlName === "proofOfIdentity") {
          this.displayPOIUpload = true;
        } else if (formControlName === "proofOfAddress") {
          this.displayPOAUpload = true;
        }
        self[formControlName]["documenttype"] = event.source.value;
      }
    }
    this.userInputValues[formControlName] = event.source.viewValue;
  }

  captureAddressValue(event: any, formControlName: string, language: string, currentValue: any) {
    let self = this;
    if (event.target.value !== '' && event.target.value !== currentValue) {
      if (event.target.value === "" && this.userInfoAddressClone[formControlName]) {
        this.userInfoAddressClone[formControlName].forEach(item => {
          if (item.language === language) {
            let index = this.userInfoAddressClone[formControlName].findIndex(data => data === item)
            this.userInfoAddressClone[formControlName].splice(index, 1)
          }
        })
      } else {
        if ((formControlName !== "proofOfIdentity") && (formControlName !== "proofOfAddress")) {
          if (typeof self.userInfo[formControlName] === "string") {
            self.userInfo[formControlName] = event.target.value;
          } else {
            let index = self.userInfo[formControlName].findIndex(data => data.language.trim() === language.trim());
            let newData = { "language": language, "value": event.target.value };
            if (formControlName in this.userInfoAddressClone) {
              this.userInfoAddressClone[formControlName].forEach(item => {
                if (item['language'] === language) {
                  item['value'] = event.target.value;
                } else {
                  if (item['language']) {
                    if (this.userInfoAddressClone[formControlName]) {
                      this.userInfoAddressClone[formControlName] = this.userInfoAddressClone[formControlName].concat(newData);
                    } else {
                      this.userInfoAddressClone[formControlName] = [].concat(newData);
                    }
                  }
                }
              })
            } else {
              this.userInfoAddressClone[formControlName] = [].concat(newData);
            }
          }
        } else {
          self[formControlName]["documentreferenceId"] = event.target.value;
        }
      }
    }
    this.userInputValues[formControlName] = event.target.value;
  }


  captureAddressDropDownValue(event: any, formControlName: string, language: string, dataType: string, currentValue: any) {
    let self = this;
    if (event.source.selected && event.source.viewValue !== currentValue) {
      if ((formControlName !== "proofOfIdentity") && (formControlName !== "proofOfAddress")) {
        if (dataType === "string") {
          this.userInfoAddressClone[formControlName] = event.source.viewValue;
        } else {
          let newData = { "language": language, "value": event.source.viewValue }
          if (formControlName in this.userInfoAddressClone) {
            this.userInfoAddressClone[formControlName].forEach(item => {
              if (item['language'] === language) {
                item['value'] = event.source.viewValue;
              } else {
                if (item['language']) {
                  if (this.userInfoAddressClone[formControlName]) {
                    this.userInfoAddressClone[formControlName] = this.userInfoAddressClone[formControlName].concat(newData);
                  } else {
                    this.userInfoAddressClone[formControlName] = [].concat(newData);
                  }
                }
              }
            })
          } else {
            this.userInfoAddressClone[formControlName] = [].concat(newData);
          }
        }
      } else {
        if (formControlName === "proofOfIdentity") {
          this.displayPOIUpload = true;
        } else if (formControlName === "proofOfAddress") {
          this.displayPOAUpload = true;
        }
        self[formControlName]["documenttype"] = event.source.value;
      }
    }
    this.userInputValues[formControlName] = event.source.viewValue;

  }

  captureContactValue(event: any, formControlName) {
    this.userId = event.target.value;
    this.contactTye = formControlName;

    if (formControlName === "email" && this.userId) {
      this.userIdEmail = this.userId.toLowerCase();
      this.sendOtpDisable = this.userIdEmail === this.confirmEmailContact ? false : true;
    } else if (formControlName === "phone" && this.userId) {
      this.userIdPhone = this.userId;
      this.sendOtpDisable = this.userIdPhone === this.confirmPhoneContact ? false : true;
    }

    if (this[formControlName]) {
      if (formControlName === "email") {
        this.showNotMatchedMessageEmail = this.userIdEmail === this.confirmEmailContact ? false : true;
      } else {
        this.showNotMatchedMessagePhone = this.userIdPhone === this.confirmPhoneContact ? false : true;
      }
    }
  }

  captureConfirmValue(event: any, formControlName: any) {
    this[formControlName] = event.target.value;
    this.contactTye = formControlName;

    if (formControlName === "email") {
      this.confirmEmailContact = event.target.value.toLowerCase();
      this.showNotMatchedMessageEmail = this.userIdEmail === this.confirmEmailContact ? false : true;
      this.sendOtpDisable = this.userIdEmail === this.confirmEmailContact ? false : true;
    } else if (formControlName === "phone") {
      this.confirmPhoneContact = event.target.value;
      this.showNotMatchedMessagePhone = this.userIdPhone === this.confirmPhoneContact ? false : true;
      this.sendOtpDisable = this.userIdPhone === this.confirmPhoneContact ? false : true;
    }
  }

  capturePerfLang(event: any, formControlName: string, language: string) {
    this.userInputValues[formControlName] = event.source.viewValue;
    this.userPrefLang[formControlName] = event.source.viewValue;
  }

  updateBtn() {
    this.conditionsForupdateDemographicData();
  }

  uploadFiles(files, transactionID, docCatCode, docTypCode, referenceId) {
    this.dataStorageService.uploadfile(files, transactionID, docCatCode, docTypCode, referenceId).subscribe(response => {
      console.log(response)
    });
  }

  updateDemographicData() {
    this.isLoading = true;
    let transactionID = window.crypto.getRandomValues(new Uint32Array(1)).toString();
    if (transactionID.length < 10) {
      let diffrence = 10 - transactionID.length;
      for (let i = 0; i < diffrence; i++) {
        transactionID = transactionID + i
      }
    }
    if (this.updatingtype === "identity") {
      this.files.forEach((eachFile) => {
        const formData = new FormData();
        formData.append('file', eachFile);
        this.uploadFiles(formData, transactionID, 'POI', this.proofOfIdentity['documenttype'], this.proofOfIdentity['documentreferenceId']);
      })
    }
    if (this.updatingtype === "address") {
      this.filesPOA.forEach(eachFile => {
        const formData = new FormData();
        formData.append('file', eachFile);
        this.uploadFiles(formData, transactionID, 'POA', this.proofOfAddress['documenttype'], this.proofOfAddress['documentreferenceId']);
      })
    }

    setTimeout(() => {
      const request = {
        "id": this.appConfigService.getConfig()["resident.updateuin.id"],
        "version": this.appConfigService.getConfig()["resident.vid.version.new"],
        "requesttime": Utils.getCurrentDate(),
        "request": {
          "transactionID": transactionID,
          "consent": "Accepted",
          "identity": this.finalUserCloneData
        }
      };
      this.dataStorageService.updateuin(request).subscribe(response => {
        let eventId = response.headers.get("eventid")
        this.message = this.popupMessages.genericmessage.updateMyData.newDataUpdatedSuccessMsg.replace("$eventId", eventId)
        if (response.body["response"]) {
          this.isLoading = false;
          this.showMessage(this.message, eventId);
          this.router.navigate(['uinservices/dashboard']);
        } else {
          this.isLoading = false;
          this.showErrorPopup(response.body["errors"])
        }
      }, error => {
        console.log(error)
      })
    }, 4000)
  }

  updatenotificationLanguage() {
    this.auditService.audit('RP-031', 'Update my data', 'RP-Update my data', 'Update my data', 'User clicks on "submit" button in update notification language');
    const request = {
      "id": this.appConfigService.getConfig()["resident.updateuin.id"],
      "version": this.appConfigService.getConfig()["resident.vid.version.new"],
      "requesttime": Utils.getCurrentDate(),
      "request": {
        "transactionID": null,
        "consent": "Accepted",
        "identity": this.userPrefLang
      }
    };
    this.dataStorageService.updateuin(request).subscribe(response => {
      let eventId = response.headers.get("eventid")
      this.message = this.popupMessages.genericmessage.updateMyData.newDataUpdatedSuccessMsg.replace("$eventId", eventId)
      if (response.body["response"]) {
        this.showMessage(this.message, eventId);
        this.router.navigate(['uinservices/dashboard']);
      } else {
        this.showErrorPopup(response.body["errors"])
      }
    }, error => {
      console.log(error)
    })
  }

  conditionsForupdateDemographicData() {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '650px',
      data: {
        id: "updateMyData",
        case: 'termsAndConditionsForUpdateMyData',
        title: this.popupMessages.genericmessage.termsAndConditionsLabel,
        conditions: this.popupMessages.genericmessage.conditionsForupdateDemographicData,
        agreeLabel: this.popupMessages.genericmessage.agreeLabelForUpdateData,
        btnTxt: this.popupMessages.genericmessage.submitButton
      }
    });
    return dialogRef;
  }
  closePreview(fileType: any) {
    if (fileType === "POI") {
      this.pdfSrc = "";
      this.selectedPOIFileForPreview = "";
    } else {
      this.pdfSrcPOA = "";
      this.selectedPOAFileForPreview = "";
    }

  }
  /**
   * on file drop handler
   */
  onFileDropped($event, type) {
    this.prepareFilesList($event, type);
  }

  /**
   * handle file from browsing
   */
  fileBrowseHandler(files, type) {
    this.prepareFilesList(files, type);
  }

  /**
   * Preview file from files list
   * @param index (File index)
   */
  previewFile(index: number, type: string) {
    if (type === "POI") {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.pdfSrc = e.target.result;
      };
      reader.readAsDataURL(this.files[index]);
      this.selectedPOIFileForPreview = this.files[index].name;
    } else {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.pdfSrcPOA = e.target.result;
      };
      reader.readAsDataURL(this.filesPOA[index]);
      this.selectedPOAFileForPreview = this.filesPOA[index].name;
    }
  }

  previewFileInPreviewPage(index: number, filename: string) {
    this.selectedFileInPreviewPage = filename;
    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.pdfSrcInPreviewPage = e.target.result;
    };
    reader.readAsDataURL(this.uploadedFiles[index]);
  }

  closePreviewPageImage() {
    this.selectedFileInPreviewPage = '';
    this.pdfSrcInPreviewPage = '';
  }

  /**
   * Delete file from files list
   * @param index (File index)
   */
  deleteFile(index: number, type: string) {
    if (type === "POI") {
      this.files.splice(index, 1);
      this.uploadedFiles = this.files
    } else {
      this.filesPOA.splice(index, 1);
      this.uploadedFiles = this.filesPOA
    }
    if (this.files.length < 1) {
      this.previewDisabled = true;
      this.selectedPOIFileForPreview = "";
    }
    if (this.filesPOA.length < 1) {
      this.previewDisabledInAddress = true;
      this.selectedPOAFileForPreview = "";
    }
    this.pdfSrc = "";
  }

  /**
   * Simulate the upload process
   */
  uploadFilesSimulator(index: number, type: string) {
    setTimeout(() => {
      if (type === "POI") {
        if (index === this.files.length) {
          return;
        } else {
          if (this.files.length) {
            const progressInterval = setInterval(() => {
              if (this.files[index].progress === 100) {
                clearInterval(progressInterval);
                this.uploadFilesSimulator(index + 1, type);
              } else {
                this.files[index].progress += 20;
              }
            }, 200);
          }
        }
      } else {
        if (index === this.filesPOA.length) {
          return;
        } else {
          const progressInterval = setInterval(() => {
            if (this.filesPOA[index].progress === 100) {
              clearInterval(progressInterval);
              this.uploadFilesSimulator(index + 1, type);
            } else {
              this.filesPOA[index].progress += 20;
            }
          }, 200);
        }
      }
    }, 1000);
  }

  /**
   * Convert Files list to normal array list
   * @param files (Files List)
   */
  prepareFilesList(files: Array<any>, type: string) {
    var allowedFiles = ["image/jpg", "image/jpeg", "image/png", "application/pdf"];
    var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(" + allowedFiles.join('|') + ")$");
    let fileSize = (files[0].size) / 1048576;
    if (!allowedFiles.includes(files[0].type)) {
      if (type === "POI") {
        this.isValidFileFormatPOI = true
      } else {
        this.isValidFileFormatPOA = true
      }
      this.warningMessage = this.popupMessages.updatedemographic.InvalidFormatMsg
    } else {
      if (fileSize < 2.0) {
        if (type === "POI") {
          this.isValidFileFormatPOI = false;
          for (const item of files) {
            item.progress = 0;
            this.files.push(item);
          }
          this.uploadFilesSimulator(0, type);
          this.previewDisabled = false
        } else {
          this.isValidFileFormatPOA = false;
          for (const item of files) {
            item.progress = 0;
            this.filesPOA.push(item);
          }
          this.uploadFilesSimulator(0, type);
          this.previewDisabledInAddress = false
        }
      } else {
        if (type === "POI") {
          this.isValidFileFormatPOI = true
        } else {
          this.isValidFileFormatPOA = true
        }
        this.warningMessage = this.popupMessages.updatedemographic.InvalidFileSize
      }
    }
  }

  /**
   * format bytes
   * @param bytes (File size in bytes)
   * @param decimals (Decimals point)
   */

  formatBytes(bytes, decimals) {
    if (bytes === 0) {
      return '0 Bytes';
    }
    const k = 1024;
    const dm = decimals <= 0 ? 0 : decimals || 2;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }

  showOTPPopup() {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'OTP',
        message:this.popupMessages.genericmessage.otpPopupDescription,
        newContact: this.userId,
        submitBtnTxt: this.popupMessages.genericmessage.submitButton,
        resentBtnTxt: this.popupMessages.genericmessage.resentBtn
      }
    });
    return dialogRef;
  }

  showMessage(message: string, eventId: any) {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.successLabel,
        trackStatusText: this.popupMessages.genericmessage.trackStatusText,
        clickHere: this.popupMessages.genericmessage.clickHere,
        message: message,
        eventId: eventId,
        clickHere2: this.popupMessages.genericmessage.clickHere2,
        dearResident: this.popupMessages.genericmessage.dearResident,
        btnTxt: this.popupMessages.genericmessage.successButton
      }
    });
    return dialogRef;
  }

  showErrorPopup(message: string) {
    this.errorCode = message[0]["errorCode"];
    setTimeout(() => {
      if (this.errorCode === "RES-SER-410") {
        let messageType = message[0]["message"].split("-")[1].trim();
        this.message = this.popupMessages.serverErrors[this.errorCode][messageType]
      } else {
        this.message = this.popupMessages.serverErrors[this.errorCode]
      }
      this.dialog
        .open(DialogComponent, {
          width: '650px',
          data: {
            case: 'MESSAGE',
            title: this.popupMessages.genericmessage.errorLabel,
            message: this.message,
            btnTxt: this.popupMessages.genericmessage.successButton
          },
          disableClose: true
        });
    }, 400)
  }


  onItemSelected(item: any) {
    if (item === "matTabLabel") {
      this.showPreviewPage = false;
    } else {
      this.router.navigate([item]);
    }
  }

  typeOf(value: any) {
    return typeof value
  }

  backBtn() {
    this.showPreviewPage = false;
    this.selectedFileInPreviewPage = '';
    this.pdfSrcInPreviewPage = '';
  }

  logChange(event: any) {
    this.matTabIndex = event.index;
    this.matTabLabel = event.tab.textLabel;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
    this.clickEventSubscription.unsubscribe();
  }
}