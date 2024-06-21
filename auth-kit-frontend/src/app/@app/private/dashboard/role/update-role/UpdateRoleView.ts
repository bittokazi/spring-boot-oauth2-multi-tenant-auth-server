import {Role} from 'src/app/@base/shared/models/role/Role';

export interface UpdateRoleView {
  onRoleFetchSuccess(role: Role): void;
  onRoleFetchError(error: any): void;
  onRoleUpdateSuccess(): void;
  onRoleUpdateError(error: any): void;
  onLoadComplete(): void;
}
