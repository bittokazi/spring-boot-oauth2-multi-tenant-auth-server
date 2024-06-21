import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SweetErrorDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetErrorDialog';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import FormErrorSetter from 'src/app/@base/shared/form-helpers/FormErrorHandler';
import {
  AddUserError,
  AddUserFormHelper,
} from 'src/app/@base/shared/form-helpers/user/AddUserFormHelper';
import {
  UpdateUserFormHelper,
  UpdateUserPasswordFormHelper,
} from 'src/app/@base/shared/form-helpers/user/UpdateUserFormHelper';
import { Role } from 'src/app/@base/shared/models/role/Role';
import { RoleRecords } from 'src/app/@base/shared/models/role/RoleRecords';
import { User } from 'src/app/@base/shared/models/user/User';
import { ListRoleView } from '../../role/list-role/ListRoleView';
import { RoleService } from '../../role/role.service';
import { UserService } from '../user.service';
import { ShowUserView } from './ShowUserView';
import { UpdateUserView } from './UpdateUserView';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css'],
})
export class UpdateUserComponent
  implements OnInit, UpdateUserView, ShowUserView, ListRoleView
{
  public form: any;
  public customErrors: any;
  public roles: Role[] = [];
  public loading: Boolean = true;
  public formLoading: Boolean = true;
  public passwordForm: any;

  constructor(
    private roleService: RoleService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.customErrors = AddUserError();
    this.roleService.listRoleView = this;
    this.userService.showUserView = this;
    this.userService.updateUserView = this;
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
    this.userService.updateUser(form.value);
  }

  onSubmitPassword() {
    this.loading = true;
    this.userService.updateUserPassword(this.passwordForm.value);
  }

  onFetchRoleListSuccess(roleRecords: RoleRecords): void {
    this.roles = roleRecords.roles;
    this.userService.getUser(this.route.snapshot.params.id);
  }

  onFetchRoleListError(error: any): void {
    console.log('UpdateUserComponent > onFetchRoleListError', error);
  }

  @SweetSuccessDialog({
    title: 'Update User',
    text: 'Updated User Successfull',
  })
  onUpdateUserSuccess(user: User) {
    this.router.navigate(['/dashboard/users']);
  }

  onUpdateUserError(error: any) {
    if (error.status == 400) FormErrorSetter(this.form, error.error);
    console.log('UpdateUserComponent > onUpdateUserError', error);
  }

  onShowUserSuccess(user: User) {
    this.form = UpdateUserFormHelper(user);
    this.passwordForm = UpdateUserPasswordFormHelper(user);
    this.formLoading = false;
  }

  @SweetErrorDialog({
    title: 'Error',
    text: 'No User Found',
  })
  onShowUserError(error: any) {
    console.log('UpdateUserComponent > onShowUserError', error);
  }

  @SweetSuccessDialog({
    title: 'Update Password',
    text: 'Updated User Password Successfull',
  })
  onUpdateUserPasswordSuccess(user: User) {
    this.router.navigate(['/dashboard/users']);
  }

  @SweetErrorDialog({
    title: 'Error',
    text: 'Something Went Wrong',
  })
  onUpdateUserPasswordError(error: any) {
    console.log('UpdateUserComponent > onUpdateUserPasswordError', error);
  }

  onLoadComplete() {
    this.loading = false;
  }
}
