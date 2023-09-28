import { Component, HostListener } from '@angular/core';
import { AppConfigService } from './app-config.service';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { Subscription } from 'rxjs';
import { Event as NavigationEvent, Router, NavigationStart } from '@angular/router';
import { filter } from 'rxjs/operators';
import { LogoutService } from 'src/app/core/services/logout.service';
import { AuditService } from 'src/app/core/services/audit.service';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { MatKeyboardService } from 'ngx7-material-keyboard';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'resident-ui';
  subscriptions: Subscription[] = [];
  previousUrl: string;
  primaryLangCode: string = localStorage.getItem("langCode");
  sitealignment;

  constructor(
    private appConfigService: AppConfigService,
    private autoLogout: AutoLogoutService, 
    private router: Router,
    private logoutService: LogoutService,
    private auditService: AuditService,
    private keyboardService: MatKeyboardService,
    private dataStorageService: DataStorageService
  ) {
    this.appConfigService.getConfig();
    if (this.primaryLangCode === "ara") {
      localStorage.setItem('direction','rtl')
    }else{
      localStorage.setItem('direction','ltr')
    }
    this.sitealignment = localStorage.getItem('direction');
    document.body.dir = this.sitealignment;
  }
  
  ngOnInit() { 
    this.router.routeReuseStrategy.shouldReuseRoute = function(){
      return false;
    };

    this.dataStorageService.isAuthenticated().subscribe((response) => {
      if(response){
        if(response["response"]){
          if (window.location.href.includes('uinservices')) {
          }else{
            this.router.navigate(['uinservices/dashboard']); 
          }
        }else{
          if(window.location.href.includes('error=invalid_transaction')){
            this.router.navigate(['error']);
          }else{
            if (window.location.href.includes('uinservices')) {
              this.router.navigate(['dashboard']);
            }
          };
        }
      }else{
        this.router.navigate(['dashboard']);
      }
    });
    if(window.location.href.includes('error=invalid_transaction')){
      this.router.navigate(['error']);
    }
    
    if(!localStorage.getItem("langCode")){
      localStorage.setItem("langCode", "eng");
    }
    this.subscriptions.push(this.autoLogout.currentMessageAutoLogout.subscribe(() => {}));
    this.autoLogout.changeMessage({ timerFired: false });
    this.routerType();
  }

  routerType() {
    this.subscriptions.push(
      this.router.events
        .pipe(filter((event: NavigationEvent) => event instanceof NavigationStart))
        .subscribe((event: NavigationStart) => {
          if (event.restoredState) {
           // this.configService.navigationType = 'popstate';
            //this.preventBack();
          }
          if (this.keyboardService.isOpened) {
            this.keyboardService.dismiss();
          }
        })
    );
  }

  preventBack() {
    window.history.forward();
    window.onunload = function() {
      null;
    };
  }

  @HostListener('document:keydown.escape', ['$event']) onKeydownHandler(event: KeyboardEvent) {
    if (this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
    }
  }

  @HostListener('mouseover')
  @HostListener('document:mousemove', ['$event'])
  @HostListener('keypress')
  @HostListener('click')
  @HostListener('document:keypress', ['$event'])
  onMouseClick() {
    this.autoLogout.setisActive(true);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(subscription => {
      subscription.unsubscribe()});
  }
}
