import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
  Router,
} from '@angular/router';
import { Observable } from 'rxjs';
import { SweetAlartService } from '../../shared/components/alerts/sweet-alert/sweet-alart.service';
import { AuthHolder } from '../AuthHolder';
import { AuthService } from './../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private sweetAlartService: SweetAlartService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    return new Promise(async (resolve, reject) => {
      this.authService
        .checkAuthentication()
        .then((response) => {
          this.authService.userInfo = response;
          this.authService.authSubject.next(this.authService.userInfo);
          return resolve(true);
        })
        .catch((error) => {
          if (error.status == 403) {
            if (this.authService.getCookie('access_token') != null) {
              this.sweetAlartService.showConfirmation(
                'Access Denied',
                'Your Access has been revoked.',
                'Logout',
                () => {
                  this.authService.logout();
                }
              );
            }
            this.router
              .navigateByUrl('/', { skipLocationChange: true })
              .then(() => this.router.navigate(['/login']));
            // this.sweetAlartService.errorDialog(
            //   'Access Denied',
            //   'Your Access has been revoked.'
            // );
            // this.authService.logoutTasks();
            // this.router
            //   .navigateByUrl('/', { skipLocationChange: true })
            //   .then(() => this.router.navigate(['/login']));
          } else {
            // this.router.navigate(['/login']);
            console.log('401 | Unauthorized');

            window.location.href = '/oauth2/login';
          }
          return reject(false);
        });
    });
  }
}
