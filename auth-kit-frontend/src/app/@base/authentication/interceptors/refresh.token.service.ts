import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { environment } from '../../../../environments/environment';
import {
  HttpClient,
  HttpHeaders,
  HttpBackend,
  HttpEvent,
  HttpResponse,
} from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthHolder } from '../AuthHolder';
import { LoginResponse } from '../../shared/models/auth/LoginResponse';
import { catchError, filter, switchMap, take } from 'rxjs/operators';

@Injectable()
export class RefreshTokenService {
  public env = environment;
  headers: any;
  public csrf: any;
  private http: HttpClient;
  tokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);
  isRefreshingToken: boolean = false;

  constructor(public router: Router, httpBackend: HttpBackend) {
    this.http = new HttpClient(httpBackend);
  }

  refreshToken(request, next) {
    if (!this.isRefreshingToken) {
      this.isRefreshingToken = true;
      this.tokenSubject.next(null);
      return this.http
        .post<LoginResponse>(
          this.env.baseUrl + '/oauth2/refresh/token',
          this.getRefreshTokenPayload(),
          {
            observe: 'response',
          }
        )
        .pipe(
          switchMap((response: HttpResponse<LoginResponse>) => {
            AuthHolder.setToken(response.body);
            request = this.setAuthorizationHeader(request);
            document.cookie = `token=${response.body.access_token}; Path=/;`;
            this.tokenSubject.next(response.body);
            this.isRefreshingToken = false;
            return this.http.request(request.method, request.url, {
              body: request.body,
              headers: request.headers,
              observe: 'response',
            });
          }),
          catchError((error) => {
            if (error.status == 401) {
              console.log('Invalid Refresh Token');
              this.router.navigate(['/login']);
            }
            return Observable.throw(error);
          })
        );
    } else {
      return this.tokenSubject.pipe(
        filter((token) => token != null),
        take(1),
        switchMap((response) => {
          request = this.setAuthorizationHeader(request);
          return this.http.request(request.method, request.url, {
            body: request.body,
            headers: request.headers,
            observe: 'response',
          });
        })
      );
    }
  }

  getRefreshTokenPayload() {
    let payload = {
      refresh_token: this.getCookie('refresh_token')
        ? this.getCookie('refresh_token')
        : '',
    };
    return payload;
  }

  getBasicAuthHeader(basicAuthToken) {
    let headers = new HttpHeaders().set('Content-Type', 'application/json');
    headers = headers.set('Authorization', `Basic ${basicAuthToken}`);
    headers = headers.set('X-Requested-With', `XMLHttpRequest`);
    return headers;
  }

  setAuthorizationHeader(request) {
    request = request.clone({
      headers: request.headers.set(
        'Authorization',
        `Bearer ${AuthHolder.getToken().access_token}`
      ),
    });
    return request;
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
