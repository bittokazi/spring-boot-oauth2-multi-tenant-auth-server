import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

export const AddUserFormHelper = () => {
  return new FormGroup({
    firstName: new FormControl('', [
      Validators.required,
      Validators.minLength(1),
    ]),
    lastName: new FormControl('', [
      Validators.required,
      Validators.minLength(1),
    ]),
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(5),
      Validators.pattern('^[a-z0-9_.]+'),
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.pattern(
        /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      ),
    ]),
    roles: new FormArray([
      new FormGroup({
        id: new FormControl('', [Validators.required, Validators.minLength(1)]),
        name: new FormControl(''),
      }),
    ]),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
  });
};

export const AddUserError = () => {
  return {
    user: [
      {
        key: 'exist',
        message: 'Username already exist',
      },
    ],
    email: [
      {
        key: 'exist',
        message: 'Email already exist',
      },
    ],
  };
};

export const UpdateMyPasswordError = () => {
  return {
    currentPassword: [
      {
        key: 'currentWrong',
        message: 'Current password do not match',
      },
    ],
    newPassword: [
      {
        key: 'sameToPrevious',
        message: 'New password can not be same as old password',
      },
    ],
    newConfirmPassword: [
      {
        key: 'newDoNotMatch',
        message: 'New password do not match',
      },
    ],
  };
};
