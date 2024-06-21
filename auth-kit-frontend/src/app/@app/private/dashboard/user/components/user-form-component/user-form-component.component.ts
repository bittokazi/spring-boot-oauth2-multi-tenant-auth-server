import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Role } from 'src/app/@base/shared/models/role/Role';

@Component({
  selector: 'app-user-form-component',
  templateUrl: './user-form-component.component.html',
  styleUrls: ['./user-form-component.component.css'],
})
export class UserFormComponentComponent implements OnInit {
  @Input('loading')
  public loading: Boolean;

  @Input('roles')
  public roles: Role[];

  @Input('form')
  public form: any;

  @Input('customErrors')
  public customErrors: any;

  @Input('edit')
  public edit: Boolean = false;

  @Output() onSubmitEvent = new EventEmitter<FormGroup>();

  constructor() {}

  ngOnInit(): void {}

  onSubmit() {
    this.onSubmitEvent.emit(this.form);
  }
}
