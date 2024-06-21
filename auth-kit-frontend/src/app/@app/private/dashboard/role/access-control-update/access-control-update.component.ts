import { Component, OnInit } from '@angular/core';
import { FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SweetSuccessDialog } from 'src/app/@base/shared/components/alerts/sweet-alert/SweetSuccessDialog';
import { RestResourceAccess } from 'src/app/@base/shared/models/acl/RestResourceAccess';
import { Role } from 'src/app/@base/shared/models/role/Role';
import { CapitalizeFirstLetter } from 'src/app/@base/shared/utils/Utils';
import { RoleService } from '../role.service';
import { UpdateRoleView } from '../update-role/UpdateRoleView';
import { AclUpdateView } from './AclUpdateView';

@Component({
  selector: 'app-access-control-update',
  templateUrl: './access-control-update.component.html',
  styleUrls: ['./access-control-update.component.css'],
})
export class AccessControlUpdateComponent
  implements OnInit, AclUpdateView, UpdateRoleView
{
  public formattedAcl: any = {};
  public resourceGroups: String[] = [];
  public resources: any = {};
  public formList: any = {};
  public loading: Boolean = true;
  public role: Role;

  constructor(
    public roleService: RoleService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.roleService.updateRoleView = this;
    this.roleService.aclUpdateView = this;
  }

  ngOnInit(): void {
    this.roleService.getRole(this.route.snapshot.params['id']);
  }

  onSubmit() {
    this.loading = true;
    this.roleService.updateAceesControlList(
      this.route.snapshot.params['id'],
      this.formList
    );
  }

  onAclFormProcessComplete(key: String, formArray: FormArray) {
    this.resourceGroups.push(CapitalizeFirstLetter(key));
    this.resources[CapitalizeFirstLetter(key)] = this.formattedAcl[`${key}`];
    this.formList[CapitalizeFirstLetter(key)] = formArray;
  }

  onAclListLoadSuccess(restResourceAccessList: RestResourceAccess[]) {
    this.roleService
      .sortResourceGroups(restResourceAccessList)
      .then((data: any) => {
        this.formattedAcl = data;
        this.roleService.processAclForm(data, this.route.snapshot.params['id']);
      });
  }

  onAclListLoadError(error: any) {
    console.log('AccessControlUpdateComponent > onAclListLoadError', error);
  }

  onLoadComplete(): void {
    this.loading = false;
  }

  @SweetSuccessDialog({
    title: 'Access Control List',
    text: 'Update Successfull',
  })
  onAclUpdateSuccess() {
    this.router.navigate(['/dashboard/roles']);
  }

  onAclUpdateError(error) {
    console.log('AccessControlUpdateComponent > onAclUpdateError', error);
  }

  onRoleFetchSuccess(role: Role): void {
    this.role = role;
    this.roleService.getAccessControlList(this.route.snapshot.params['id']);
  }

  onRoleFetchError(error: any): void {
    console.log('AccessControlUpdateComponent > onRoleFetchError', error);
  }

  onRoleUpdateSuccess(): void {
  }

  onRoleUpdateError(error: any): void {
  }
}
