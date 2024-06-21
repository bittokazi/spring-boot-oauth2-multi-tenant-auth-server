import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { SharedModule } from 'src/app/@base/shared/shared.module';
import { FrontPageComponent } from './front-page/front-page.component';
import { ErrorNotFoundComponent } from './error-not-found/error-not-found.component';
import { ForgetPasswordComponent } from './forget-password/forget-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { VerfyEmailComponent } from './verfy-email/verfy-email.component';

const routes: Routes = [
  {
    path: '',
    component: FrontPageComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'forget-password',
    component: ForgetPasswordComponent,
  },
  {
    path: 'reset-password/:token',
    component: ResetPasswordComponent,
  },
  {
    path: 'verify/email/:code',
    component: VerfyEmailComponent,
  },
];

@NgModule({
  declarations: [
    LoginComponent,
    FrontPageComponent,
    ErrorNotFoundComponent,
    ForgetPasswordComponent,
    ResetPasswordComponent,
    VerfyEmailComponent,
  ],
  imports: [
    CommonModule,
    LayoutModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    SharedModule,
  ],
  exports: [RouterModule, ErrorNotFoundComponent],
})
export class PublicModule {}
