import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../../models/user/User';

export const UpdateUserFormHelper = (user: User) => {
  return new FormGroup({
    id: new FormControl(user.id),
    firstName: new FormControl(user.firstName, [
      Validators.required,
      Validators.minLength(1),
    ]),
    lastName: new FormControl(user.lastName, [
      Validators.required,
      Validators.minLength(1),
    ]),
    username: new FormControl(user.username, [
      Validators.required,
      Validators.minLength(5),
      Validators.pattern('^[a-z0-9_.]+'),
    ]),
    email: new FormControl(user.email, [
      Validators.required,
      Validators.pattern(
        /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      ),
    ]),
    roles: new FormArray([
      new FormGroup({
        id: new FormControl(user.roles[0].id, [
          Validators.required,
          Validators.minLength(1),
        ]),
        name: new FormControl(''),
      }),
    ]),
    imageAbsolutePath: new FormControl(
      user?.imageAbsolutePath ? user?.imageAbsolutePath : ''
    ),
    imageName: new FormControl(user?.imageName ? user?.imageName : ''),
  });
};

export const UpdateUserPasswordFormHelper = (user: User) => {
  return new FormGroup({
    id: new FormControl(user.id),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
  });
};

export const UpdateMyPasswordFormHelper = (user: User) => {
  return new FormGroup({
    id: new FormControl(user.id),
    currentPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.pattern(
        /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/
      ),
    ]),
    newConfirmPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
  });
};
