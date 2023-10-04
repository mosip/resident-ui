import { Component, OnInit, OnDestroy, ViewChildren, ElementRef, HostListener } from "@angular/core";
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
import { AutoLogoutService } from "src/app/core/services/auto-logout.service";
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { BreakpointService } from "src/app/core/services/breakpoint.service";
import {
  MatKeyboardRef,
  MatKeyboardComponent,
  MatKeyboardService
} from 'ngx7-material-keyboard';
import {
  MAT_MOMENT_DATE_FORMATS,
  MomentDateAdapter,
  MAT_MOMENT_DATE_ADAPTER_OPTIONS,
} from '@angular/material-moment-adapter';

@Component({
  selector: "app-demographic",
  templateUrl: "updatedemographic.component.html",
  styleUrls: ["updatedemographic.component.css"],
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
    },
    {provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS},
  ],
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
  buildCloneJsonData: any = [];
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
  matTabLabel: string;
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
  isLoading: boolean = true;
  showNotMatchedMessageEmail: boolean = false;
  showNotMatchedMessagePhone: boolean = false;
  email: any;
  phone: any;
  userInputValues: any = {};
  finalUserCloneData: any;
  updatingtype: string;
  sitealignment: string = localStorage.getItem('direction');
  transactionIDForPOI:string = "";
  transactionIDForPOA:string = "";
  getAllDocIds:any = {};
  isSelectedAllAddress:boolean = true;
  fieldName:string;
  oldKeyBoradIndex:number;
  getUserPerfLang = [];
  getUserPerfLangString:string = "";
  attributeUpdateCountMaxLimit:any;
  attributeUpdateCountRemainLimit:any = {};
  selectedOptionData:any;
  oldSelectedIndex:any;
  
  private keyboardRef: MatKeyboardRef<MatKeyboardComponent>;
  @ViewChildren('keyboardRef', { read: ElementRef })
  private attachToElementMesOne: any;
  constructor(private autoLogout: AutoLogoutService, private interactionService: InteractionService,
    private dialog: MatDialog, private dataStorageService: DataStorageService,
    private translateService: TranslateService, private router: Router,
    private appConfigService: AppConfigService, private auditService: AuditService,
    private dateAdapter: DateAdapter<Date>, private breakPointService: BreakpointService, 
    private keyboardService: MatKeyboardService) {
    this.clickEventSubscription = this.interactionService.getClickEvent().subscribe((id) => {
      if (id === "updateMyData") {
        this.updateDemographicData();
      } else if (id === "resend") {
        this.reGenerateOtp();
      } else if (id !== 'string' && id.type === 'otp') {
        this.verifyupdatedData(id.otp);
      }
    });

    this.breakPointService.isBreakpointActive().subscribe(active => {
      if (active) {
        if (active === "extraSmall") {
          this.cols = 1;
          this.width = "99%";
        }
        if (active === "ExtraLarge") {
          this.cols = 4;
          this.width = "40%";
        }
        if (active === "medium") {
          this.cols = 2;
          this.width = "75%";
        }
        if (active === "large") {
          this.cols = 4;
          this.width = "50%";
        }
        if (active === "small") {
          this.cols = 2;
          this.width = "95%";
        }
      }
    });

    this.dateAdapter.setLocale('en-GB');
  }

  async ngOnInit() {
    this.defaultJsonValue = { ...defaultJson }
    this.dateAdapter.setLocale(defaultJson.keyboardMapping[this.langCode]);
    this.initialLocationCode = "MOR";
    this.locCode = 5;
    this.translateService.use(localStorage.getItem("langCode"));
    this.supportedLanguages = ["eng"];
    this.translateService
      .getTranslation(localStorage.getItem("langCode"))
      .subscribe(response => {
        this.langJson = response.updatedemographic
        this.popupMessages = response;
        this.matTabLabel = response.updatedemographic.identity;
      });

    let supportedLanguages = this.appConfigService.getConfig()['supportedLanguages'].split(',');
    supportedLanguages.forEach(data => {
      let newObj = { "code": data, "name": this.defaultJsonValue['languages'][data]['nativeName'] }
      this.newNotificationLanguages.push(newObj)
    })

    this.getUpdateMyDataSchema();
    this.getUserInfo();
    await this.getMappingData();
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

  createTransactionIds(){
    let transactionID = window.crypto.getRandomValues(new Uint32Array(1)).toString();
    if (transactionID.length < 10) {
      let diffrence = 10 - transactionID.length;
      for (let i = 0; i < diffrence; i++) {
        transactionID = transactionID + i
      }
    }
    return transactionID
  }


  async getUpdateMyDataSchema() {
    this.isLoading = true;
    await new Promise((resolve) => {
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
          this.userInfo['fullName'].forEach(item=>{
            this.getUserPerfLang.indexOf(item.language) === -1 ? this.getUserPerfLang.push(item.language) : ''
          }) 
          UpdatedemographicComponent.actualData = response["response"];
          if (this.schema && this.userInfo) {
            this.buildData()
            this.isLoading = false;
          } else {
            this.getUpdateMyDataSchema();
            this.getUserInfo();
          }
        } else {
          this.showErrorPopup(response['errors'])
        }
      });
  }

  async getMappingData(){
    this.dataStorageService.getMappingData().subscribe((response) =>{
      if(response){
        this.attributeUpdateCountMaxLimit = {...response['attributeUpdateCountLimit']}
        this.getUpdateDataCount();
      }
    })
  }

  async getUpdateDataCount(){
    this.dataStorageService.getUpdateDataCount().subscribe((response) =>{
      this.attributeUpdateCountRemainLimit = {...this.attributeUpdateCountMaxLimit}
      if(response['response']){
        response['response'].attributes.forEach(item =>{
          this.attributeUpdateCountRemainLimit[item.attributeName] = item.noOfUpdatesLeft
        })
      }
    })

  }

  buildData() {
    let count = 0;
    try {
      let self = this;
      for (var schema of self.schema['identity']) {
        if(schema.controlType === "textbox" || schema.controlType === "fileupload"){
          if(schema.controlType === "textbox"){
            if(typeof self.userInfo[schema.attributeName] === "string"){
              this.userInputValues[schema.attributeName] = "";
              this.buildJSONData[schema.attributeName] = {value:self.userInfo[schema.attributeName],index:count,confIndex:count+1}
              count++
              count++
            }else{
              this.userInputValues[schema.attributeName] = {};
              this.buildJSONData[schema.attributeName] = self.userInfo[schema.attributeName].map(item =>{
                item.index = count
                count++
                this.userInputValues[schema.attributeName][item.language] = '';
                return item
              })
            }
          }else{
            this.buildJSONData[schema.attributeName] = count
            count++
          }
        }else{
          if(typeof self.userInfo[schema.attributeName] === "string"){
            this.userInputValues[schema.attributeName] = "";
            this.buildJSONData[schema.attributeName] = {value:self.userInfo[schema.attributeName]}
          }else{
            this.userInputValues[schema.attributeName] = {};
            this.buildJSONData[schema.attributeName] = self.userInfo[schema.attributeName]
            self.userInfo[schema.attributeName].forEach(item =>{
                this.userInputValues[schema.attributeName][item.language] = ''
              })
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
    this.getUserPerfLang.forEach(item =>{
      this.getUserPerfLangString = this.getUserPerfLangString + item + ","
    })
    this.getUserPerfLangString = this.getUserPerfLangString.replace(/,\s*$/, "");
  }

  changedBuildData(finaluserInfoClone: any) {
    this.buildCloneJsonData = [];
    let self = this;
    for (var schema of self.schema['identity']) {
      Object.keys(finaluserInfoClone).map(attributeName =>{
        if(schema.attributeName === attributeName){
          this.buildCloneJsonData.push({
            'labelName':schema.labelName[this.langCode][1],
            'newData':finaluserInfoClone[attributeName]
          })
        }
      })
    }
  }

  addingAddessData(event:any, formControlName:string, fieldType:string, fieldName:string) {
    let locationCode = event.value
    if(fieldType !== 'string'){
      this.userInfoAddressClone[formControlName] = []
      this.getUserPerfLang.forEach(langCode =>{
        let newData
        this.dynamicDropDown[fieldName][langCode].forEach(eachLocation =>{
          if(eachLocation.code === locationCode){
            newData = { "language": langCode, "value": eachLocation.name }
            this.userInputValues[formControlName][langCode] = eachLocation.code;
          }
        })
        this.userInfoAddressClone[formControlName].push(newData)
      })
    }else{
      this.dynamicDropDown[fieldName]['eng'].forEach(eachLocation =>{
        if(eachLocation.code === locationCode){
          this.userInfoAddressClone[formControlName] = eachLocation.name;
          this.userInfoAddressClone[formControlName] = eachLocation.code;
        }
      })
    }
    
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
    this.dropDownValues["gender"] = {}
    this.dataStorageService.getDataForDropDown("/proxy/masterdata/dynamicfields/all/gender").subscribe(response => {
      if(response['response']){
        response['response'].forEach(eachItem =>{
          this.dropDownValues["gender"][eachItem.langCode] = eachItem.fieldVal
        })
      }
    });
  }

  getLocationHierarchyLevel() {
    let self = this;
    self.locationFieldNameList = [];
    self.dataStorageService.getLocationHierarchyLevel('eng').subscribe(response => {
      response["response"]["locationHierarchyLevels"].forEach(function (value) {
        if (value.hierarchyLevel != 0)
          if (value.hierarchyLevel <= self.locCode)
            self.locationFieldNameList.push(value.hierarchyLevelName);
      });
      for (let value of self.locationFieldNameList) {
        self.dynamicDropDown[value] = [];
      }
      self.loadLocationDataDynamically("", 0,"","");
    });
  }

  loadLocationDataDynamically(event: any, index: any, schemaFieldName:string, fieldType:string) {
    let unSelectedItems = this.locationFieldNameList.slice(index, this.locationFieldNameList.length)
    let locationCode = "";
    let fieldName = "";
    let self = this;
    if (event === "") {
      fieldName = this.locationFieldNameList[parseInt(index)];
      locationCode = this.initialLocationCode;
    } else {
      fieldName = this.locationFieldNameList[parseInt(index)];
      locationCode = event.value;
      this.isSelectedAllAddress = unSelectedItems.length ? false : true;
    }
    
    this.fieldName = fieldName
    if(fieldName){
      this.dataStorageService.getImmediateChildren(locationCode, this.getUserPerfLangString)
      .subscribe(response => {
        if (response['response'])
          self.dynamicDropDown[fieldName] = response['response']['locations'];
      });
    }
  
    if(event !==''){
        this.addingAddessData(event, schemaFieldName, fieldType, this.locationFieldNameList[parseInt(index) -1]);
    }
    
    unSelectedItems.forEach(item =>{
      this.dynamicDropDown[item] = [];
      let filedNameForuserInput = (item.charAt(0).toLocaleLowerCase() + item.slice(1)).replace(" ","")
      if(typeof this.userInputValues[filedNameForuserInput] !== 'string'){
        this.getUserPerfLang.forEach(lang =>{
          this.userInputValues[filedNameForuserInput][lang] = ''
        })
      }else{
        this.userInputValues[filedNameForuserInput] = ''
      }
    });
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
    this.transactionID = this.createTransactionIds()
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
    this.transactionID = this.createTransactionIds();

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
      "id": this.appConfigService.getConfig()["resident.contact.details.update.id"],
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

async translateUserInput(toLang: string, fromLang: string, input:string,formControlName:string, userInfoType:string){
        let request = {
          "id": this.appConfigService.getConfig()["mosip.resident.transliteration.transliterate.id"],
          "version": this.appConfigService.getConfig()['mosip.resident.request.response.version'],
          "requesttime": "2023-09-20T10:43:08.864Z",
          "request": {
            "from_field_value": input,
            "from_field_lang": fromLang,
            "to_field_lang": toLang
          }
        }
        this.dataStorageService.translateUserInput(request).subscribe(response =>{
          if(response['response']){
            let value = response['response'].to_field_value
            this[userInfoType][formControlName].push({"language":toLang, "value" : value})
            this.userInputValues[formControlName][toLang] = value;
          }else{
            this.userInputValues[formControlName][toLang] = '';
          }
        })
  }

captureValue(event: any, formControlName: string, language: string, currentValue: any) {
    let self = this;
      if (event.target.value.trim() === "") {
        if(this.userInfoClone[formControlName]){
          delete this.userInfoClone[formControlName]
        }
        this.getUserPerfLang.forEach(item =>{
          this.userInputValues[formControlName][item] = ''
        })
      } else {
        if (formControlName !== "proofOfIdentity") {
          this.userInfoClone[formControlName] = []
          this.getUserPerfLang.forEach(item =>{
            let newData
            if(item === language){
              newData = { "language": language, "value": event.target.value }
              this.userInfoClone[formControlName].push(newData)
              this.userInputValues[formControlName][language] = event.target.value;
            }else{
              this.translateUserInput(item, language, event.target.value, formControlName, 'userInfoClone')
            }
          })
        } else {
          self[formControlName]["documentreferenceId"] = event.target.value;
          this.userInputValues[formControlName] = event.target.value;
        }
      }
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

  captureDropDownValue(event: any, formControlName: string, language: string, currentValue: any) {
    let genders =this.dropDownValues.gender
    let self = this;
      if (formControlName !== "proofOfIdentity") {
        this.userInfoClone[formControlName] = []
        this.getUserPerfLang.forEach(item =>{
          let newData
          genders[item].forEach(eachGender =>{
            if(eachGender.code === event.value){
              newData = { "language": item, "value": eachGender.value }
              this.userInputValues[formControlName][item] = eachGender.code;
            }
          })
          this.userInfoClone[formControlName].push(newData)
        })
      } else {
        if (formControlName === "proofOfIdentity") {
          this.displayPOIUpload = true;
        } else if (formControlName === "proofOfAddress") {
          this.displayPOAUpload = true;
        }
        self[formControlName]["documenttype"] = event.source.value;
      }
  }

  captureAddressValue(event: any, formControlName: string, language: string) {
    let self = this;
    if (event.target.value.trim() === "") {
      if(this.userInfoClone[formControlName]){
        delete this.userInfoClone[formControlName]
      }
      this.getUserPerfLang.forEach(item =>{
        this.userInputValues[formControlName][item] = ''
      })
    } else {
      if (formControlName !== "proofOfAddress") {
        this.userInfoAddressClone[formControlName] = []
        this.getUserPerfLang.forEach(item =>{
          let newData
          if(item === language){
            newData = { "language": language, "value": event.target.value }
            this.userInfoAddressClone[formControlName].push(newData)
            this.userInputValues[formControlName][language] = event.target.value;
          }else{
            this.translateUserInput(item, language, event.target.value, formControlName, 'userInfoAddressClone')
          }
        })
      } else {
        self[formControlName]["documentreferenceId"] = event.target.value;
        this.userInputValues[formControlName] = event.target.value;
      }
    }
  }


  captureAddressDropDownValue(event: any, formControlName: string) {
    let self = this;
    if (event.source.selected) {
      if (formControlName === "proofOfIdentity") {
        this.displayPOIUpload = true;
      } else if (formControlName === "proofOfAddress") {
        this.displayPOAUpload = true;
      }
      self[formControlName]["documenttype"] = event.source.value;
    }
    this.userInputValues[formControlName] = event.source.viewValue;
  }

  captureContactValue(event: any, formControlName:any) {
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

  capturePerfLang(event: any, formControlName: string) {
    this.userInputValues[formControlName] = event.source.viewValue;
    this.userPrefLang[formControlName] = event.source.viewValue;
  }

  captureVirtualKeyboard(element: HTMLElement, index: number) {
   
    if(this.keyboardRef){
      this.keyboardRef.instance.setInputInstance(this.attachToElementMesOne._results[index]);
    }
    
  }

  openKeyboard(inputId:any, langCode:string) {
    if (this.oldKeyBoradIndex === inputId && this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
      this.keyboardRef = undefined;
    } else {
      this.oldKeyBoradIndex = inputId;
      this.keyboardRef = this.keyboardService.open(defaultJson.keyboardMapping[langCode]);
      document.getElementById(inputId).focus();
    }
  }

  updateBtn() {
    this.conditionsForupdateDemographicData();
  }

  uploadFiles(files, transactionID, docCatCode, docTypCode, referenceId) {
    this.dataStorageService.uploadfile(files, transactionID, docCatCode, docTypCode, referenceId).subscribe(response => {
      if(response['response']){
        this.getAllDocIds[response['response'].docName] = response['response'].docId
      }
    });
  }

  deleteUploadedFile(docId, transactionID){
    this.dataStorageService.deleteUploadedFile(docId, transactionID).subscribe(response =>{
      console.log(response)
    })
  }

  finalUpdateDemographicData(transactionID:any){
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
      let eventId = response.headers.get("eventid");
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
  }

  updateDemographicData() {
    this.isLoading = true;
    if(this.updatingtype === 'identity'){
      this.finalUpdateDemographicData(this.transactionIDForPOI)
    }else{
      this.finalUpdateDemographicData(this.transactionIDForPOA)
    }
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
        this.showPreviewImage(this.pdfSrc)
      };
      reader.readAsDataURL(this.files[index]);
    } else {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.pdfSrcPOA = e.target.result;
        this.showPreviewImage(this.pdfSrcPOA)
      };
      reader.readAsDataURL(this.filesPOA[index]);
    }
  }

  previewFileInPreviewPage(index: number) {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.pdfSrcInPreviewPage = e.target.result;
      this.showPreviewImage(this.pdfSrcInPreviewPage)
    };
    reader.readAsDataURL(this.uploadedFiles[index]);
  }

  /**
   * Delete file from files list
   * @param index (File index)
   */
  deleteFile(index: number, type: string) {
    if (type === "POI") {
      let documentName = this.files[index].name;
      let documentID = this.getAllDocIds[documentName]
      this.files.splice(index, 1);
      this.uploadedFiles = this.files
      this.deleteUploadedFile(documentID, this.transactionIDForPOI)
      this.pdfSrc = "";
    } else {
      let documentName = this.filesPOA[index].name;
      let documentID = this.getAllDocIds[documentName]
      this.filesPOA.splice(index, 1);
      this.uploadedFiles = this.filesPOA
      this.deleteUploadedFile(documentID, this.transactionIDForPOA)
      this.pdfSrcPOA = ""
    }
    if (this.files.length < 1) {
      this.previewDisabled = true;
    }
    if (this.filesPOA.length < 1) {
      this.previewDisabledInAddress = true;
    }
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
          this.transactionIDForPOI = this.transactionIDForPOI || this.createTransactionIds();
          this.isValidFileFormatPOI = false;
          for (const item of files) {
            item.progress = 0;
            this.files.push(item);
            const formData = new FormData()
            formData.append('file', item);
            this.uploadFiles(formData, this.transactionIDForPOI, 'POI', this.proofOfIdentity['documenttype'], this.proofOfIdentity['documentreferenceId']);
          }
          this.uploadFilesSimulator(0, type);
          this.previewDisabled = false
        } else {
          this.transactionIDForPOA = this.transactionIDForPOA || this.createTransactionIds()
          this.isValidFileFormatPOA = false;
          for (const item of files) {
            item.progress = 0;
            this.filesPOA.push(item);
            const formData = new FormData()
            formData.append('file', item);
            this.uploadFiles(formData, this.transactionIDForPOA, 'POA', this.proofOfAddress['documenttype'], this.proofOfAddress['documentreferenceId']);
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
        message: this.popupMessages.genericmessage.otpPopupDescription,
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
        btnTxt: this.popupMessages.genericmessage.successButton,
        isOk: 'OK'
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
            btnTxt: this.popupMessages.genericmessage.successButton,
            isOk: 'OK'
          },
          disableClose: true
        });
    }, 400)
  }

  showPreviewImage(pdfSrc:any){
    this.dialog
    .open(DialogComponent, {
      width: '70%',
      data: {
        case: 'previewImage',
        imageLink:pdfSrc
      },
      disableClose: true
    });
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
    this.pdfSrcInPreviewPage = '';
  }

  logChange(event: any) {
    this.matTabIndex = event.index;
    this.matTabLabel = event.tab.textLabel;
  }

  @HostListener("blur", ["$event"])
  @HostListener("focusout", ["$event"])
  private _hideKeyboard() {
    if (this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
    this.clickEventSubscription.unsubscribe();
  }
}