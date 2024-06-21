import { Component, Input, OnInit } from '@angular/core';
import { FeatherIconSetter } from '../../../js/FeatherIconSetter';

@Component({
  selector: 'app-danger-flat',
  templateUrl: './danger-flat.component.html',
  styleUrls: ['./danger-flat.component.css'],
})
export class DangerFlatComponent implements OnInit {
  @Input('message')
  public message: String = 'Error Occured';

  public open: Boolean = false;

  constructor() {}

  ngOnInit(): void {
    setTimeout(() => {
      this.open = true;
    }, 100);
    setTimeout(() => {
      FeatherIconSetter();
    }, 500);
  }
}
