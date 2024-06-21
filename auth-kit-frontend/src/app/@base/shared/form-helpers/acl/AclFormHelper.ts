import { FormControl, FormGroup } from '@angular/forms';

export function AclFormHelper(resource, roleId: Number) {
  return new FormGroup({
    id: new FormControl(resource.id ? resource.id : ''),
    backendResource: new FormControl(resource.backendResource),
    resourceType: new FormControl(resource.resourceType),
    roleId: new FormControl(roleId),
    backendRequestType: new FormControl(resource.backendRequestType),
    enabled: new FormControl(resource.roleId ? true : false),
  });
}
