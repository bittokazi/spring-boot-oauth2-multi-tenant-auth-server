import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import { MailTemplateHelper } from 'src/app/@base/shared/form-helpers/mail/MailFormHelper';
import { MailTemplate } from 'src/app/@base/shared/models/mail/MailTemplate';
import { MailService } from '../mail.service';
import { UpdateMailTemplateView } from './UpdateMailTemplateView';

@Component({
  selector: 'app-update-mail-template',
  templateUrl: './update-mail-template.component.html',
  styleUrls: ['./update-mail-template.component.css'],
})
export class UpdateMailTemplateComponent
  implements OnInit, UpdateMailTemplateView
{
  public form: any;
  public formLoading: Boolean = true;
  public quillEditor: any;
  public labels: String[] = [];

  constructor(
    private mailService: MailService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.mailService.updateMailTemplateView = this;
  }

  ngOnInit(): void {
    this.mailService.getMailTemplate(this.route.snapshot.params['id']);
  }

  onMailTemplateFetchSuccess(mailTemplate: MailTemplate) {
    this.labels = mailTemplate.labels;
    this.form = MailTemplateHelper(mailTemplate);
    this.formLoading = false;
  }

  onSubmit() {
    this.mailService.updateMailTemplate(this.form.value);
  }

  onMailTemplateFetchError(error: any) {
    throw new Error('Method not implemented.');
  }

  @SweetSuccessDialog({
    title: 'Mail Template',
    text: 'Mail Template Update Successfull',
  })
  onMailTemplateUpdateSuccess() {
    this.router.navigate(['/dashboard/settings/mail/templates']);
  }

  onMailTemplateUpdateError() {
    throw new Error('Method not implemented.');
  }

  onLoadComplete() {
    this.formLoading = false;
  }
}
