import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserAccountSettingsComponent } from './user-account-settings/user-account-settings.component';
import { UserSecuritySettingsComponent } from './user-security-settings/user-security-settings.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { SharedModule } from 'src/app/@base/shared/shared.module';
import { AccountSettingsTopBarComponent } from './components/account-settings-top-bar/account-settings-top-bar.component';
import { QRCodeModule } from 'angularx-qrcode';

const routes: Routes = [
  { path: '', component: UserAccountSettingsComponent },
  { path: 'security', component: UserSecuritySettingsComponent },
];

@NgModule({
  declarations: [
    UserAccountSettingsComponent,
    UserSecuritySettingsComponent,
    AccountSettingsTopBarComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    LayoutModule,
    SharedModule,
    QRCodeModule,
  ],
  exports: [RouterModule],
})
export class AccountSettingsModule {}
