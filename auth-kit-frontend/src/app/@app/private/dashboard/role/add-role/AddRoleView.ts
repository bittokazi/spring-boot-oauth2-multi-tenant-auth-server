import {Role} from '../../../../../@base/shared/models/role/Role';

export interface AddRoleView {
  onAddRoleSuccess(role: Role): void;
  onAddRoleError(error: any): void;
}
