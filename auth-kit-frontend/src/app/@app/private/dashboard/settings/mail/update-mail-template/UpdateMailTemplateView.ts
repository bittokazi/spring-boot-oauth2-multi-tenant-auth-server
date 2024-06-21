import { MailTemplate } from 'src/app/@base/shared/models/mail/MailTemplate';

export interface UpdateMailTemplateView {
  onMailTemplateFetchSuccess(mailTemplate: MailTemplate);
  onMailTemplateFetchError(error: any);
  onMailTemplateUpdateSuccess();
  onMailTemplateUpdateError(error: any);
  onLoadComplete();
}
