import { Component, OnInit } from '@angular/core';
import { JqueryValidate } from './../../../../@base/shared/js/JqueryValidate';
import { DashboardApp } from './../../../../@base/shared/js/DashboardApp';
import { DashboardAppMenu } from './../../../../@base/shared/js/DashboardAppMenu';
import { DashboardCustom } from './../../../../@base/shared/js/DashboardCustom';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';
import { EmailVerifyService } from './email-verify.service';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { environment } from 'src/environments/environment';
import { EmailVerifyView } from '../components/interfaces/EmailVerifyView';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import { ActivatedRoute, Router } from '@angular/router';

let $ = window['$'];

@Component({
  selector: 'app-email-verify',
  templateUrl: './email-verify.component.html',
  styleUrls: ['./email-verify.component.css'],
})
export class EmailVerifyComponent implements OnInit, EmailVerifyView {
  public captchaSuccess: Boolean = false;
  public captchaString: String = '';
  public user: any = {};

  constructor(
    private authService: AuthService,
    private emailVerifyService: EmailVerifyService,
    public sweetAlartService: SweetAlartService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.emailVerifyService.emailVerifyView = this;
    this.user = this.authService.userInfo.user;
  }

  ngOnInit(): void {
    if (this.user.emailVerified) this.router.navigate(['/dashboard']);
    else {
      $('body').removeClass('vertical-layout');
      $('body').removeClass('vertical-menu-modern');
      $('body').removeClass('blank-page');
      $('body').removeClass('navbar-floating');
      $('body').removeClass('footer-static');
      $('body').addClass(
        'vertical-layout vertical-menu-modern blank-page navbar-floating footer-static'
      );
      JqueryValidate();
      DashboardAppMenu();
      DashboardApp();
      DashboardCustom();
      this.loadCaptcha();
    }
  }

  loadCaptcha() {
    if (window['grecaptcha'] && window['grecaptcha'].render) {
      let captchaCallback = (response) => {
        this.captchaCallback(response);
      };
      window['grecaptcha'].render('recaptcha', {
        sitekey: environment.captchaSiteKey,
        callback: captchaCallback,
      });
    } else {
      setTimeout(() => {
        this.loadCaptcha();
      }, 100);
    }
  }

  captchaCallback(response) {
    this.captchaString = response;
  }

  @SweetSuccessDialog({
    title: 'Email Verification',
    text: 'Sent verification successfully!',
  })
  sendValidationEmailSuccess(response: any): void {}

  sendValidationEmailError(error: any): void {
    if (error.status == 403 && error.error?.message == 'INVALID_CAPTCHA') {
      window['grecaptcha'].reset();
      this.sweetAlartService.errorDialog('Error', 'Invalid Captcha');
    }
  }

  verifyEmailSuccess(response: any): void {
    throw new Error('Method not implemented.');
  }

  verifyEmailError(error: any): void {
    throw new Error('Method not implemented.');
  }

  logout() {
    this.authService.logoutTasks();
  }

  sendEmail() {
    this.emailVerifyService.sendVerificationMail(
      {
        email: this.user.email,
      },
      this.captchaString
    );
  }
}
