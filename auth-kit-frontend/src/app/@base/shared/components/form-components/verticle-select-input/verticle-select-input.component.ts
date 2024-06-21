import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { InputError } from './../../../models/component/input/InputError';

@Component({
  selector: 'app-verticle-select-input',
  templateUrl: './verticle-select-input.component.html',
  styleUrls: ['./verticle-select-input.component.css'],
})
export class VerticleSelectInputComponent implements OnInit {
  @Input('label')
  public label: String = '';

  @Input('control')
  public control: FormControl;

  @Input('none')
  public none: Boolean = true;

  @Input('items')
  public items: [];

  @Input('key')
  public key: String = '';

  @Input('value')
  public value: String = '';

  @Input('errorMessage')
  public errorMessage: String = '';

  @Input()
  public inputErrors: InputError[] = [];

  @Input('disabled')
  public disabled: Boolean = false;

  @Input('typeString')
  public typeString: Boolean = false;

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
