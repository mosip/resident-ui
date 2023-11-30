import { Injectable } from '@angular/core';
import { v4 as uuid } from 'uuid';
import { AppConfigService } from 'src/app/app-config.service';
import defaultJson from "src/assets/i18n/default.json";

@Injectable()
export class LoginRedirectService {

  constructor(private appService: AppConfigService) { }

  redirect(url: string) {
    const stateParam = uuid();
    let constructurl = url;
    if(url.split("#")[1] === "/dashboard"){
      constructurl = url.replace("/dashboard", "/uinservices/dashboard");
    }
    window.location.href = `${this.appService.getConfig().baseUrl}${this.appService.getConfig().login}v2/` + btoa(constructurl)+"?state="+stateParam+"&ui_locales="+defaultJson["keyboardMapping"][localStorage.getItem("langCode")];
    localStorage.setItem("redirectURL", constructurl)
  }

}