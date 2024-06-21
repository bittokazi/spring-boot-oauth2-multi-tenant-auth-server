import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';
import { SweetErrorDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetErrorDialog';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';
import {
  SectionTemplate,
  SectionTemplateDataSource,
} from 'src/app/@base/shared/models/section-template/SectionTemplate';
import { Section } from 'src/app/@base/shared/models/section/Section';
import { Website } from 'src/app/@base/shared/models/website/Website';
import { WebsiteRecords } from 'src/app/@base/shared/models/website/WebsiteRecords';

@Component({
  selector: 'app-default',
  templateUrl: './default.component.html',
  styleUrls: ['./default.component.css'],
})
export class DefaultComponent implements OnInit {
  public user: any = {};

  public websites: Website[] = [];

  public sectionTemplates: SectionTemplate[] = [];

  public sections: Section[] = [];

  constructor(private authService: AuthService) {
    this.user = this.authService.userInfo;
  }

  @FeatherIconSetterInjector()
  ngOnInit(): void {
    if (this.user.roles[0].name == 'ROLE_USER') {
    }
  }

  onWebsiteListFetchSuccess(result: WebsiteRecords) {
    this.websites = result.results;
  }

  @SweetErrorDialog({
    title: 'Error',
    text: 'Error Fetching website info',
  })
  onWebsiteListFetchError(error: any): void {}

  onSectionsFetchSuccess(sections: Section[]) {
    this.sections = sections;
  }

  onSectionsFetchError(error: any): void {
    throw new Error('Method not implemented.');
  }

  onSectionsSaveSuccess(sections: Section[]) {
    throw new Error('Method not implemented.');
  }

  onSectionsSaveError(error: any): void {
    throw new Error('Method not implemented.');
  }

  onSectionTemplatesFetchSuccess(sectionTemplate: SectionTemplate[]) {
    this.sectionTemplates = sectionTemplate;
  }

  onSectionTemplatesFetchError(error: any): void {
    throw new Error('Method not implemented.');
  }

  onSectionTemplatesSaveSuccess(sectionTemplate: SectionTemplate[]) {
    throw new Error('Method not implemented.');
  }

  onSectionTemplatesSaveError(error: any): void {
    throw new Error('Method not implemented.');
  }

  onSectionTemplatesFetchPropsSuccess(
    sectionTemplateDataSource: SectionTemplateDataSource
  ) {
    throw new Error('Method not implemented.');
  }

  onSectionTemplatesFetchPropsError(error: any): void {
    throw new Error('Method not implemented.');
  }

  onLoadComplete(): void {}
}
