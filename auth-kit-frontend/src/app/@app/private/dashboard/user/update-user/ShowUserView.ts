import { User } from 'src/app/@base/shared/models/user/User';

export interface ShowUserView {
  onShowUserSuccess(user: User);
  onShowUserError(error: any);
  onLoadComplete();
}
