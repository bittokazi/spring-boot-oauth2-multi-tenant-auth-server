import { User } from 'src/app/@base/shared/models/user/User';

export interface AddUserView {
  onAddUserSuccess(user: User);
  onAddUserError(error: any);
  onLoadComplete();
}
