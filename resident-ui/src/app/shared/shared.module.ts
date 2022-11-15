import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { DialogComponent } from './dialog/dialog.component';
import { HeaderComponent } from './header/header.component';
import { CaptchaComponent } from './captcha/captcha.component';
import { I18nModule } from 'src/app/i18n.module';
import { RecaptchaModule, RECAPTCHA_LANGUAGE } from "ng-recaptcha";
import { SanitizePipe } from './pipes/sanitize.pipe';

@NgModule({
  imports: [CommonModule, MaterialModule, ReactiveFormsModule, FormsModule, I18nModule, RecaptchaModule],
  declarations: [DialogComponent, HeaderComponent, CaptchaComponent, SanitizePipe],
  exports: [DialogComponent, MaterialModule, HeaderComponent, CaptchaComponent, SanitizePipe],
  entryComponents: [DialogComponent],
  providers: []
})
export class SharedModule {}