import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SweetAlartService } from 'src/app/@base/shared/components/alerts/sweet-alert/sweet-alart.service';
import { User } from 'src/app/@base/shared/models/user/User';
import { UserRecords } from 'src/app/@base/shared/models/user/UserRecords';
import { environment } from 'src/environments/environment';
import { AddUserView } from './add-user/AddUserView';
import { ListUserView } from './list-user/ListUserView';
import { ShowUserView } from './update-user/ShowUserView';
import { UpdateUserView } from './update-user/UpdateUserView';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  public addUserView: AddUserView;
  public listUserView: ListUserView;
  public showUserView: ShowUserView;
  public updateUserView: UpdateUserView;

  constructor(
    private http: HttpClient,
    private sweetAlartService: SweetAlartService
  ) {}

  public addUser(user: User) {
    this.http
      .post<User>(`${environment.baseUrl}/api/users`, user)
      .toPromise()
      .then((response) => this.addUserView.onAddUserSuccess(response))
      .catch((error) => this.addUserView.onAddUserError(error))
      .finally(() => this.addUserView.onLoadComplete());
  }

  public getUsers(page: number, count: number) {
    this.http
      .get<UserRecords>(
        `${environment.baseUrl}/api/users?page=${page}&count=${count}`
      )
      .toPromise()
      .then((response) => this.listUserView.onFetchUsersSuccess(response))
      .catch((error) => this.listUserView.onFetchUsersError(error));
  }

  public getUser(id: Number) {
    this.http
      .get<User>(`${environment.baseUrl}/api/users/${id}`)
      .toPromise()
      .then((response) => this.showUserView.onShowUserSuccess(response))
      .catch((error) => this.showUserView.onShowUserError(error))
      .finally(() => this.showUserView.onLoadComplete());
  }

  public updateUser(user: User) {
    this.http
      .put<User>(`${environment.baseUrl}/api/users/${user.id}`, user)
      .toPromise()
      .then((response) => this.updateUserView.onUpdateUserSuccess(response))
      .catch((error) => this.updateUserView.onUpdateUserError(error))
      .finally(() => this.updateUserView.onLoadComplete());
  }

  public updateUserPassword(user: User) {
    this.http
      .put<User>(
        `${environment.baseUrl}/api/users/${user.id}/update/password`,
        user
      )
      .toPromise()
      .then((response) =>
        this.updateUserView.onUpdateUserPasswordSuccess(response)
      )
      .catch((error) => this.updateUserView.onUpdateUserPasswordError(error))
      .finally(() => this.updateUserView.onLoadComplete());
  }
}
