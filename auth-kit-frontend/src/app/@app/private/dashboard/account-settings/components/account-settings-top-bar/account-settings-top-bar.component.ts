import { Component, Input, OnInit } from '@angular/core';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';

@Component({
  selector: 'app-account-settings-top-bar',
  templateUrl: './account-settings-top-bar.component.html',
  styleUrls: ['./account-settings-top-bar.component.css'],
})
export class AccountSettingsTopBarComponent implements OnInit {
  @Input('activeTab')
  public activeTab: String = '';

  constructor() {}

  @FeatherIconSetterInjector({
    delay: 20,
  })
  ngOnInit(): void {}
}
