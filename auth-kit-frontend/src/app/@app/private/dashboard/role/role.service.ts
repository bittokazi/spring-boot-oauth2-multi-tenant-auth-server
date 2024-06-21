import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Role } from 'src/app/@base/shared/models/role/Role';
import { environment } from 'src/environments/environment';
import { ListRoleView } from './list-role/ListRoleView';
import { RoleRecords } from '../../../../@base/shared/models/role/RoleRecords';
import { AddRoleView } from './add-role/AddRoleView';
import { RestResourceAccess } from 'src/app/@base/shared/models/acl/RestResourceAccess';
import { AclUpdateView } from './access-control-update/AclUpdateView';
import { FormArray } from '@angular/forms';
import { AclFormHelper } from 'src/app/@base/shared/form-helpers/acl/AclFormHelper';
import { UpdateRoleView } from './update-role/UpdateRoleView';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  public listRoleView: ListRoleView;
  public addRoleView: AddRoleView;
  public aclUpdateView: AclUpdateView;
  public updateRoleView: UpdateRoleView;

  constructor(private http: HttpClient) {}

  public getRoles(page?: number, count?: number): void {
    let uri = '/api/roles';
    if (page && count) {
      uri = uri + `?page=${page}&count=${count}`;
    }
    const url = `${environment.baseUrl}` + uri;
    this.http
      .get<RoleRecords>(url)
      .toPromise()
      .then((response) => this.listRoleView.onFetchRoleListSuccess(response))
      .catch((error) => this.listRoleView.onFetchRoleListError(error))
      .finally(() => this.listRoleView.onLoadComplete());
  }

  public addRole(role: Role): void {
    this.http
      .post<Role>(`${environment.baseUrl}/api/roles`, role)
      .toPromise()
      .then((role) => this.addRoleView.onAddRoleSuccess(role))
      .catch((error) => this.addRoleView.onAddRoleError(error));
  }

  public getAccessControlList(id: Number) {
    this.http
      .get<RestResourceAccess[]>(`${environment.baseUrl}/api/roles/${id}/acl`)
      .toPromise()
      .then((data) => this.aclUpdateView.onAclListLoadSuccess(data))
      .catch((error) => this.aclUpdateView.onAclListLoadError(error))
      .finally(() => this.aclUpdateView.onLoadComplete());
  }

  public getRole(id: Number): void {
    this.http
      .get<Role>(`${environment.baseUrl}/api/roles/${id}`)
      .toPromise()
      .then((response) => this.updateRoleView.onRoleFetchSuccess(response))
      .catch((error) => this.updateRoleView.onRoleFetchError(error))
      .finally(() => this.updateRoleView.onLoadComplete());
  }

  sortResourceGroups(restResourceAccessList: RestResourceAccess[]) {
    let resourceGroups: any = {};
    let promises = [];
    return new Promise((resolve) => {
      restResourceAccessList.forEach((element: RestResourceAccess) => {
        promises.push(
          new Promise((resolve) => {
            if (resourceGroups[`${element.resourceGroup}`]) {
              resourceGroups[`${element.resourceGroup}`].push(element);
            } else {
              resourceGroups[`${element.resourceGroup}`] = [];
              resourceGroups[`${element.resourceGroup}`].push(element);
            }
            return resolve({});
          })
        );
      });
      Promise.all(promises).then(() => {
        return resolve(resourceGroups);
      });
    });
  }

  updateAceesControlList(id: Number, formList: []) {
    let data = [];
    Object.keys(formList).map((key) => {
      data = [...data, ...formList[key].value.filter((d) => d.enabled)];
    });
    this.http
      .put<RestResourceAccess[]>(
        `${environment.baseUrl}/api/roles/${id}/acl`,
        data
      )
      .toPromise()
      .then(() => this.aclUpdateView.onAclUpdateSuccess())
      .catch((error) => this.aclUpdateView.onAclUpdateError(error))
      .finally(() => this.aclUpdateView.onLoadComplete());
  }

  processAclForm(data, roleId: Number) {
    Object.keys(data).map((key) => {
      let formArray = new FormArray([]);
      data[key].map((resource) => {
        formArray.push(AclFormHelper(resource, roleId));
      });
      this.aclUpdateView.onAclFormProcessComplete(key, formArray);
    });
  }

  updateRole(id: number, role: Role): void {
    this.http
      .put<Role>(`${environment.baseUrl}/api/roles/${id}`, role)
      .toPromise()
      .then(() => this.updateRoleView.onRoleUpdateSuccess())
      .catch((error) => this.updateRoleView.onRoleUpdateError(error))
      .finally(() => this.updateRoleView.onLoadComplete());
  }

}
