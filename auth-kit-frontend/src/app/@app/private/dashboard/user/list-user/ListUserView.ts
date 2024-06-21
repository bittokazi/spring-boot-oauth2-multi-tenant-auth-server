import { UserRecords } from 'src/app/@base/shared/models/user/UserRecords';

export interface ListUserView {
  onFetchUsersSuccess(userRecords: UserRecords): void;
  onFetchUsersError(error: any): void;
}
