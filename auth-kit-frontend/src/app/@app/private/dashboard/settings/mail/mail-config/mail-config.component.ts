import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import { MailFormHelper } from 'src/app/@base/shared/form-helpers/mail/MailFormHelper';
import { MailConfig } from 'src/app/@base/shared/models/mail/MailConfig';
import { MailService } from '../mail.service';
import { MailConfigView } from './MailConfigView';

@Component({
  selector: 'app-mail-config',
  templateUrl: './mail-config.component.html',
  styleUrls: ['./mail-config.component.css'],
})
export class MailConfigComponent implements OnInit, MailConfigView {
  public form: any;
  public loading: Boolean = true;
  public formLoading: Boolean = true;
  public testEmail: String = '';

  constructor(
    public mailService: MailService,
    private router: Router,
    private sweetAlertService: SweetAlartService
  ) {
    this.mailService.mailConfigView = this;
  }

  ngOnInit(): void {
    this.mailService.getMailConfig();
  }

  onSubmit() {
    this.mailService.updateMailConfig(this.form.value);
  }

  onSubmitTestMail() {
    if (this.testEmail == '') return;
    this.loading = true;
    this.mailService.testMailConfig({
      email: this.testEmail,
    });
  }

  onMailConfigFetchSuccess(mailConfig: MailConfig[]) {
    this.form = MailFormHelper(this.mailService.formatMailConfig(mailConfig));
    this.formLoading = false;
  }

  onMailConfigFetchError(error: any) {
    console.log('MailConfigComponent > onMailConfigFetchError', error);
  }

  @SweetSuccessDialog({
    title: 'Mail Config',
    text: 'Mail Config Update Successfull',
  })
  onMailConfigUpdateSuccess() {
    this.router.navigate(['/dashboard']);
  }
  onMailConfigUpdateError(error: any) {
    console.log('MailConfigComponent > onMailConfigUpdateError', error);
  }

  onMailTest(response: any) {
    if (response.success == 'ok') {
      this.sweetAlertService.successDialog(
        'Mail Config Test',
        'Configuration Successfull'
      );
    } else {
      this.sweetAlertService.errorDialog(
        'Mail Config Test',
        'Error Sending Mail'
      );
    }
  }

  onLoadComplete() {
    this.loading = false;
  }
}
