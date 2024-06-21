import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardRootComponent } from './dashboard-root/dashboard-root.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from 'src/app/@base/layout/layout.module';

const routes: Routes = [
  {
    path: '',
    component: DashboardRootComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./home/home.module').then((m) => m.HomeModule),
      },
      {
        path: 'users',
        loadChildren: () =>
          import('./user/user.module').then((m) => m.UserModule),
      },
      {
        path: 'roles',
        loadChildren: () =>
          import('./role/role.module').then((m) => m.RoleModule),
      },
      {
        path: 'settings',
        loadChildren: () =>
          import('./settings/settings.module').then((m) => m.SettingsModule),
      },
      {
        path: 'account-settings',
        loadChildren: () =>
          import('./account-settings/account-settings.module').then(
            (m) => m.AccountSettingsModule
          ),
      },
      {
        path: 'tenants',
        loadChildren: () =>
          import('./tenant/tenant.module').then((m) => m.TenantModule),
      },
      {
        path: 'clients',
        loadChildren: () =>
          import('./client/client.module').then((m) => m.ClientModule),
      },
    ],
  },
];

@NgModule({
  declarations: [DashboardRootComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    LayoutModule,
  ],
  providers: [
    // { provide: LOADING_BAR_CONFIG, useValue: { latencyThreshold: 100 } },
  ],
  exports: [RouterModule],
})
export class DashboardModule {}
