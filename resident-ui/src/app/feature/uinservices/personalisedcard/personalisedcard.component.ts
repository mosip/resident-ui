import { Component, OnInit, OnDestroy } from "@angular/core";
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from "@ngx-translate/core";
import { Subscription } from "rxjs";
import { Router } from "@angular/router";
import Utils from 'src/app/app.utils';
import { AppConfigService } from 'src/app/app-config.service';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { MatDialog } from '@angular/material';
import { saveAs } from 'file-saver';
import { InteractionService } from "src/app/core/services/interaction.service";
import { AuditService } from "src/app/core/services/audit.service";
import moment from 'moment';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AutoLogoutService } from "src/app/core/services/auto-logout.service";

@Component({
  selector: "app-personalisedcard",
  templateUrl: "personalisedcard.component.html",
  styleUrls: ["personalisedcard.component.css"],
})
export class PersonalisedcardComponent implements OnInit, OnDestroy {
  langJSON: any;
  popupMessages: any;
  subscriptions: Subscription[] = [];
  schema: any;
  langCode: string = "";
  userInfo: any;
  buildHTML: any;
  dataDisplay: any = {};
  message: string;
  formatData: any;
  nameFormatValues: string[];
  addressFormatValues: string[];
  eventId: any;
  givenNameBox: boolean = false;
  downloadBtnDisabled: boolean = true;
  valuesSelected: any = [];
  width : string;
  cols : number;
  message2:any;
  attributeWidth:string;
  fullAddress:string = "";
  formatLabels:any;
  formatCheckBoxClicked:boolean = false;
  isLoading:boolean = true;
  selectedOprionsFormOptions: object = {};

  constructor(private autoLogout: AutoLogoutService,private interactionService: InteractionService, private dialog: MatDialog, private appConfigService: AppConfigService, private dataStorageService: DataStorageService, private translateService: TranslateService, private router: Router, private auditService: AuditService, private breakpointObserver: BreakpointObserver) {
    this.breakpointObserver.observe([
      Breakpoints.XSmall,
      Breakpoints.Small,
      Breakpoints.Medium,
      Breakpoints.Large,
      Breakpoints.XLarge,
    ]).subscribe(result => {
      if (result.matches) {
        if (result.breakpoints[Breakpoints.Medium]) {
          this.cols = 2;
          this.width = "25em";
          this.attributeWidth = "12em";
        }
        if (result.breakpoints[Breakpoints.Small]) {
          this.cols = 1;
          this.width = "40em";
          this.attributeWidth = "20em";
        }
        if (result.breakpoints[Breakpoints.Large]) {
          this.cols = 2;
          this.width = "29em";
          this.attributeWidth = "12em";
        }
        if (result.breakpoints[Breakpoints.XSmall]) {
          this.cols = 1;
          this.width = "19em";
          this.attributeWidth = "10em";
        }
        if (result.breakpoints[Breakpoints.XLarge]) {
          this.cols = 2;
          this.width = "35rem";
          this.attributeWidth = "18em";
        }
      }
    });
  }

  async ngOnInit() {
    this.langCode = localStorage.getItem("langCode");

    this.translateService.use(localStorage.getItem("langCode"));

    this.translateService
      .getTranslation(localStorage.getItem("langCode"))
      .subscribe(response => {
        this.langJSON = response;
        this.popupMessages = response;
      });

    this.getUserInfo();
    this.getMappingData();
    this.getPersonalizedCardSchema()

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

  getMappingData() {
    this.dataStorageService
      .getMappingData()
      .subscribe((response) => {
        this.formatData = { "Address Format": response["identity"]["fullAddress"]["value"].split(","), "Name Format": response["identity"]["name"]["value"].split(","), "Date Format": response["identity"]["dob"]["value"].split(",") }
      })
  }


  getPersonalizedCardSchema(){
    this.dataStorageService
    .getUpdateMyDataSchema('personalized-card')
    .subscribe((response) => {
      this.schema = response["identity"];
      this.schema.forEach(data =>{
        this.valuesSelected.push(data.attributeName)
      })
    });
  }

  getUserInfo() {
    this.dataStorageService
      .getUserInfo('personalized-card')
      .subscribe((response) => {
        if(response['response']){
          this.userInfo = response["response"];
          this.isLoading = false;
        }else{
          this.showErrorPopup(response['errors'])
        }
        
      });
  }

captureCheckboxValue($event: any, data: any, type: any) {
  this.buildHTML = "";
  if (type === "datacheck") {
    if (data.attributeName.toString() in this.dataDisplay) {
      delete this.dataDisplay[data.attributeName];
    } else {
      let value = "";
      if (typeof this.userInfo[data.attributeName] === "string") {
        if(data.attributeName === "dateOfBirth"){
          value = moment(this.userInfo[data.attributeName]).format(data["defaultFormat"]);
        }else{
          value = this.userInfo[data.attributeName];
        }
      } else {
          if (data.formatRequired) {
            if (data.attributeName === "fullAddress") {
              this.fullAddress = ""
              this.schema.forEach(item => {
                if (item.attributeName === data.attributeName) {
                  this.formatLabels = item.formatOption[this.langCode]
                }
              })

              this.formatLabels.forEach(item => {
                if (this.userInfo[item.value] !== undefined) {
                  if (typeof this.userInfo[item.value] !== "string") {
                    this.userInfo[item.value].forEach(eachLang => {
                      if (eachLang.language === this.langCode) {
                        this.fullAddress = this.fullAddress + "," +  eachLang.value
                      }
                    })
                  } else {
                    this.fullAddress = this.fullAddress + this.userInfo[item.value]
                  }
                }
              })
              this.fullAddress = this.fullAddress.replace(/^./, "");
              value = this.fullAddress
            } else {
              this.userInfo[data.attributeName].forEach(item =>{
                if(item.language === this.langCode){
                  value = item.value
                }
              });
            }
          } else {
            this.userInfo[data.attributeName].forEach(item =>{
              if(item.language === this.langCode){
                value = item.value
              }
            });
          }
      }

      if (data.formatRequired) {
        this.dataDisplay[data.attributeName] = { "label": data.label[this.langCode], "attributeName": data['attributeName'], "isMasked": data['maskRequired'], "format": data['defaultFormat'], "value": value };
      } else {
        this.dataDisplay[data.attributeName] = { "label": data.label[this.langCode], "attributeName": data['attributeName'], "isMasked": data['maskRequired'], "value": value };
      }
    }

    this.schema = this.schema.map(item => {
      if (item.attributeName === data.attributeName) {
        let newItem = { ...item, checked: !item.checked }
        if (!newItem.checked && newItem['formatOption']) {
          newItem['formatOption'][this.langCode] = this.selectedOprionsFormOptions[data.attributeName]
        }
        return newItem
      } else {
        return item
      }
    })
  } else {
    if (!data.formatRequired) {
      let value;
      if (this.dataDisplay[data.attributeName].value === this.userInfo[type]) {
        value = this.userInfo[data.attributeName];
      } else {
        value = this.userInfo[type];
      }
      this.dataDisplay[data.attributeName] = { "label": data.label[this.langCode], "attributeName": data['attributeName'], "isMasked": $event.checked, "value": value };
    } else {
      let value = "";
      let allValue = "";
      let self = this;
      if (typeof this.userInfo[data.attributeName] === "string") {
        data.formatOption[this.langCode].forEach(item =>{
          item.checked = !item.checked
          if(item.checked){
            value = moment(this.userInfo[data.attributeName]).format(item["value"]);
          }
        })

      } else {
        this.schema = this.schema.map(eachItem => {
          if (data['attributeName'] === eachItem['attributeName']) {
            eachItem['formatOption'][this.langCode].forEach(item => {
              if (item.value === type['value']) {
                return item['checked'] = !item['checked']
              } else {
                return item['checked'];
              }
            })
          }
          return eachItem
        })

        if (data.attributeName === "fullAddress") {
          let selectedValuesCount = 0;
          if (type["value"] !== 'fullAddress') {
            this.schema.map(eachItem => {
              if (data['attributeName'] === eachItem['attributeName']) {
                eachItem['formatOption'][this.langCode].forEach((item) => {
                  if (item.checked) {
                    if (self.userInfo[item.value] !== undefined) {
                      if (item.value === "postalCode") {
                        allValue = allValue + self.userInfo[item.value];
                      } else {
                        this.userInfo[item.value].forEach(eachLang => {
                            if (eachLang.language === this.langCode) {
                              allValue = allValue + eachLang.value + ",";
                            }
                        })
                      }
                    }
                  }
                  return "";
                });
              }
            });
            
            let unCheckFullAddress = () =>{
              data.formatOption[this.langCode].forEach(item =>{
                if(item.value === "fullAddress"){
                  item['checked'] = false;
                }
              })
            }
            
            for(let item of data.formatOption[this.langCode]){
              if(!item.checked && item.value !== "fullAddress"){
                  unCheckFullAddress();
                  break;
              }else{
                item.checked = true;
              }
            }

            if (allValue.endsWith(',')) {
              allValue = allValue.replace(/.$/, '')
            }
            value = allValue;
          } else {
              value = this.fullAddress
              data.formatOption[this.langCode].forEach(item =>{
                item.checked = true;
              })
          }

          for (let eachItem of data.formatOption[this.langCode]){
            if(!eachItem.checked){
             selectedValuesCount += 1
            }
         }
         
         if(selectedValuesCount === data.formatOption[this.langCode].length){
           data.checked = false;
           delete this.dataDisplay[data.attributeName];
           data.formatOption[this.langCode].forEach(item =>{
            item.checked = true;
          })
         $event.closeMenu();
         }
        }else{
          data.checked = false;
          delete this.dataDisplay[data.attributeName];
          data.formatOption[this.langCode].forEach(item =>{
            item.checked = true;
          })
         $event.closeMenu();
        }
      }
      if(data.checked){
        this.dataDisplay[data.attributeName] = { "label": data.label[this.langCode], "attributeName": data['attributeName'], "isMasked": false, "format": type["value"], "value": value };
      }
    }
  }

  $event.stopPropagation()

  if (Object.keys(this.dataDisplay).length >= 3) {
    this.downloadBtnDisabled = false
  } else {
    this.downloadBtnDisabled = true
  }

  if (!data.checked && typeof type === "string") {
    if (data.formatRequired) {
      let formatOptions = data['formatOption'][this.langCode].map(eachItem => {
        return { ...eachItem }
      })
      this.selectedOprionsFormOptions[data['attributeName']] = formatOptions;
    }
  }

  let row = "";
  let rowImage = ""

  for (let key of this.valuesSelected) {
    if(this.dataDisplay[key]){
      if (key === "photo") {
        rowImage = "<tr><td><img src=' " + this.dataDisplay[key].value + "' alt='' style='margin-left:48%;' width='70px' height='70px'/></td></tr>";
      } else {
        row = row + "<tr><td style='font-weight:600; font-family:Roboto;'>" + this.dataDisplay[key].label + ":</td><td style='font-weight:500; font-family:Roboto;'>" + this.dataDisplay[key].value + "</td></tr>";
      }
    }
  }
  this.buildHTML = `<html><head></head><body><table>` + rowImage + row + `</table></body></html>`;
  }

  downloadFile() {
    this.auditService.audit('RP-032', 'Download personalised card', 'RP-Download personalised card', 'Download personalised card', 'User clicks on "download" button on download personalised card page');
    this.convertpdf();
  }

  convertpdf() {
    this.isLoading = true;
    let self = this;
    const request = {
      "id": this.appConfigService.getConfig()["mosip.resident.download.personalized.card.id"],
      "version": this.appConfigService.getConfig()["resident.vid.version.new"],
      "requesttime": Utils.getCurrentDate(),
      "request": {
        "html": btoa(this.buildHTML),
        "attributes": Object.keys(this.dataDisplay)
      }
    };
    this.dataStorageService
      .convertpdf(request)
      .subscribe(data => {
        // var fileName = self.userInfo.fullName+".pdf";
        let contentDisposition = data.headers.get('content-disposition');
        this.eventId = data.headers.get("eventid")
        if (contentDisposition) {
          this.isLoading = false;
          try {
            var fileName = ""
            if (contentDisposition) {
              const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
              const matches = fileNameRegex.exec(contentDisposition);
              if (matches != null && matches[1]) {
                fileName = matches[1].replace(/['"]/g, '');
              }
            }
            saveAs(data.body, fileName);
            this.showMessage()
            this.router.navigate(['uinservices/dashboard']);
          } catch (error) {
            this.isLoading = false;
            console.log(error)
          }
        }

      },
        err => {
          console.error(err);
        });
  }



  conditionsForPersonalisedCard() {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '650px',
      data: {
        case: 'conditionsForPersonalisedCard',
        description: this.popupMessages.genericmessage.personalisedcardConditions,
        btnTxt: this.popupMessages.genericmessage.sendButton
      }
    });
    return dialogRef;
  }

  showMessage() {
    this.message = this.popupMessages.genericmessage.personalisedcardMessages.downloadedSuccessFully.replace("$eventId", this.eventId)
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '650px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.successLabel,
        clickHere: this.popupMessages.genericmessage.clickHere,
        clickHere2: this.popupMessages.genericmessage.clickHere2,
        eventId: this.eventId,
        passwordCombinationHeading: this.popupMessages.genericmessage.passwordCombinationHeading,
        passwordCombination: this.popupMessages.genericmessage.passwordCombination,
        trackStatusText:this.popupMessages.genericmessage.trackStatusText,
        dearResident: this.popupMessages.genericmessage.dearResident,
        message: this.message,
        btnTxt: this.popupMessages.genericmessage.successButton
      }
    });
    return dialogRef;
  }

  showErrorPopup(message: string) {
    let errorCode = message[0]['errorCode']
    setTimeout(() => {
      this.dialog
      .open(DialogComponent, {
        width: '650px',
        data: {
          case: 'MESSAGE',
          title: this.popupMessages.genericmessage.errorLabel,
          message: this.popupMessages.serverErrors[errorCode],
          btnTxt: this.popupMessages.genericmessage.successButton
        },
        disableClose: true
      });
  },400)
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  onItemSelected(item: any) {
    this.router.navigate([item]);
  }
}

