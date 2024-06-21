import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { SweetErrorDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetErrorDialog';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import { FileService } from 'src/app/@base/shared/file/file.service';
import FormErrorSetter from 'src/app/@base/shared/form-helpers/FormErrorHandler';
import { AddUserError } from 'src/app/@base/shared/form-helpers/user/AddUserFormHelper';
import { UpdateUserFormHelper } from 'src/app/@base/shared/form-helpers/user/UpdateUserFormHelper';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';
import { UploadObject } from 'src/app/@base/shared/models/file/UploadObject';
import { User } from 'src/app/@base/shared/models/user/User';
import { AccountSettingsService } from '../account-settings.service';
import { AccountSettingsView } from '../components/interfaces/AccountSettingsView';
import { UpdateMyProfileView } from '../components/interfaces/UpdateMyProfileView';

@Component({
  selector: 'app-user-account-settings',
  templateUrl: './user-account-settings.component.html',
  styleUrls: ['./user-account-settings.component.css'],
})
export class UserAccountSettingsComponent
  implements OnInit, AccountSettingsView, UpdateMyProfileView
{
  public user: User;
  public form: any;
  public loading: boolean = true;
  public customErrors: any;
  @ViewChild('imageFile') myInputVariable: ElementRef;
  public selectedImage: any = '';

  constructor(
    private accountSettingsService: AccountSettingsService,
    private fileService: FileService,
    private readonly sanitizer: DomSanitizer
  ) {
    this.customErrors = AddUserError();
    this.accountSettingsService.accountSettingsView = this;
    this.accountSettingsService.updateMyProfileView = this;
  }

  ngOnInit(): void {
    this.accountSettingsService.getUserInfo();
  }

  onSubmit() {
    this.loading = true;
    // if (this.myInputVariable.nativeElement.files.length > 0) {
    //   this.fileService.uploadFile(
    //     this.myInputVariable.nativeElement.files,
    //     'image.*',
    //     3,
    //     (data) => {
    //       this.accountSettingsService.addUserImage(data);
    //     },
    //     () => {}
    //   );
    //   return;
    // }
    this.accountSettingsService.updateMyProfile(this.form.value);
  }

  onUserInfoFetchSuccess(user: User): void {
    this.user = user;
    this.form = UpdateUserFormHelper(user);
  }

  onUserInfoFetchError(error: any): void {
    console.log('UserAccountSettingsComponent > onUserInfoFetchError', error);
  }

  @SweetSuccessDialog({
    title: 'Success',
    text: 'Successfully Updated Profile',
  })
  onUserInfoUpateSuccess(user: User): void {}

  @SweetErrorDialog({
    title: 'Error',
    text: 'Error Updating Profile',
  })
  onUserInfoUpdateError(error: any): void {
    if (error.status == 400) FormErrorSetter(this.form, error.error);
    console.log('UserAccountSettingsComponent > onUserInfoUpdateError', error);
  }

  onUserImageUploadSuccess(uploadObject: UploadObject): void {
    this.form.controls.imageAbsolutePath.patchValue(
      uploadObject.absoluteFilePath
    );
    this.form.controls.imageName.patchValue(uploadObject.filename);

    this.accountSettingsService.updateMyProfile(this.form.value);
  }

  @SweetErrorDialog({
    title: 'Error',
    text: 'File Upload Error',
  })
  onUserImageUploadError(error: any): void {}

  @FeatherIconSetterInjector()
  onLoadComplete(): void {
    this.loading = false;
  }

  selectImage(event) {
    const [file] = event.srcElement.files;
    if (file) {
      this.selectedImage = this.sanitizer.bypassSecurityTrustResourceUrl(
        URL.createObjectURL(file)
      );
    }
  }

  resetImageFile() {
    this.selectedImage = '';
    this.myInputVariable.nativeElement.value = '';
  }
}
