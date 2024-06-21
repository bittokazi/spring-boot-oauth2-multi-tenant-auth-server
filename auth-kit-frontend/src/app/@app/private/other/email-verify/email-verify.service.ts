import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';
import { User } from 'src/app/@base/shared/models/user/User';
import { environment } from 'src/environments/environment';
import { EmailVerifyView } from '../components/interfaces/EmailVerifyView';

@Injectable({
  providedIn: 'root',
})
export class EmailVerifyService {
  public emailVerifyView: EmailVerifyView;

  constructor(private http: HttpClient, private authService: AuthService) {}

  public sendVerificationMail(user: User, captchaString: any) {
    let headers = new HttpHeaders();
    headers = headers.set('captcha-response', captchaString);
    this.http
      .post<any>(`${environment.baseUrl}/api/users/verify/email`, user, {
        headers: headers,
      })
      .subscribe({
        next: (response) =>
          this.emailVerifyView.sendValidationEmailSuccess(response),
        error: (error) => this.emailVerifyView.sendValidationEmailError(error),
      });
  }

  public verifyLink(code: String) {
    this.http
      .get<any>(`${environment.baseUrl}/public/api/verify/email/${code}`)
      .subscribe({
        next: (response) => this.emailVerifyView.verifyEmailSuccess(response),
        error: (error) => this.emailVerifyView.verifyEmailError(error),
      });
  }
}
