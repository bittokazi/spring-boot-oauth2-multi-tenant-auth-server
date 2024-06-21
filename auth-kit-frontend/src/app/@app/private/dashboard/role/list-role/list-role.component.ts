import { Component, OnInit } from '@angular/core';
import { Role } from '../../../../../@base/shared/models/role/Role';
import { RoleService } from '../role.service';
import { ListRoleView } from './ListRoleView';
import { RoleRecords } from '../../../../../@base/shared/models/role/RoleRecords';
import { FeatherIconSetterInjector } from '../../../../../@base/shared/js/FeatherIconSetter';

@Component({
  selector: 'app-list-role',
  templateUrl: './list-role.component.html',
  styleUrls: ['./list-role.component.css'],
})
export class ListRoleComponent implements OnInit, ListRoleView {
  public roles: Role[] = [];
  public currentPage = 0;
  public currentCount = 10;
  public totalPage = [];
  public totalRecord = 0;

  constructor(public roleService: RoleService) {
    this.roleService.listRoleView = this;
  }

  ngOnInit(): void {
    this.roleService.getRoles(this.currentPage, this.currentCount);
  }

  @FeatherIconSetterInjector()
  onFetchRoleListSuccess(roleRecords: RoleRecords): void {
    this.roles = roleRecords.roles;
    this.totalPage = Array(roleRecords.pages).fill(0);
    this.totalRecord = roleRecords.records;
  }

  onFetchRoleListError(error: any): void {}

  onLoadComplete() {}
}
