import { Component, OnInit } from '@angular/core';
import { PaginationInterface } from 'src/app/@base/shared/components/pagination/PaginationInterface';
import { FeatherIconSetterInjector } from 'src/app/@base/shared/js/FeatherIconSetter';
import { User } from 'src/app/@base/shared/models/user/User';
import { UserRecords } from 'src/app/@base/shared/models/user/UserRecords';
import { UserService } from '../user.service';
import { ListUserView } from './ListUserView';

@Component({
  selector: 'app-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.css'],
})
export class ListUserComponent
  implements OnInit, ListUserView, PaginationInterface
{
  public users: User[] = [];
  public currentPage = 0;
  public currentCount = 10;
  public totalPage = [];
  public totalRecord = 0;

  constructor(public userService: UserService) {
    this.userService.listUserView = this;
  }

  ngOnInit(): void {
    this.userService.getUsers(this.currentPage, this.currentCount);
  }

  @FeatherIconSetterInjector()
  onFetchUsersSuccess(userRecords: UserRecords) {
    this.users = userRecords.users;
    this.totalPage = Array(userRecords.pages).fill(0);
    this.totalRecord = userRecords.records;
    console.log(userRecords);
  }

  gotoPage(page) {
    this.users = [];
    this.currentPage = page;
    this.userService.getUsers(this.currentPage, this.currentCount);
  }

  onFetchUsersError(error: any) {}
}
