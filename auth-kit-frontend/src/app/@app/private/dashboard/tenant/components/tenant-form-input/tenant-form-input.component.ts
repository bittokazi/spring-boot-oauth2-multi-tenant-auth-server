import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild,
} from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-tenant-form-input',
  templateUrl: './tenant-form-input.component.html',
  styleUrls: ['./tenant-form-input.component.css'],
})
export class TenantFormInputComponent implements OnInit {
  @Input('loading')
  public loading: Boolean;

  @Input('form')
  public form: any;

  @Input('customErrors')
  public customErrors: any;

  @Input('edit')
  public edit: Boolean = false;

  @Output() onSubmitEvent = new EventEmitter<FormGroup>();

  @ViewChild('themeFile') public themeFile: ElementRef;

  constructor() {}

  ngOnInit(): void {}

  onSubmit() {
    this.onSubmitEvent.emit(this.form);
  }
}
