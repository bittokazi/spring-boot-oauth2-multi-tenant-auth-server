import {Component, Input, EventEmitter, OnInit, Output} from '@angular/core';
import {FormGroup} from '@angular/forms';

@Component({
  selector: 'app-role-form',
  templateUrl: './role-form.component.html',
  styleUrls: ['./role-form.component.css']
})
export class RoleFormComponent implements OnInit {

  @Input('loading')
  public loading: Boolean = true;

  @Input('form')
  public form: any;

  @Input('customErrors')
  public customErrors: any;

  @Input('editMode')
  public editMode: Boolean = false;

  @Output() onSubmitEvent = new EventEmitter<FormGroup>();

  constructor() {}

  ngOnInit(): void {}

  onSubmit(): void {
    this.onSubmitEvent.emit(this.form);
  }
}
