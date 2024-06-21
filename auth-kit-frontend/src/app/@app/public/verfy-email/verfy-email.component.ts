import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { JqueryValidate } from './../../../@base/shared/js/JqueryValidate';
import { DashboardApp } from './../../../@base/shared/js/DashboardApp';
import { DashboardAppMenu } from './../../../@base/shared/js/DashboardAppMenu';
import { DashboardCustom } from './../../../@base/shared/js/DashboardCustom';
import { EmailVerifyView } from '../../private/other/components/interfaces/EmailVerifyView';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import { SweetErrorDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetErrorDialog';
import { EmailVerifyService } from '../../private/other/email-verify/email-verify.service';

let $ = window['$'];

@Component({
  selector: 'app-verfy-email',
  templateUrl: './verfy-email.component.html',
  styleUrls: ['./verfy-email.component.css'],
})
export class VerfyEmailComponent implements OnInit, EmailVerifyView {
  public loading: Boolean = true;
  public success: Boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private emailVerifyService: EmailVerifyService
  ) {
    this.emailVerifyService.emailVerifyView = this;
  }

  ngOnInit(): void {
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
    this.emailVerifyService.verifyLink(this.route.snapshot.params['code']);
  }

  sendValidationEmailSuccess(response: any): void {
    throw new Error('Method not implemented.');
  }

  sendValidationEmailError(error: any): void {
    throw new Error('Method not implemented.');
  }

  @SweetSuccessDialog({
    title: 'Email Verification',
    text: 'Successfull!',
  })
  verifyEmailSuccess(response: any): void {
    this.router.navigate(['/dashboard']);
    this.loading = false;
    this.success = true;
  }

  @SweetErrorDialog({
    title: 'Email Verification',
    text: 'Error!',
  })
  verifyEmailError(error: any): void {
    this.loading = false;
    this.success = false;
  }
}
