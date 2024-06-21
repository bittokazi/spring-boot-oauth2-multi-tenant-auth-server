import { Component, OnInit } from '@angular/core';
import { SweetErrorDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetErrorDialog';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import FormErrorSetter from 'src/app/@base/shared/form-helpers/FormErrorHandler';
import { UpdateMyPasswordError } from 'src/app/@base/shared/form-helpers/user/AddUserFormHelper';
import { UpdateMyPasswordFormHelper } from 'src/app/@base/shared/form-helpers/user/UpdateUserFormHelper';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';
import { User } from 'src/app/@base/shared/models/user/User';
import { AccountSettingsService } from '../account-settings.service';
import { AccountSettingsView } from '../components/interfaces/AccountSettingsView';
import { UpdatePasswordView } from '../components/interfaces/UpdatePasswordView';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-user-security-settings',
  templateUrl: './user-security-settings.component.html',
  styleUrls: ['./user-security-settings.component.css'],
})
export class UserSecuritySettingsComponent
  implements OnInit, AccountSettingsView, UpdatePasswordView
{
  public user: User;
  public form: any;
  public loading: boolean = true;
  public customErrors: any;
  public otpPayload: any;
  public otpLink: any;
  public mfaEnabled: Boolean = false;
  public mfaCode = new FormControl('');
  public trustedDevices: [] = [];
  public scratchCodes: [] = [];

  constructor(
    private accountSettingsService: AccountSettingsService,
    private authService: AuthService,
    private sweetAlartService: SweetAlartService
  ) {
    this.accountSettingsService.accountSettingsView = this;
    this.accountSettingsService.updatePasswordView = this;
    this.customErrors = UpdateMyPasswordError();
  }

  ngOnInit(): void {
    this.accountSettingsService.getUserInfo();
  }

  onSubmit() {
    this.loading = true;
    this.accountSettingsService.updatePassword(this.form.value);
  }

  onUserInfoFetchSuccess(user: User): void {
    this.user = user;
    this.form = UpdateMyPasswordFormHelper(user);
    this.mfaEnabled = user.twoFaEnabled;
  }

  @SweetErrorDialog({
    title: 'Error',
    text: 'Error Fetching User Info',
  })
  onUserInfoFetchError(error: any): void {
    console.log('UserSecuritySettingsComponent > onUserInfoFetchError', error);
  }

  @SweetSuccessDialog({
    title: 'Success',
    text: 'Successfully updated password',
  })
  onUserPasswordUpateSuccess(user: User): void {}

  @SweetErrorDialog({
    title: 'Error',
    text: 'Error Updating Password',
  })
  onUserPasswordUpdateError(error: any): void {
    if (error.status == 400) FormErrorSetter(this.form, error.error);
    console.log(
      'UserSecuritySettingsComponent > onUserPasswordUpdateError',
      error
    );
  }

  @FeatherIconSetterInjector()
  onLoadComplete(): void {
    this.loading = false;
  }

  enable2Fa() {
    this.mfaCode.patchValue('');
    this.accountSettingsService
      .generate2FaSecret()
      .then((response: any) => {
        this.otpPayload = response.body;
        this.otpLink = `otpauth://totp/${
          location.protocol + '//' + location.host
        }:${this.user.username}?secret=${response.body.secret}`;
        this.scratchCodes = response.body.scratchCodes;
      })
      .catch((error) => {
        console.log(error);
      });
  }

  confirm2Fa() {
    console.log(this.mfaCode);

    if (this.mfaCode.value == '') return;
    this.accountSettingsService
      .enable2Fa(this.otpPayload, this.mfaCode.value)
      .then((response: any) => {
        if (response.body.twoFaEnabled) {
          document.getElementById('open-recovery-modal').click();
          this.sweetAlartService.successDialog(
            'Success',
            'Two-Step Verification is now enabled for your account.'
          );
          this.accountSettingsService.getUserInfo();
          document.getElementById('modal-close').click();
        } else {
          this.sweetAlartService.errorDialog(
            'Error',
            'Invalid verification code.'
          );
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }

  getTrustedDevices() {
    this.accountSettingsService
      .get2FaTrustedDeviceList()
      .then((response: any) => {
        this.trustedDevices = response.body;
      });
  }

  disable2Fa() {
    this.sweetAlartService.showConfirmation(
      'Disable 2FA',
      'Are you sure you want to disable two-step verification?',
      'Proceed',
      () => {
        this.accountSettingsService.diable2Fa().then((response: any) => {
          this.accountSettingsService.getUserInfo();
          this.getTrustedDevices();
        });
      }
    );
  }

  deleteTrustedDevice(id) {
    this.accountSettingsService.deleteTrustedDevice(id).then((res) => {
      this.getTrustedDevices();
    });
  }

  regenrateScratchCode() {
    this.accountSettingsService.regenrateScratchCode().then((response: any) => {
      this.scratchCodes = response.body.scratchCodes;
      this.sweetAlartService.successDialog(
        'Success',
        'Your 2FA scratch code regenerated.'
      );
      document.getElementById('open-recovery-modal-re').click();
    });
  }
}
