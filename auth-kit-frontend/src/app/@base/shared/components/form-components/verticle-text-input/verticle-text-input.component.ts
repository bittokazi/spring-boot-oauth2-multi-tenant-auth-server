import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormControl } from '@angular/forms';
import { InputError } from './../../../models/component/input/InputError';

@Component({
  selector: 'app-verticle-text-input',
  templateUrl: './verticle-text-input.component.html',
  styleUrls: ['./verticle-text-input.component.css'],
})
export class VerticleTextInputComponent implements OnInit {
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

  @Input('type')
  public type: String = 'text';

  @Input('disabled')
  public disabled: Boolean = false;

  @Input('postFix')
  public postFix: String = '';

  @ViewChild('postFixRef') postFixRef: ElementRef;

  constructor(private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {}

  ngAfterViewChecked() {
    this.cdr.detectChanges();
  }

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
