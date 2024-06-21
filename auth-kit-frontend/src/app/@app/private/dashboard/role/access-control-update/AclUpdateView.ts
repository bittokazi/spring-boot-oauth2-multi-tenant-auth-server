import { FormArray } from '@angular/forms';
import { RestResourceAccess } from 'src/app/@base/shared/models/acl/RestResourceAccess';

export interface AclUpdateView {
  onAclListLoadSuccess(restResourceAccess: RestResourceAccess[]);
  onAclListLoadError(error: any);
  onAclUpdateSuccess();
  onAclUpdateError(error: any);
  onLoadComplete();
  onAclFormProcessComplete(key: String, formArray: FormArray);
}
