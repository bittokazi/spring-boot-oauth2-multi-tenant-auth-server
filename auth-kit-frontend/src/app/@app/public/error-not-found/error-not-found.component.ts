import { Component, OnInit } from '@angular/core';
import { DashboardCustom } from './../../../@base/shared/js/DashboardCustom';

@Component({
  selector: 'app-error-not-found',
  templateUrl: './error-not-found.component.html',
  styleUrls: ['./error-not-found.component.css'],
})
export class ErrorNotFoundComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {
    setTimeout(() => {
      DashboardCustom();
    }, 200);
  }
}
