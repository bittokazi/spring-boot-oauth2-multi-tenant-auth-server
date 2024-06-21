import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddClientComponent } from './add-client/add-client.component';
import { UpdateClientComponent } from './update-client/update-client.component';
import { ListClientComponent } from './list-client/list-client.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { SharedModule } from 'src/app/@base/shared/shared.module';
import { ClientFormComponentComponent } from './components/client-form-component/client-form-component.component';

const routes: Routes = [
  { path: '', component: ListClientComponent },
  { path: 'add', component: AddClientComponent },
  { path: ':id/update', component: UpdateClientComponent },
];

@NgModule({
  declarations: [
    AddClientComponent,
    UpdateClientComponent,
    ListClientComponent,
    ClientFormComponentComponent,
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
export class ClientModule {}
