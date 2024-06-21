import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResetPasswordReques } from 'src/app/@base/shared/models/reset-password/ResetPasswordRequest';
import { environment } from 'src/environments/environment';
import { ResetPasswordView } from './ResetPasswordView';

@Injectable({
  providedIn: 'root',
})
export class ResetPasswordService {
  public resetPasswordView: ResetPasswordView;

  constructor(private http: HttpClient) {}

  checkTokenValidity(token: String) {
    this.http
      .get<any>(
        `${environment.baseUrl}/public/api/reset/password/token/${token}`
      )
      .toPromise()
      .then((response) => this.resetPasswordView.onResetPasswordToken(response))
      .finally(() => this.resetPasswordView.onLoadComplete());
  }

  resetPasswordRequest(
    token: String,
    resetPasswordRequest: ResetPasswordReques
  ) {
    this.http
      .post<any>(
        `${environment.baseUrl}/public/api/reset/password/token/${token}`,
        resetPasswordRequest
      )
      .toPromise()
      .then((response) => this.resetPasswordView.onResetPassword(response))
      .finally(() => this.resetPasswordView.onLoadComplete());
  }
}
