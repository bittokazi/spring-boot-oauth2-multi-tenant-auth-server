import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ForgetPasswordRequest } from 'src/app/@base/shared/models/forget-password/ForgetPasswordRequest';
import { environment } from 'src/environments/environment';
import { ForgetPasswordView } from './ForgetPasswordView';

@Injectable({
  providedIn: 'root',
})
export class ForgetPasswordService {
  public forgetPasswordView: ForgetPasswordView;

  constructor(private http: HttpClient) {}

  forgetPasswordRequest(
    forgetPasswordRequest: ForgetPasswordRequest,
    captchaString: any
  ) {
    let headers = new HttpHeaders();
    headers = headers.set('captcha-response', captchaString);
    this.http
      .post<any>(
        `${environment.baseUrl}/public/api/reset/password`,
        forgetPasswordRequest,
        {
          headers: headers,
        }
      )
      .toPromise()
      .then((response) => this.forgetPasswordView.onForgetPassword(response))
      .catch((error) => this.forgetPasswordView.onError(error))
      .finally(() => this.forgetPasswordView.onLoadComplete());
  }
}
