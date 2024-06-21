import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { ResetPasswordService } from './reset-password.service';
import { ResetPasswordView } from './ResetPasswordView';
import { JqueryValidate } from './../../../@base/shared/js/JqueryValidate';
import { DashboardApp } from './../../../@base/shared/js/DashboardApp';
import { DashboardAppMenu } from './../../../@base/shared/js/DashboardAppMenu';
import { DashboardCustom } from './../../../@base/shared/js/DashboardCustom';
import { AuthLogin } from './../../../@base/shared/js/AuthLogin';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent implements OnInit, ResetPasswordView {
  public form: any;
  public checkingToken: Boolean = true;
  public validToken: Boolean = false;
  public loading: Boolean = true;
  public passwordDoNotMatch: Boolean = false;

  constructor(
    public resetPasswordService: ResetPasswordService,
    private route: ActivatedRoute,
    private router: Router,
    private sweetAlartService: SweetAlartService
  ) {
    this.resetPasswordService.resetPasswordView = this;
    this.form = new FormGroup({
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(
          /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/
        ),
      ]),
      confirmpassword: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
    });
  }

  ngOnInit(): void {
    this.resetPasswordService.checkTokenValidity(
      this.route.snapshot.params['token']
    );
  }

  onSubmit() {
    this.passwordDoNotMatch = false;
    if (this.form.value.password != this.form.value.confirmpassword) {
      this.passwordDoNotMatch = true;
      return;
    }
    this.loading = true;
    this.resetPasswordService.resetPasswordRequest(
      this.route.snapshot.params['token'],
      this.form.value
    );
  }

  onResetPasswordToken(response: any) {
    if (response.result == 'found') {
      this.validToken = true;
    } else {
      this.sweetAlartService.errorDialog(
        'Reset Password',
        'Invalid/Expired Password Reset Token.'
      );
    }
    setTimeout(() => {
      JqueryValidate();
      DashboardAppMenu();
      DashboardApp();
      AuthLogin();
      DashboardCustom();
    }, 200);
  }

  onResetPassword(response: any) {
    if (response.result == 'notMatch') {
      this.passwordDoNotMatch = true;
    } else if (response.result == 'notFound') {
      this.validToken = false;
      this.sweetAlartService.errorDialog(
        'Reset Password',
        'Invalid/Expired Password Reset Token.'
      );
    } else if (response.result == 'sameToPrevious') {
      this.sweetAlartService.errorDialog(
        'Reset Password',
        'Your password can not be same as previous one.'
      );
    } else if (response.result == 'success') {
      this.sweetAlartService.successDialog(
        'Reset Password',
        'Password Reset Successfull.'
      );
      this.router.navigate(['/login']);
    }
  }

  onLoadComplete() {
    this.loading = false;
    this.checkingToken = false;
  }
}
