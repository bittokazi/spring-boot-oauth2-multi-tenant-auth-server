import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddTenantComponent } from './add-tenant/add-tenant.component';
import { UpdateTenantComponent } from './update-tenant/update-tenant.component';
import { ListTenantComponent } from './list-tenant/list-tenant.component';
import { TenantFormInputComponent } from './components/tenant-form-input/tenant-form-input.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { SharedModule } from 'src/app/@base/shared/shared.module';

const routes: Routes = [
  { path: '', component: ListTenantComponent },
  { path: 'add', component: AddTenantComponent },
  { path: ':id/update', component: UpdateTenantComponent },
];

@NgModule({
  declarations: [
    AddTenantComponent,
    UpdateTenantComponent,
    ListTenantComponent,
    TenantFormInputComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    LayoutModule,
    SharedModule,
  ],
})
export class TenantModule {}
