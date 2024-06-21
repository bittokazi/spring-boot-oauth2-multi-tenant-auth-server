import { MailConfig } from 'src/app/@base/shared/models/mail/MailConfig';

export interface MailConfigView {
  onMailConfigFetchSuccess(mailConfig: MailConfig[]);
  onMailConfigFetchError(error: any);
  onMailConfigUpdateSuccess();
  onMailConfigUpdateError(error: any);
  onMailTest(response: any);
  onLoadComplete();
}
