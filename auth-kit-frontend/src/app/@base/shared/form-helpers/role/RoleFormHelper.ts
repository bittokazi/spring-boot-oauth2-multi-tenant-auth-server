import { FormControl, FormGroup, Validators } from '@angular/forms';

export const RoleFormHelper = (config) => {
  return new FormGroup({
    name: new FormControl(config.name ? config.name : '', [
      Validators.required,
      Validators.minLength(2),
      Validators.pattern('^ROLE_[A-Z_]+'),
    ]),
    title: new FormControl(config.title ? config.title : '', [
      Validators.required, Validators.minLength(2),
    ]),
  });
};

export const RoleError = () => {
  return {
    name: [
      {
        key: 'exist',
        message: 'Role name already exist.',
      },
    ],
  };
};
