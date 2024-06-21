import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { InputError } from '../../../models/component/input/InputError';

@Component({
  selector: 'app-vertitcle-textarea-input',
  templateUrl: './vertitcle-textarea-input.component.html',
  styleUrls: ['./vertitcle-textarea-input.component.css'],
})
export class VertitcleTextareaInputComponent implements OnInit {
  @Input('label')
  public label: String = '';

  @Input('placeHolder')
  public placeHolder: String = '';

  @Input('control')
  public control: FormControl;

  @Input('errorMessage')
  public errorMessage: String = '';

  @Input()
  public inputErrors: InputError[] = [];

  @Input('rows')
  public rows: Number = 3;

  @Input('disabled')
  public disabled: Boolean = false;

  constructor() {}

  ngOnInit(): void {}

  checkCustomErrors() {
    let errorExist = false;
    if (this.control.errors && this.inputErrors.length > 0) {
      this.inputErrors.map((inputError: InputError) => {
        if (this.control.errors[`${inputError.key}`]) {
          errorExist = true;
        }
      });
    }
    return errorExist;
  }
}
