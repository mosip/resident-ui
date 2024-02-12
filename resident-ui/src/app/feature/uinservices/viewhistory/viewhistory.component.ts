import { Component, OnInit, OnDestroy, ViewChild } from "@angular/core";
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from "@ngx-translate/core";
import { Subscription } from "rxjs";
import { Router } from "@angular/router";
import { AppConfigService } from 'src/app/app-config.service';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { MatDialog } from '@angular/material';
import { DateAdapter } from '@angular/material/core';
import { saveAs } from 'file-saver';
import { HeaderService } from 'src/app/core/services/header.service';
import { AuditService } from "src/app/core/services/audit.service";
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AutoLogoutService } from "src/app/core/services/auto-logout.service";
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: "app-viewhistory",
  templateUrl: "viewhistory.component.html",
  styleUrls: ["viewhistory.component.css"],
})
export class ViewhistoryComponent implements OnInit, OnDestroy {
  langJSON:any;
  popupMessages:any;
  subscriptions: Subscription[] = [];
  responselist: any;
  totalItems = 0;
  defaultPageSize = 10;
  pageSize = this.defaultPageSize;
  pageIndex = 0;
  pageSizeOptions: number[] = [5, 10, 15, 20];
  serviceTypeFilter:any;
  serviceTypeFilter2:any;
  statusTypeFilter:any;
  statusTypeFilter2:any;
  showFirstLastButtons:boolean = true;
  cols:number;
  today: Date = new Date();
  presentYear: any = new Date().getFullYear();
  startdate: Date = new Date(this.presentYear, 0, 1);
  selectedDate:any = new Date();
  toDateStartDate:any = this.startdate;
  searchText:string = "";
  serviceType:string = "";
  statusFilter:string = "";
  fromDate:string = this.startdate.getFullYear() + "-" + ("0" + (this.startdate.getMonth() + 1)).slice(-2) + "-" + ("0" + this.startdate.getDate()).slice(-2);
  toDate:string = this.today.getFullYear() + "-" + ("0" + (this.today.getMonth() + 1)).slice(-2) + "-" + ("0" + this.today.getDate()).slice(-2);
  controlTypes = ["searchText", "serviceType", "statusFilter", "fromDate", "toDate"];
  datas:{};
  isStatusAllValue:boolean = false;
  statusSelectedValue:string;
  isHistoryAllValue:boolean = false;
  historySelectedValue:string;
  @ViewChild('statusFilterSelectAll') statusFilterSelectAll: any;
  @ViewChild('serviceTypeSelectAll') serviceTypeSelectAll: any;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  searchParam:string;
  message2:any;
  langCode = localStorage.getItem("langCode");
  serviceHistorySelectedValue: string = "History type";
  statusHistorySelectedValue: string = "Status";
  isLoading:boolean = true;
  dataAvailable:boolean = false;

  constructor(private autoLogout: AutoLogoutService,private dialog: MatDialog, private appConfigService: AppConfigService, private dataStorageService: DataStorageService, private translateService: TranslateService, private router: Router,private dateAdapter: DateAdapter<Date>, public headerService: HeaderService,private auditService: AuditService, private breakpointObserver: BreakpointObserver) {
    this.dateAdapter.setLocale('en-GB');
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
        }
        if (result.breakpoints[Breakpoints.Small]) {
          this.cols = 2;
        }
        if (result.breakpoints[Breakpoints.Medium]) {
          this.cols = 4;
        }
        if (result.breakpoints[Breakpoints.Large]) {
          this.cols = 6;
        }
        if (result.breakpoints[Breakpoints.XLarge]) {
          this.cols = 6;
        }
      }
    });
  }

  async ngOnInit() {
    this.translateService.use(localStorage.getItem("langCode"));

    this.translateService
      .getTranslation(localStorage.getItem("langCode"))
      .subscribe(response => {
        this.langJSON = response;
        this.popupMessages = response;
      });

    this.getServiceHistory("","","");
    this.captureValue("","ALL","", "")

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

  getServiceHistory(pageEvent:any, filters:any, actionTriggered:string){
    this.dataStorageService
      .getServiceHistory(pageEvent, filters,this.pageSize)
      .subscribe((response) => {
        if(response["response"]){
          this.isLoading = false;
          this.responselist = response["response"]["data"];
          this.totalItems = response["response"]["totalItems"];
          this.serviceTypeFilter = this.appConfigService.getConfig()["resident.view.history.serviceType.filters"].split(',');
          this.serviceTypeFilter2 = this.appConfigService.getConfig()["resident.view.history.serviceType.filters"].split(',');
          this.statusTypeFilter = this.appConfigService.getConfig()["resident.view.history.status.filters"].split(',');
          this.statusTypeFilter2 = this.appConfigService.getConfig()["resident.view.history.status.filters"].split(',');
          this.pageSize = response["response"]['pageSize']
          this.parsedrodowndata();
          if (this.responselist.length) {
            this.dataAvailable = false;
          } else {
            this.dataAvailable = true;
          }
        } else {
          this.isLoading = false;
          this.showErrorPopup(response["errors"])
        }
      });
  }

  parsedrodowndata() {
    let serviceTypeFilter = this.serviceTypeFilter;
    this.serviceTypeFilter = [];
    let statusTypeFilter = this.statusTypeFilter;
    this.statusTypeFilter = [];
    serviceTypeFilter.forEach((element) => {
      if (this.langJSON.viewhistory.serviceTypeFilter[element]) {
        this.serviceTypeFilter.push({ "label": this.langJSON.viewhistory.serviceTypeFilter[element], "value": element });
      }
    });
    statusTypeFilter.forEach((element) => {
      if (this.langJSON.viewhistory.statusTypeFilter[element]) {
        this.statusTypeFilter.push({ "label": this.langJSON.viewhistory.statusTypeFilter[element], "value": element });
      }
    });
  }

  tosslePerOne(event:string, isStatusAllValue:boolean, formControlName:string){
    if (isStatusAllValue) {
      this[formControlName] = this.statusTypeFilter2.join(",");
      this.statusHistorySelectedValue = event;
      this.statusTypeFilter = this.statusTypeFilter.map(eachServiceType => {
        eachServiceType.label.checked = true;
        return eachServiceType
      })
    } else {
      this[formControlName] = "";
      this.statusHistorySelectedValue = 'Status';
      this.statusTypeFilter = this.statusTypeFilter.map(eachServiceType => {
        eachServiceType.label.checked = false;
        return eachServiceType
      })
    }
  }

  selectingStatusOneValue(event:string,formControlName:string){
    let count = 0;
    this.statusTypeFilter.forEach(eachServiceType => {
      if (eachServiceType.value === "all") {
        eachServiceType.label.checked = false;
      } else if (eachServiceType.value === event) {
        eachServiceType.label.checked = !eachServiceType.label.checked;
      }

      if(eachServiceType.label.checked){
        count += 1;
        this[formControlName] += eachServiceType.value + ",";
        this.statusHistorySelectedValue += eachServiceType.label.statusType + ","
      }
    
    });

    if(!this.statusHistorySelectedValue){
      this.statusHistorySelectedValue = "Status";
    }else if(this.statusHistorySelectedValue.length > 26){
      this.statusHistorySelectedValue = this.statusHistorySelectedValue.substring(0,24) + "...";
    }else{
      this.statusHistorySelectedValue = this.statusHistorySelectedValue.replace(/,\s*$/, "");
    }
    
    if(count === 3){
      this.isStatusAllValue = !this.isStatusAllValue;
      this.tosslePerOne('All', this.isStatusAllValue, formControlName);
    }

  }

  historyTosslePerOne(event:string, isHistoryAllValue: boolean, formControlName: string) {
    if (isHistoryAllValue) {
      this[formControlName] = this.serviceTypeFilter2.join(",");
      this.serviceHistorySelectedValue = event;
      this.serviceTypeFilter = this.serviceTypeFilter.map(eachServiceType => {
        eachServiceType.label.checked = true;
        return eachServiceType
      })
    } else {
      this[formControlName] = "";
      this.serviceHistorySelectedValue = 'History Type';
      this.serviceTypeFilter = this.serviceTypeFilter.map(eachServiceType => {
        eachServiceType.label.checked = false;
        return eachServiceType
      })
    }
  }

  selectingHistoryOneValues(event:string,formControlName:string){
    let count = 0;
    this.serviceTypeFilter.forEach(eachServiceType => {
      if (eachServiceType.value === "ALL") {
        eachServiceType.label.checked = false;
      } else if (eachServiceType.value === event) {
        eachServiceType.label.checked = !eachServiceType.label.checked;
      }

      if(eachServiceType.label.checked){
        count += 1
        this[formControlName] += eachServiceType.value + ",";
        this.serviceHistorySelectedValue += eachServiceType.label.serviceType + ","
      }
    });
  
    if(!this.serviceHistorySelectedValue){
      this.serviceHistorySelectedValue = "History type";
    }else if(this.serviceHistorySelectedValue.length > 26){
      this.serviceHistorySelectedValue = this.serviceHistorySelectedValue.substring(0,24) + "...";
    }else{
      this.serviceHistorySelectedValue = this.serviceHistorySelectedValue.replace(/,\s*$/, "");
    }

    if(count === 5){
      this.isHistoryAllValue = !this.isHistoryAllValue;
      this.historyTosslePerOne("All", this.isHistoryAllValue, formControlName);
    }
  }

  captureValue(event: any,selectedValue:any, formControlName: string, controlType: string) {
    this.selectedDate = this.today;
    if (controlType === "dropdown") {
      if (selectedValue === "ALL" || selectedValue === "all") {
        if (formControlName === "serviceType") {
          this.isHistoryAllValue = !this.isHistoryAllValue;
          this.historyTosslePerOne(selectedValue, this.isHistoryAllValue, formControlName);
        } else {
          this.isStatusAllValue = !this.isStatusAllValue;
          this.tosslePerOne(selectedValue,this.isStatusAllValue,formControlName);
        }
      } else {
        if (formControlName === "serviceType") {
          this.serviceType = "";
          this.isHistoryAllValue = false;
          this.serviceHistorySelectedValue = "";
          this.selectingHistoryOneValues(selectedValue,formControlName)
        } else {
          this.statusFilter = "";
          this.isStatusAllValue = false;
          this.statusHistorySelectedValue = "";
          this.selectingStatusOneValue(selectedValue,formControlName)

        }
      }
    } else if (controlType === "datepicker") {
      let dateFormat = new Date(event.target.value);
      this.toDateStartDate = formControlName === "fromDate" ? dateFormat : "";
      let formattedDate = dateFormat.getFullYear() + "-" + ("0" + (dateFormat.getMonth() + 1)).slice(-2) + "-" + ("0" + dateFormat.getDate()).slice(-2);
      this[formControlName] = formattedDate;

    }else{
      if(event.target){
      this[formControlName] = event.target.value;
      }
    }
    if(formControlName === "serviceType"){
      this.auditService.audit('RP-009', 'View history', 'RP-View history', 'View history', 'User chooses the "history filter" from the drop-down');
      this.serviceType = this.serviceType.replace(/ALL,/ig, '').replace(/,\s*$/, "");
    }else if(formControlName === "statusFilter"){
      this.auditService.audit('RP-010', 'View history', 'RP-View history', 'View history', 'User chooses the "status filter" from the drop-down');
      this.statusFilter = this.statusFilter.replace(/ALL,/ig, '').replace(/,\s*$/, "");
    }
   
    if(event){
      event.stopPropagation()
    }
  }

  pinData(data:any){
    this.auditService.audit('RP-006', 'View history', 'RP-View history', 'View history', 'User clicks on "Pin to top"');
    this.dataStorageService
      .pinData(data.eventId)
      .subscribe((response) => {
        this.getServiceHistory("","","");
    });
  }

  unpinData(data:any){
    this.dataStorageService
      .unpinData(data.eventId)
      .subscribe((response) => {
        this.getServiceHistory("", "", "");
      });
  }

  viewDetails(data: any) {
    this.auditService.audit('RP-008', 'View history', 'RP-View history', 'View history', 'User clicks on "View Details"');
    this.router.navigateByUrl(`uinservices/trackservicerequest?source=ViewMyHistory&eid=` + data.eventId);
  }

  reportDetails(data: any) {
    this.auditService.audit('RP-007', 'View history', 'RP-View history', 'View history', 'User clicks on "Report an issue"');
    this.router.navigateByUrl(`uinservices/grievanceRedressal?source1=viewMyHistory&eid=` + data.eventId);
  }

  search() {
    let searchParam = "",
      self = this;
    this.controlTypes.forEach(controlType => {
      if (self[controlType]) {
        if (searchParam) {
          searchParam = searchParam + "&" + controlType + "=" + self[controlType];
        } else {
          searchParam = controlType + "=" + self[controlType];
        }
      }
    });
    this.getServiceHistory("", searchParam, "search");
    this.auditService.audit('RP-004', 'View history', 'RP-View history', 'View history', 'User clicks on "Go" button for applying "the chosen filter"');
    this.paginator.pageIndex = 0;
  }

  capturePageValue(pageEvent: any) {
    let searchParam = "",
      self = this;
    this.controlTypes.forEach(controlType => {
      if (self[controlType]) {
        if (searchParam) {
          searchParam = searchParam + "&" + controlType + "=" + self[controlType];
        } else {
          searchParam = controlType + "=" + self[controlType];
        }
      }
    });

    this.getServiceHistory(pageEvent, searchParam, "");
  }

  downloadServiceHistory() {
    this.isLoading = true;
    this.auditService.audit('RP-005', 'View history', 'RP-View history', 'View history', 'User clicks on "download" button');
    let searchParam = "", self = this;
    this.controlTypes.forEach(controlType => {

      if (self[controlType]) {
        if (searchParam) {
          searchParam = searchParam + "&" + controlType + "=" + self[controlType];
        } else {
          searchParam = controlType + "=" + self[controlType];
        }
      }
    });
    this.dataStorageService
      .downloadServiceHistory(searchParam)
      .subscribe(data => {
        // var fileName = "viewhistory.pdf";
        var fileName = ""
        const contentDisposition = data.headers.get('content-disposition');
        if (contentDisposition) {
          this.isLoading = false;
          const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
          const matches = fileNameRegex.exec(contentDisposition);
          if (matches != null && matches[1]) {
            fileName = matches[1].replace(/['"]/g, '');
          }
          saveAs(data.body, fileName);
        }else{
          this.isLoading = false;
        }
        
      },
        err => {
          console.error(err);
        });
  }

  showMessage(message: string) {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '650px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.successLabel,
        message: message,
        btnTxt: this.popupMessages.genericmessage.successButton
      }
    });
    return dialogRef;
  }

  showErrorPopup(message: string) {
    let errorCode = message[0]['errorCode']
    console.log(errorCode)
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
    }, 400)
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  onItemSelected(item: any) {
    this.router.navigate([item]);
  }
}
