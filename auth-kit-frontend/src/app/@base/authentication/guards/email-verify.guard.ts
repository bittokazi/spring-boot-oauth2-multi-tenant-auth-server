import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
  Router,
} from '@angular/router';
import { Observable, Subscriber } from 'rxjs';
import { SweetAlartService } from '../../shared/components/alerts/sweet-alert/sweet-alart.service';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class EmailVerifyGuard implements CanActivate {
  private authSubscriber$: Subscriber<any>;

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
    return new Promise((resolve) => {
      this.authService.authSubject.subscribe(
        (this.authSubscriber$ = new Subscriber<any>(() => {
          return resolve(true);
          this.authSubscriber$.unsubscribe();
          if (this.authService.userInfo.emailVerified) return resolve(true);
          else {
            this.sweetAlartService.errorDialog(
              'Notice',
              'Please verify your email first to gain access.'
            );
            setTimeout(() => {
              this.router.navigate(['/verify/email']);
            }, 500);
          }
          return resolve(false);
        }))
      );
    });
  }

  ngOnDestroy() {
    console.log('EmailVerifyGuard -> Destroyed');
    this.authSubscriber$.unsubscribe();
  }
}
