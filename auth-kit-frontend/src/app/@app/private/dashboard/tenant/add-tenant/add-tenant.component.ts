import { Component, OnInit } from '@angular/core';
import { TenantService } from '../tenant.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';

@Component({
  selector: 'app-add-tenant',
  templateUrl: './add-tenant.component.html',
  styleUrls: ['./add-tenant.component.css'],
})
export class AddTenantComponent implements OnInit {
  public form: any;
  public customErrors: any;
  public loading: Boolean = true;

  constructor(
    private tenantService: TenantService,
    private router: Router,
    private sas: SweetAlartService
  ) {
    this.form = tenantService.generateForm(null);
    this.customErrors = tenantService.generateError();
  }

  ngOnInit(): void {
    this.loading = false;
  }

  onSubmit(form: FormGroup) {
    this.loading = true;
    this.tenantService
      .addTenant(this.form.value)
      .then((res) => {
        this.sas.successDialog('Success', 'Added');
        this.router.navigate(['/dashboard/tenants']);
      })
      .catch((e) => {
        this.sas.successDialog('Error', 'Error');
      })
      .finally(() => {
        this.onLoad();
      });
  }

  @FeatherIconSetterInjector()
  onLoad() {
    this.loading = false;
  }
}
