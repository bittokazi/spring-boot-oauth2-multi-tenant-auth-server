import { Component, OnInit } from '@angular/core';
import { AuthLogin } from './../../shared/js/AuthLogin';
import { JqueryValidate } from './../../shared/js/JqueryValidate';
import { DashboardApp } from './../../shared/js/DashboardApp';
import { DashboardAppMenu } from './../../shared/js/DashboardAppMenu';
import { DashboardCustom } from './../../shared/js/DashboardCustom';

let $ = window['$'];

@Component({
  selector: 'app-login-layout',
  templateUrl: './login-layout.component.html',
  styleUrls: ['./login-layout.component.scss'],
})
export class LoginLayoutComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {
    $('body').removeClass('vertical-layout');
    $('body').removeClass('vertical-menu-modern');
    $('body').removeClass('blank-page');
    $('body').removeClass('navbar-floating');
    $('body').removeClass('footer-static');
    $('body').addClass(
      'vertical-layout vertical-menu-modern blank-page navbar-floating footer-static'
    );
    JqueryValidate();
    DashboardAppMenu();
    DashboardApp();
    AuthLogin();
    DashboardCustom();
  }
}
