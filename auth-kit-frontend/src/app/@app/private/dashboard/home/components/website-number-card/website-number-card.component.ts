import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  FeatherIconSetter,
  FeatherIconSetterInjector,
} from 'src/app/@base/shared/js/FeatherIconSetter';
import { Website } from 'src/app/@base/shared/models/website/Website';

@Component({
  selector: 'app-website-number-card',
  templateUrl: './website-number-card.component.html',
  styleUrls: ['./website-number-card.component.css'],
})
export class WebsiteNumberCardComponent implements OnInit {
  @Input('websites')
  public websites: Website[] = [];

  constructor(private router: Router) {}

  ngOnInit(): void {}

  configureWebsite() {
    this.router.navigate(['/dashboard/my-websites/configure']);
  }
}
