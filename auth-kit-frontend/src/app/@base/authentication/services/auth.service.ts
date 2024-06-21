import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthHolder } from '../AuthHolder';
import menu from './menu.json';
import menuTenant from './menuTenant.json';
import menuUser from './menuUser.json';
import menuTenantAdmin from './menuTenantAdmin.json';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public userInfo;
  public hasAccess: boolean = false;
  public authSubject: Subject<any> = new Subject<any>();

  constructor(private http: HttpClient, private router: Router) {}

  public checkAuthentication() {
    return this.http
      .get<any>(`${environment.baseUrl}/api/users/whoami`)
      .toPromise();
  }

  public logout() {
    this.logoutTasks();
    window.location.href = '/logout';
  }

  public logoutTasks() {
    AuthHolder.setToken(JSON.parse(`{}`));
    AuthHolder.setTenant(null);
    document.cookie =
      'access_token=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  }

  checkPageAccess() {
    return new Promise((resolve) => resolve(true));
    this.hasAccess = false;
    return new Promise((resolve) => {
      if (this.userInfo.accessMenu) {
        this.getPageAccess(
          this.userInfo.accessMenu,
          this.router.url,
          false
        ).then(() => {
          return resolve(this.hasAccess);
        });
      } else {
        return resolve(this.hasAccess);
      }
    });
  }

  getPageAccess(pages, url, _found) {
    return new Promise((resolve) => {
      let _promises = [];
      pages.forEach((page) => {
        _promises.push(
          new Promise((_resolve) => {
            if (page.path.split('/').length == url.split('/').length) {
              let promises = [];
              let found = true;
              page.path.split('/').forEach((p, key) => {
                if (p != '?' && p != url.split('/')[key]) {
                  found = false;
                }
                promises.push({});
              });
              Promise.all(promises).then(() => {
                if (found && page.enabled) {
                  _found = true;
                  this.hasAccess = true;
                  return resolve({});
                }
                if (page.subMenu.length > 0) {
                  this.getPageAccess(page.subMenu, url, _found).then(() => {
                    return _resolve({});
                  });
                } else {
                  return _resolve({});
                }
              });
            } else {
              if (page.subMenu.length > 0) {
                this.getPageAccess(page.subMenu, url, _found).then(() => {
                  return _resolve({});
                });
              } else {
                return _resolve({});
              }
            }
          })
        );
      });
      Promise.all(_promises).then(() => {
        return resolve({});
      });
    });
  }

  getMenu() {
    if (this.userInfo.roles[0].name != 'ROLE_SUPER_ADMIN') {
      return menuUser;
    }
    if (AuthHolder.getTenant() != '' && this.userInfo.adminTenantUser) {
      return menuTenant;
    } else if (!this.userInfo.adminTenantUser) {
      return menuTenantAdmin;
    }
    return menu;
  }

  getCookie(name) {
    function escape(s) {
      return s.replace(/([.*+?\^$(){}|\[\]\/\\])/g, '\\$1');
    }
    var match = document.cookie.match(
      RegExp('(?:^|;\\s*)' + escape(name) + '=([^;]*)')
    );
    return match ? match[1] : null;
  }
}
