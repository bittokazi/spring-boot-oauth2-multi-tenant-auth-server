import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-verticle-switch-input',
  templateUrl: './verticle-switch-input.component.html',
  styleUrls: ['./verticle-switch-input.component.css'],
})
export class VerticleSwitchInputComponent implements OnInit {
  @Input('label')
  public label: String = '';

  @Input('control')
  public control: FormControl;

  @Input('color')
  public color: String = 'form-check-primary';

  constructor() {}

  ngOnInit(): void {}
}
