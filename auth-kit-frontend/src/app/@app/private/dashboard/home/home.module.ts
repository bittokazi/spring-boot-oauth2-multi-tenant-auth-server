import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DefaultComponent } from './default/default.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { UserProfileCardComponent } from './components/user-profile-card/user-profile-card.component';
import { WebsiteNumberCardComponent } from './components/website-number-card/website-number-card.component';
import { ResumeConfigBoxComponent } from './components/resume-config-box/resume-config-box.component';

const routes: Routes = [{ path: '', component: DefaultComponent }];

@NgModule({
  declarations: [DefaultComponent, UserProfileCardComponent, WebsiteNumberCardComponent, ResumeConfigBoxComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    LayoutModule,
  ],
  exports: [RouterModule],
})
export class HomeModule {}
