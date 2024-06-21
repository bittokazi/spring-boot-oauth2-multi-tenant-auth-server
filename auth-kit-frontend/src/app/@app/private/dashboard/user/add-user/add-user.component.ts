import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  AddUserError,
  AddUserFormHelper,
} from 'src/app/@base/shared/form-helpers/user/AddUserFormHelper';
import { Role } from 'src/app/@base/shared/models/role/Role';
import { User } from 'src/app/@base/shared/models/user/User';
import { RoleService } from '../../role/role.service';
import { UserService } from '../user.service';
import { AddUserView } from './AddUserView';
import { ListRoleView } from '../../role/list-role/ListRoleView';
import { RoleRecords } from '../../../../../@base/shared/models/role/RoleRecords';
import FormErrorSetter from 'src/app/@base/shared/form-helpers/FormErrorHandler';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css'],
})
export class AddUserComponent implements OnInit, ListRoleView, AddUserView {
  public form: any;
  public customErrors: any;
  public roles: Role[] = [];
  public loading: Boolean = true;

  constructor(
    private roleService: RoleService,
    private userService: UserService,
    private router: Router
  ) {
    this.form = AddUserFormHelper();
    this.customErrors = AddUserError();
    this.roleService.listRoleView = this;
    this.userService.addUserView = this;
  }

  ngOnInit(): void {
    this.roleService.getRoles();
  }

  onSubmit(form: FormGroup) {
    this.loading = true;
    this.roles.map((role: Role) => {
      if (role.id == this.form.value.roles[0].id) {
        this.form.controls.roles.controls[0].controls.name.patchValue(
          role.name
        );
      }
    });
    this.userService.addUser(form.value);
  }

  onFetchRoleListSuccess(roleRecords: RoleRecords): void {
    this.roles = roleRecords.roles;
    console.log(this.roles);
  }

  onFetchRoleListError(error: any): void {
    console.log('AddUserComponent > onFetchRoleListError', error);
  }

  @SweetSuccessDialog({
    title: 'Add User',
    text: 'User Added Successfull',
  })
  onAddUserSuccess(user: User) {
    this.router.navigate(['/dashboard/users']);
  }

  onAddUserError(error: any) {
    if (error.status == 400) FormErrorSetter(this.form, error.error);
    console.log('AddUserComponent > onAddUserError', error);
  }

  onLoadComplete() {
    this.loading = false;
  }
}
