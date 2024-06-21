import { User } from 'src/app/@base/shared/models/user/User';

export interface UpdateUserView {
  onUpdateUserSuccess(user: User);
  onUpdateUserError(error: any);
  onUpdateUserPasswordSuccess(user: User);
  onUpdateUserPasswordError(error: any);
  onLoadComplete();
}
