import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';
import { EventBusService } from 'src/app/@base/shared/event/event-bus.service';
import { UserInfoChange } from 'src/app/@base/shared/event/user/UserInfoChange';
import { UploadObject } from 'src/app/@base/shared/models/file/UploadObject';
import { User } from 'src/app/@base/shared/models/user/User';
import { environment } from 'src/environments/environment';
import { AccountSettingsView } from './components/interfaces/AccountSettingsView';
import { UpdateMyProfileView } from './components/interfaces/UpdateMyProfileView';
import { UpdatePasswordView } from './components/interfaces/UpdatePasswordView';

@Injectable({
  providedIn: 'root',
})
export class AccountSettingsService {
  public accountSettingsView: AccountSettingsView;
  public updateMyProfileView: UpdateMyProfileView;
  public updatePasswordView: UpdatePasswordView;

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private eventBusService: EventBusService
  ) {}

  getUserInfo() {
    this.authService
      .checkAuthentication()
      .then((response: any) => {
        this.accountSettingsView.onUserInfoFetchSuccess(response);
      })
      .catch((error: any) => {
        this.accountSettingsView.onUserInfoFetchError(error);
      })
      .finally(() => {
        this.accountSettingsView.onLoadComplete();
      });
  }

  updateMyProfile(user: User) {
    this.http
      .put<User>(`${environment.baseUrl}/api/users/whoami`, user)
      .toPromise()
      .then((response) => {
        this.updateMyProfileView.onUserInfoUpateSuccess(response);
        this.eventBusService
          .emit<UserInfoChange>('DashboardVerticleHeaderComponent')
          ?.onInfoChange(response);
      })
      .catch((error) => this.updateMyProfileView.onUserInfoUpdateError(error))
      .finally(() => this.updateMyProfileView.onLoadComplete());
  }

  updatePassword(user: User) {
    this.http
      .put<User>(`${environment.baseUrl}/api/users/whoami/password`, user)
      .toPromise()
      .then((response) => {
        this.updatePasswordView.onUserPasswordUpateSuccess(response);
      })
      .catch((error) =>
        this.updatePasswordView.onUserPasswordUpdateError(error)
      )
      .finally(() => this.updatePasswordView.onLoadComplete());
  }

  addUserImage(data) {
    this.http
      .post<UploadObject>(`${environment.baseUrl}/api/users/add/image`, data)
      .toPromise()
      .then((response) =>
        this.updateMyProfileView.onUserImageUploadSuccess(response)
      )
      .catch((error) => this.updateMyProfileView.onUserImageUploadError(error))
      .finally(() => this.updateMyProfileView.onLoadComplete());
  }

  generate2FaSecret() {
    return new Promise((resolve, reject) => {
      this.http
        .get(
          `${environment.baseUrl}/api/users/whoami/mfa/otp/generate-secret`,
          {
            observe: 'response',
          }
        )
        .subscribe({
          next: (res) => {
            return resolve(res);
          },
          error: (err) => {
            return reject(err);
          },
        });
    });
  }

  enable2Fa(payload, code) {
    console.log(code);

    let req = {
      ...payload,
      code: code,
    };
    return new Promise((resolve, reject) => {
      this.http
        .post(`${environment.baseUrl}/api/users/whoami/mfa/otp/enable`, req, {
          observe: 'response',
        })
        .subscribe({
          next: (res) => {
            return resolve(res);
          },
          error: (err) => {
            return reject(err);
          },
        });
    });
  }

  get2FaTrustedDeviceList() {
    return new Promise((resolve, reject) => {
      this.http
        .get(`${environment.baseUrl}/api/users/whoami/mfa/trusted-devices`, {
          observe: 'response',
        })
        .subscribe({
          next: (res) => {
            return resolve(res);
          },
          error: (err) => {
            return reject(err);
          },
        });
    });
  }

  diable2Fa() {
    return new Promise((resolve, reject) => {
      this.http
        .get(`${environment.baseUrl}/api/users/whoami/mfa/otp/disable`, {
          observe: 'response',
        })
        .subscribe({
          next: (res) => {
            return resolve(res);
          },
          error: (err) => {
            return reject(err);
          },
        });
    });
  }

  deleteTrustedDevice(id) {
    return new Promise((resolve, reject) => {
      this.http
        .get(
          `${environment.baseUrl}/api/users/whoami/mfa/trusted-devices/${id}`,
          {
            observe: 'response',
          }
        )
        .subscribe({
          next: (res) => {
            return resolve(res);
          },
          error: (err) => {
            return reject(err);
          },
        });
    });
  }

  regenrateScratchCode() {
    return new Promise((resolve, reject) => {
      this.http
        .get(
          `${environment.baseUrl}/api/users/whoami/mfa/generate-scratch-codes`,
          {
            observe: 'response',
          }
        )
        .subscribe({
          next: (res) => {
            return resolve(res);
          },
          error: (err) => {
            return reject(err);
          },
        });
    });
  }
}
