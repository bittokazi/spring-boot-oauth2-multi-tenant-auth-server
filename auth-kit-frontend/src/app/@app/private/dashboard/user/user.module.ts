import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddUserComponent } from './add-user/add-user.component';
import { ListUserComponent } from './list-user/list-user.component';
import { RouterModule, Routes } from '@angular/router';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UpdateUserComponent } from './update-user/update-user.component';
import { SharedModule } from 'src/app/@base/shared/shared.module';
import { UserFormComponentComponent } from './components/user-form-component/user-form-component.component';

const routes: Routes = [
  { path: '', component: ListUserComponent },
  { path: 'add', component: AddUserComponent },
  { path: ':id/update', component: UpdateUserComponent },
];

@NgModule({
  declarations: [AddUserComponent, ListUserComponent, UpdateUserComponent, UserFormComponentComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    LayoutModule,
    SharedModule,
  ],
  exports: [RouterModule],
})
export class UserModule {}
