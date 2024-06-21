import { Component, OnInit } from '@angular/core';
import {UpdateRoleView} from './UpdateRoleView';
import {Role} from '../../../../../@base/shared/models/role/Role';
import {ActivatedRoute, Router} from '@angular/router';
import {RoleService} from '../role.service';
import {RoleError, RoleFormHelper} from '../../../../../@base/shared/form-helpers/role/RoleFormHelper';
import FormErrorSetter from '../../../../../@base/shared/form-helpers/FormErrorHandler';
import {SweetSuccessDialog} from '../../../../../@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';

@Component({
  selector: 'app-update-role',
  templateUrl: './update-role.component.html',
  styleUrls: ['./update-role.component.css']
})
export class UpdateRoleComponent implements OnInit, UpdateRoleView {

  public form: any;
  public roleId: number;
  public loading: Boolean = true;
  public customErrors: any;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private roleService: RoleService) {
    this.form = RoleFormHelper({});
    this.customErrors = RoleError();
    this.roleService.updateRoleView = this;
  }

  ngOnInit(): void {
    this.roleId = this.route.snapshot.params.id;
    this.roleService.getRole(this.roleId);
  }

  onSubmit(): void{
    this.roleService.updateRole(this.roleId, this.form.value);
  }

  onLoadComplete(): void {
    this.loading = false;
  }

  onRoleFetchError(error: any): void {
    console.log('Can not fetch role for update', error);
  }

  onRoleFetchSuccess(role: Role): void {
    this.form = RoleFormHelper(role);
  }

  onRoleUpdateError(error: any): void {
    if (error.status === 400) { FormErrorSetter(this.form, error.error); }
  }

  @SweetSuccessDialog({
    title: 'Role',
    text: 'Role Updated Successfully',
  })
  onRoleUpdateSuccess(): void {
    this.router.navigate(['/dashboard/roles']);
  }

}
