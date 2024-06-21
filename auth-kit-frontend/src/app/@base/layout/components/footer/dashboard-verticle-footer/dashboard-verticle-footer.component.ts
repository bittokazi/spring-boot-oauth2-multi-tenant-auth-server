import { Component, OnInit } from '@angular/core';
import { TenantService } from 'src/app/@app/private/dashboard/tenant/tenant.service';
import { environment } from 'src/environments/environment';
import info from './../../../../../../../../info.json';

@Component({
  selector: 'app-dashboard-verticle-footer',
  templateUrl: './dashboard-verticle-footer.component.html',
  styleUrls: ['./dashboard-verticle-footer.component.css'],
})
export class DashboardVerticleFooterComponent implements OnInit {
  public domain = environment.http + environment.domain;
  public name: String = '';
  public version = info.version;

  constructor(public tetantService: TenantService) {}

  ngOnInit(): void {
    this.name = this.tetantService.tenantInfo.name;
  }
}
