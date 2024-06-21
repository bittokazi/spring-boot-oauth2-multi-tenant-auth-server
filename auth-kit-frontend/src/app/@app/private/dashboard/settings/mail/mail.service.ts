import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MailConfig } from 'src/app/@base/shared/models/mail/MailConfig';
import { MailConfigTestPayload } from 'src/app/@base/shared/models/mail/MailConfigTestPayload';
import { MailTemplate } from 'src/app/@base/shared/models/mail/MailTemplate';
import { environment } from 'src/environments/environment';
import { ListMailTemplateView } from './list-mail-template/ListMailTemplateView';
import { MailConfigView } from './mail-config/MailConfigView';
import { UpdateMailTemplateView } from './update-mail-template/UpdateMailTemplateView';

@Injectable({
  providedIn: 'root',
})
export class MailService {
  public mailConfigView: MailConfigView;
  public listMailTemplateView: ListMailTemplateView;
  public updateMailTemplateView: UpdateMailTemplateView;

  constructor(private http: HttpClient) {}

  formatMailConfig(data) {
    if (data.length > 0) {
      return data[0];
    }
    return {
      server: '',
      port: '',
      senderName: '',
      senderAddress: '',
      senderAccount: '',
    };
  }

  getMailConfig() {
    this.http
      .get<MailConfigView[]>(`${environment.baseUrl}/api/mail`)
      .toPromise()
      .then((response: any) =>
        this.mailConfigView.onMailConfigFetchSuccess(response)
      )
      .catch((error) => this.mailConfigView.onMailConfigFetchError(error))
      .finally(() => this.mailConfigView.onLoadComplete());
  }

  testMailConfig(mailConfigTestPayload: MailConfigTestPayload) {
    this.http
      .post(
        `${environment.baseUrl}/api/mail/config/test`,
        mailConfigTestPayload
      )
      .toPromise()
      .then((response: any) => this.mailConfigView.onMailTest(response))
      .finally(() => this.mailConfigView.onLoadComplete());
  }

  updateMailConfig(mailConfig: MailConfig) {
    this.http
      .put(`${environment.baseUrl}/api/mail/config/update`, mailConfig)
      .toPromise()
      .then(() => this.mailConfigView.onMailConfigUpdateSuccess())
      .catch((error) => this.mailConfigView.onMailConfigUpdateError(error))
      .finally(() => this.mailConfigView.onLoadComplete());
  }

  getMailTemplateList() {
    this.http
      .get<String[]>(`${environment.baseUrl}/api/mail/templates`)
      .toPromise()
      .then((response: any) =>
        this.listMailTemplateView.onMailTemplateListFetchSuccess(response)
      )
      .catch((error) =>
        this.listMailTemplateView.onMailTemplateListFetchError(error)
      )
      .finally(() => this.listMailTemplateView.onLoadComplete());
  }

  getMailTemplate(type: String) {
    this.http
      .get<MailTemplate>(
        `${environment.baseUrl}/api/mail/templates/type/${type}`
      )
      .toPromise()
      .then((response: any) =>
        this.updateMailTemplateView.onMailTemplateFetchSuccess(response)
      )
      .catch((error) =>
        this.updateMailTemplateView.onMailTemplateFetchError(error)
      )
      .finally(() => this.updateMailTemplateView.onLoadComplete());
  }

  updateMailTemplate(mailTemplate: MailTemplate) {
    this.http
      .put<MailTemplate>(
        `${environment.baseUrl}/api/mail/templates/update`,
        mailTemplate
      )
      .toPromise()
      .then((response: any) =>
        this.updateMailTemplateView.onMailTemplateUpdateSuccess()
      )
      .catch((error) =>
        this.updateMailTemplateView.onMailTemplateUpdateError(error)
      )
      .finally(() => this.updateMailTemplateView.onLoadComplete());
  }
}
