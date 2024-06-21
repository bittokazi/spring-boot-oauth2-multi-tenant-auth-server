import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SectionTemplate } from 'src/app/@base/shared/models/section-template/SectionTemplate';
import { Section } from 'src/app/@base/shared/models/section/Section';

@Component({
  selector: 'app-resume-config-box',
  templateUrl: './resume-config-box.component.html',
  styleUrls: ['./resume-config-box.component.css'],
})
export class ResumeConfigBoxComponent implements OnInit {
  @Input('sectionTemplates')
  public sectionTemplates: SectionTemplate[] = [];

  @Input('sections')
  public sections: Section[] = [];

  constructor(private router: Router) {}

  ngOnInit(): void {}

  configureResume() {
    this.router.navigate(['/dashboard/resume/configure']);
  }
}
