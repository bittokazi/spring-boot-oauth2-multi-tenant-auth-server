import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { RefreshTokenService } from './refresh.token.service';
import { AuthHolder } from '../AuthHolder';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  public env = environment;

  constructor(public refreshTokenService: RefreshTokenService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (request.url.includes('/token/refresh')) {
    } else if (request.url.includes('/public/api')) {
    } else {
      if (this.getCookie('access_token')) {
        let headers = {
          Authorization: `Bearer ${this.getCookie('access_token')}`,
        };
        if (AuthHolder.getTenant() != '') {
          headers['X-DATA-TENANT'] = AuthHolder.getTenant();
        }
        request = request.clone({
          setHeaders: headers,
        });
      }
    }
    //@ts-ignore
    return next.handle(request).pipe(
      map((event: HttpEvent<any>) => {
        return event;
      }),
      catchError((err: HttpErrorResponse) => {
        if (err.status == 401) {
          if (request.url.includes('/public/api')) {
          } else {
            if (this.getCookie('refresh_token')) {
              return this.refreshTokenService.refreshToken(request, next);
            } else {
              return throwError(err);
            }
          }
        }
        return throwError(err);
      })
    );
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
