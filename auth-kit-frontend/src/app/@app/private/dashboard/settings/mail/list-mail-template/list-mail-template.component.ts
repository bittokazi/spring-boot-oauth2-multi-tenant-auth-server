import { Component, OnInit } from '@angular/core';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';
import { MailService } from '../mail.service';
import { ListMailTemplateView } from './ListMailTemplateView';

@Component({
  selector: 'app-list-mail-template',
  templateUrl: './list-mail-template.component.html',
  styleUrls: ['./list-mail-template.component.css'],
})
export class ListMailTemplateComponent implements OnInit, ListMailTemplateView {
  public mailTypes: String[] = [];
  public loading: Boolean = true;

  constructor(private mailService: MailService) {
    this.mailService.listMailTemplateView = this;
    this.mailService.getMailTemplateList();
  }

  ngOnInit(): void {}

  onMailTemplateListFetchSuccess(mailTypes: String[]) {
    this.mailTypes = mailTypes;
  }

  onMailTemplateListFetchError(error: any) {
    console.log(
      'ListMailTemplateComponent > onMailTemplateListFetchError',
      error
    );
  }

  @FeatherIconSetterInjector()
  onLoadComplete() {
    this.loading = false;
  }
}
