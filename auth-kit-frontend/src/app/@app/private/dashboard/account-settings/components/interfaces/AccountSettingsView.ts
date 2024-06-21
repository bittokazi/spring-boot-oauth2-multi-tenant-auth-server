import { User } from 'src/app/@base/shared/models/user/User';

export interface AccountSettingsView {
  onUserInfoFetchSuccess(user: User): void;
  onUserInfoFetchError(error: any): void;
  onLoadComplete(): void;
}
