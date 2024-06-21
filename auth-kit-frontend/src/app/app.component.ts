import { Component, OnInit } from '@angular/core';
import { TenantService } from './@app/private/dashboard/tenant/tenant.service';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'SSO Server';
  loading = true;
  accessDenied = false;

  constructor(
    private tenantService: TenantService,
    private titleService: Title
  ) { }

  ngOnInit(): void {
    this.tenantService
      .info()
      .then((info) => {
        this.titleService.setTitle(info.name);
        if (!info.cpanel && !info.enabledConfigPanel) this.accessDenied = true;
        this.tenantService.tenantInfo = info;
      })
      .catch(() => { })
      .finally(() => {
        this.loading = false;
        document
          .getElementById('master-loader-wrapper')
          .classList.add('master-loader-hidden');
      });
  }
}
