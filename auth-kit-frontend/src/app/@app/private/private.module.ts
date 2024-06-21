import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthGuard } from 'src/app/@base/authentication/guards/auth.guard';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { EmailVerifyComponent } from './other/email-verify/email-verify.component';
import { EmailVerifyGuard } from 'src/app/@base/authentication/guards/email-verify.guard';

const routes: Routes = [
  {
    canActivate: [AuthGuard, EmailVerifyGuard],
    path: 'dashboard',
    loadChildren: () =>
      import('./dashboard/dashboard.module').then((m) => m.DashboardModule),
  },
  {
    canActivate: [AuthGuard],
    path: 'verify/email',
    component: EmailVerifyComponent,
  },
];

@NgModule({
  declarations: [EmailVerifyComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    LayoutModule,
  ],
  exports: [RouterModule],
})
export class PrivateModule {}
