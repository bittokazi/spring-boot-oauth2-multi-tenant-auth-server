import { User } from '../../models/user/User';

export interface UserInfoChange {
  onInfoChange(user: User): void;
}
