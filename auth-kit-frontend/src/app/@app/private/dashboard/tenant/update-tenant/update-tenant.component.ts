import { Component, OnInit } from '@angular/core';
import { TenantService } from '../tenant.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';

@Component({
  selector: 'app-update-tenant',
  templateUrl: './update-tenant.component.html',
  styleUrls: ['./update-tenant.component.css'],
})
export class UpdateTenantComponent implements OnInit {
  public form: any;
  public customErrors: any;
  public loading: Boolean = true;

  constructor(
    private tenantService: TenantService,
    private router: Router,
    private sas: SweetAlartService,
    private route: ActivatedRoute
  ) {
    this.form = tenantService.generateForm(null);
    this.customErrors = tenantService.generateError();
  }

  ngOnInit(): void {
    this.tenantService
      .getTenant(this.route.snapshot.params.id)
      .then((res) => {
        this.form = this.tenantService.generateForm(res);
      })
      .catch((e) => {
        this.sas.successDialog('Error', 'Error');
      })
      .finally(() => {
        this.loading = false;
      });
  }

  onSubmit(form: FormGroup) {
    this.loading = true;
    this.tenantService
      .updateTenant(this.form.value)
      .then((res) => {
        this.sas.successDialog('Success', 'Updated');
        this.router.navigate(['/dashboard/tenants']);
      })
      .catch((e) => {
        this.sas.successDialog('Error', 'Error');
      })
      .finally(() => {
        this.loading = false;
      });
  }
}
