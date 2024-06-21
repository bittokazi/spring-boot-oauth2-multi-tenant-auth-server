import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-loading-button',
  templateUrl: './loading-button.component.html',
  styleUrls: ['./loading-button.component.css'],
})
export class LoadingButtonComponent implements OnInit {
  @Input()
  public loading: boolean = false;

  @Input()
  public btnClass: string = 'btn-primary';

  @Input()
  public disabled: boolean = false;

  @Output()
  public buttonClick = new EventEmitter<any>();

  constructor() {}

  ngOnInit(): void {}

  public click() {
    this.buttonClick.emit({});
  }
}
