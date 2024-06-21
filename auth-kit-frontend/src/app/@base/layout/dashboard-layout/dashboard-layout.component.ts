import { Component, OnInit } from '@angular/core';
import { DashboardCustom } from './../../shared/js/DashboardCustom';
import { AuthService } from '../../authentication/services/auth.service';
import {
  Event,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router,
} from '@angular/router';
import { TenantService } from 'src/app/@app/private/dashboard/tenant/tenant.service';

let $ = window['$'];

@Component({
  selector: 'app-dashboard-layout',
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.css'],
})
export class DashboardLayoutComponent implements OnInit {
  public hasAccess: boolean = false;
  public init: boolean = true;
  public name: String = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    public tenantService: TenantService
  ) {
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationStart) {
      }

      if (event instanceof NavigationEnd) {
        this.authService.checkPageAccess().then((access: boolean) => {
          this.hasAccess = access;
        });
      }

      if (event instanceof NavigationError) {
        console.log(event.error);
      }
    });
  }

  ngOnInit(): void {
    this.name = this.tenantService.tenantInfo.name;
    this.authService
      .checkPageAccess()
      .then((access: boolean) => {
        console.log(access);
        if (!access) {
          DashboardCustom();
        }
        this.hasAccess = access;
      })
      .finally(() => {
        this.init = false;
      });
  }
}
