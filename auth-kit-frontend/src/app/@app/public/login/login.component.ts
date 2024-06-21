import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthHolder } from 'src/app/@base/authentication/AuthHolder';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { LoginResponse } from 'src/app/@base/shared/models/auth/LoginResponse';
import { environment } from 'src/environments/environment';
import { LoginService } from './login.service';
import { LoginView } from './LoginView';
import { TenantService } from '../../private/dashboard/tenant/tenant.service';
import info from './../../../../../../info.json';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit, LoginView {
  public form: FormGroup;
  public loggingIn: Boolean = true;
  public loginCredentialsError: Boolean = false;
  public captchaSuccess: Boolean = false;
  public captchaString: String = '';
  public name: String = '';
  public version = info.version;

  constructor(
    public loginService: LoginService,
    public router: Router,
    public sweetAlartService: SweetAlartService,
    public tenantService: TenantService
  ) {
    this.loginService.loginView = this;
    this.form = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      rememberMe: new FormControl(false),
      deviceId: new FormControl(loginService.getInstanceId()),
    });
  }

  ngOnInit(): void {
    this.name = this.tenantService.tenantInfo.name;
    this.loginService.userAuthCheck();
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

  btnLogin() {
    // this.loggingIn = true;
    // this.loginCredentialsError = false;
    // this.loginService.login(this.form.value, this.captchaString);
    window.location.href = '/oauth2/login';
  }

  loginSuccess(loginResponse: LoginResponse) {
    AuthHolder.setToken(loginResponse);
    // document.cookie = `token=${loginResponse.access_token}; Path=/;`;
    this.router.navigate(['/dashboard']);
  }

  loginError(error: any) {
    if (error.status == 401) {
      this.loginCredentialsError = true;
    } else if (
      error.status == 403 &&
      error.error?.message == 'INVALID_CAPTCHA'
    ) {
      window['grecaptcha'].reset();
      this.sweetAlartService.errorDialog('Error', 'Invalid Captcha');
    }
    this.loggingIn = false;
  }

  userAuthCheckSuccess() {
    this.router.navigate(['/dashboard']);
  }

  userAuthCheckError() {
    this.loggingIn = false;
    setTimeout(() => {
      this.loadCaptcha();
    }, 1000);
  }
}
