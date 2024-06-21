import { RoleRecords } from '../../../../../@base/shared/models/role/RoleRecords';

export interface ListRoleView {
  onFetchRoleListSuccess(roleRecords: RoleRecords): void;
  onFetchRoleListError(error: any): void;
  onLoadComplete();
}
