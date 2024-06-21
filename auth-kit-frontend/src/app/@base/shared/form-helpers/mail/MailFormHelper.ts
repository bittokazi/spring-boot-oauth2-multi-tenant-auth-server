import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MailTemplate } from '../../models/mail/MailTemplate';

export function MailFormHelper(config) {
  return new FormGroup({
    server: new FormControl(config.server, [Validators.required]),
    port: new FormControl(config.port, [Validators.required]),
    senderName: new FormControl(config.senderName, [Validators.required]),
    senderAddress: new FormControl(config.senderAddress, [Validators.required]),
    senderAccount: new FormControl(config.senderAccount, [Validators.required]),
    password: new FormControl('', [Validators.required]),
  });
}

export function MailTemplateHelper(mailTemplate: MailTemplate) {
  return new FormGroup({
    subject: new FormControl(mailTemplate.subject, [Validators.required]),
    body: new FormControl(mailTemplate.body, [Validators.required]),
    type: new FormControl(mailTemplate.type, [Validators.required]),
  });
}
