import { Component, OnInit } from '@angular/core';
import { TenantService } from '../tenant.service';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { AuthHolder } from 'src/app/@base/authentication/AuthHolder';
import { Router } from '@angular/router';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';

@Component({
  selector: 'app-list-tenant',
  templateUrl: './list-tenant.component.html',
  styleUrls: ['./list-tenant.component.css'],
})
export class ListTenantComponent implements OnInit {
  public tenants: any[] = [];

  constructor(
    public tenantService: TenantService,
    private sas: SweetAlartService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.tenantService
      .getTenants()
      .then((res) => {
        this.tenants = res;
      })
      .catch((e) => {
        this.sas.successDialog('Error', 'Error');
      })
      .finally(() => {
        this.onLoad();
      });
  }

  switchTenant(tenant) {
    AuthHolder.setTenant(tenant);
    this.router.navigate(['/']);
  }

  @FeatherIconSetterInjector()
  onLoad() {}
}
