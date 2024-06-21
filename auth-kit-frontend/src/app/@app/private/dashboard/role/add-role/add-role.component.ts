import { Component, OnInit } from '@angular/core';
import { Role } from '../../../../../@base/shared/models/role/Role';
import { RoleService } from '../role.service';
import { Router } from '@angular/router';
import {
  RoleError,
  RoleFormHelper,
} from '../../../../../@base/shared/form-helpers/role/RoleFormHelper';
import { AddRoleView } from './AddRoleView';
import FormErrorSetter from '../../../../../@base/shared/form-helpers/FormErrorHandler';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';

@Component({
  selector: 'app-add-role',
  templateUrl: './add-role.component.html',
  styleUrls: ['./add-role.component.css'],
})
export class AddRoleComponent implements OnInit, AddRoleView {
  public form: any;
  public customErrors: any;
  public roles: Role[] = [];
  public loading: Boolean = false;

  constructor(private roleService: RoleService, private router: Router) {
    this.form = RoleFormHelper({});
    this.customErrors = RoleError();
    this.roleService.addRoleView = this;
  }

  ngOnInit(): void {}

  onSubmit(): void {
    this.loading = true;
    this.roleService.addRole(this.form.value);
  }

  onAddRoleError(error: any): void {
    if (error.status === 400) {
      FormErrorSetter(this.form, error.error);
    }
    this.loading = false;
    console.log('AddRoleComponent > onAddRoleError', error);
  }

  @SweetSuccessDialog({
    title: 'Add Role',
    text: 'Role Added Successfull',
  })
  onAddRoleSuccess(role: Role): void {
    this.loading = false;
    this.router.navigate(['/dashboard/roles']);
  }
}
