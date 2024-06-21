import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { environment } from 'src/environments/environment';
import { ForgetPasswordService } from './forget-password.service';
import { ForgetPasswordView } from './ForgetPasswordView';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.css'],
})
export class ForgetPasswordComponent implements OnInit, ForgetPasswordView {
  public form: any;
  public loading: Boolean = false;
  public emailNotFoundError: Boolean = false;
  public resetPasswordSuccess: Boolean = false;
  public captchaSuccess: Boolean = false;
  public captchaString: String = '';

  constructor(
    public forgetPasswordService: ForgetPasswordService,
    public sweetAlartService: SweetAlartService
  ) {
    this.forgetPasswordService.forgetPasswordView = this;
    this.form = new FormGroup({
      email: new FormControl('', [
        Validators.required,
        Validators.pattern(
          /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        ),
      ]),
    });
  }

  ngOnInit(): void {
    setTimeout(() => {
      this.loadCaptcha();
    }, 1000);
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

  onSubmit() {
    this.loading = true;
    this.emailNotFoundError = false;
    this.forgetPasswordService.forgetPasswordRequest(
      this.form.value,
      this.captchaString
    );
  }

  onForgetPassword(response: any) {
    if (response.result == 'success') {
      this.sweetAlartService.successDialog(
        'Forget Password',
        'Reset password email has been sent.'
      );
      this.resetPasswordSuccess = true;
    } else {
      this.emailNotFoundError = true;
    }
  }

  onError(error: any) {
    if (error.status == 403 && error.error?.message == 'INVALID_CAPTCHA') {
      window['grecaptcha'].reset();
      this.sweetAlartService.errorDialog('Error', 'Invalid Captcha');
    }
  }

  onLoadComplete() {
    this.loading = false;
  }
}
