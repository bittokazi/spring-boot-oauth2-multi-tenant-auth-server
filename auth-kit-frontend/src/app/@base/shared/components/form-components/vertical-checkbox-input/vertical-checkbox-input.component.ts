import {Component, Input, OnInit} from '@angular/core';
import {FormControl} from '@angular/forms';
import {InputError} from '../../../models/component/input/InputError';

@Component({
  selector: 'app-vertical-checkbox-input',
  templateUrl: './vertical-checkbox-input.component.html',
  styleUrls: ['./vertical-checkbox-input.component.css']
})
export class VerticalCheckboxInputComponent implements OnInit {

  @Input('label')
  public label: String = '';

  @Input('control')
  public control: FormControl;

  @Input('disabled')
  public disabled: Boolean = false;

  @Input('errorMessage')
  public errorMessage: String = '';

  @Input()
  public inputErrors: InputError[] = [];
  constructor() {
  }

  ngOnInit(): void {
  }

  checkCustomErrors(): boolean {
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
