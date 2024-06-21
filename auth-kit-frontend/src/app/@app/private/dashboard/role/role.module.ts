import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListRoleComponent } from './list-role/list-role.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { SharedModule } from 'src/app/@base/shared/shared.module';
import { AddRoleComponent } from './add-role/add-role.component';
import { AccessControlUpdateComponent } from './access-control-update/access-control-update.component';
import { UpdateRoleComponent } from './update-role/update-role.component';
import { RoleFormComponent } from './components/role-form/role-form.component';

const routes: Routes = [
  { path: '', component: ListRoleComponent },
  { path: 'add', component: AddRoleComponent },
  {path: ':id/update', component: UpdateRoleComponent},
  {
    path: ':id/access-control-list/update',
    component: AccessControlUpdateComponent,
  },
];

@NgModule({
  declarations: [ListRoleComponent, AddRoleComponent, AccessControlUpdateComponent, UpdateRoleComponent, RoleFormComponent],
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
export class RoleModule {}
