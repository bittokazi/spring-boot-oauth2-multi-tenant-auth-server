import { User } from 'src/app/@base/shared/models/user/User';

export interface UpdatePasswordView {
  onUserPasswordUpateSuccess(user: User): void;
  onUserPasswordUpdateError(error: any): void;
  onLoadComplete(): void;
}
