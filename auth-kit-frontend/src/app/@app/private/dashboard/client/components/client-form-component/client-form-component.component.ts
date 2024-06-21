import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-client-form-component',
  templateUrl: './client-form-component.component.html',
  styleUrls: ['./client-form-component.component.css'],
})
export class ClientFormComponentComponent implements OnInit {
  @Input('loading')
  public loading: Boolean;

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
