import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { TenantService } from 'src/app/@app/private/dashboard/tenant/tenant.service';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';

@Component({
  selector: 'app-dashboard-verticle-breadcrumb',
  templateUrl: './dashboard-verticle-breadcrumb.component.html',
  styleUrls: ['./dashboard-verticle-breadcrumb.component.css'],
})
export class DashboardVerticleBreadcrumbComponent implements OnInit {
  public menu;
  public currentUrl;
  public breadCrumbs;

  constructor(
    private authService: AuthService,
    private router: Router,
    private titleService: Title,
    private tenantService: TenantService,
  ) {
    this.router.events.subscribe((val: any) => {
      this.currentUrl = val.url;
      this.init();
    });
  }

  ngOnInit(): void {
    this.init();
  }

  init() {
    this.menu = [
      {
        path: '/dashboard',
        title: 'Dashboard',
        show: true,
        subMenu: this.authService.getMenu(),
      },
    ];
    this.currentUrl = this.router.url;
    this.breadCrumbs = this.genBread(this.menu);
    this.setTitle();
  }

  genBread(menu) {
    let breadcrumb = [];
    let urlJoined = '';
    for (var i = 0; i < this.router.url.split('/').length; i++) {
      if (this.router.url.split('/')[i] != '') {
        urlJoined += '/' + this.router.url.split('/')[i];
        let result = this.searchLink(urlJoined, menu);
        if (result) {
          result = Object.create(result);
          if (result['var']) {
            result.path = this.router.url;
            breadcrumb.push(result);
          } else {
            breadcrumb.push(result);
          }
        } else {
          let temp = {
            title: this.router.url.split('/')[i],
            path: this.router.url,
          };
          breadcrumb.push(temp);
        }
      }
    }
    return breadcrumb;
  }

  searchLink(urlJoined, menu) {
    for (let i = 0; i < menu.length; i++) {
      if (menu[i].path == urlJoined) {
        menu[i]['var'] = false;
        return menu[i];
      } else if (
        urlJoined.split('/').length == menu[i].path.split('/').length
      ) {
        let found = true;
        for (var j = 0; j < urlJoined.split('/').length; j++) {
          if (urlJoined.split('/')[j] == menu[i].path.split('/')[j]) {
          } else if (menu[i].path.split('/')[j] == '?') {
          } else {
            found = false;
          }
        }
        menu[i]['var'] = true;
        if (found) return menu[i];
      }
      if (menu[i].subMenu.length > 0) {
        let result = this.searchLink(urlJoined, menu[i].subMenu);
        if (result) {
          return result;
        }
      }
    }
    return null;
  }

  setTitle() {
    this.titleService.setTitle(
      `${this.tenantService.tenantInfo.name} | ${this.breadCrumbs[this.breadCrumbs.length - 1].title}`
    );
  }
}
