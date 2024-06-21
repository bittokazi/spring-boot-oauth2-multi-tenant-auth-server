import { UploadObject } from 'src/app/@base/shared/models/file/UploadObject';
import { User } from 'src/app/@base/shared/models/user/User';

export interface UpdateMyProfileView {
  onUserInfoUpateSuccess(user: User): void;
  onUserInfoUpdateError(error: any): void;
  onUserImageUploadSuccess(uploadObject: UploadObject): void;
  onUserImageUploadError(error: any): void;
  onLoadComplete(): void;
}
