import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthHolder } from 'src/app/@base/authentication/AuthHolder';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';
import { LoginRequest } from 'src/app/@base/shared/models/auth/LoginRequest';
import { LoginResponse } from 'src/app/@base/shared/models/auth/LoginResponse';
import { GenerateRandomStringAlphaNumeric } from 'src/app/@base/shared/utils/Utils';
import { environment } from 'src/environments/environment';
import { LoginView } from './LoginView';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  public loginView: LoginView;

  constructor(private http: HttpClient, private authService: AuthService) {}

  public login(loginRequest: LoginRequest, captchaString: any) {
    let headers = new HttpHeaders();
    headers = headers.set('captcha-response', captchaString);
    this.http
      .post<LoginResponse>(
        `${environment.baseUrl}/authenticate`,
        loginRequest,
        {
          headers: headers,
        }
      )
      .subscribe({
        next: (response) => this.loginView.loginSuccess(response),
        error: (error) => this.loginView.loginError(error),
      });
  }

  public userAuthCheck() {
    if (!this.getCookie('access_token')) {
      this.authService.logoutTasks();
      this.loginView.userAuthCheckError();
    } else {
      this.authService
        .checkAuthentication()
        .then((response) => {
          this.loginView.userAuthCheckSuccess();
        })
        .catch((error) => {
          this.authService.logoutTasks();
          this.loginView.userAuthCheckError();
        });
    }
  }

  getInstanceId() {
    let instanceId = AuthHolder.getInstanceId();
    if (instanceId != '') {
      return instanceId;
    } else {
      AuthHolder.setInstanceId(GenerateRandomStringAlphaNumeric(50));
      return AuthHolder.getInstanceId();
    }
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
