import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardLayoutComponent } from './dashboard-layout/dashboard-layout.component';
import { DashboardVerticleLightComponent } from './dashboard-layout/dashboard-verticle-light/dashboard-verticle-light.component';
import { LoginLayoutComponent } from './login-layout/login-layout.component';
import { DashboardVerticleNavMenuComponent } from './components/menu/dashboard-verticle-nav-menu/dashboard-verticle-nav-menu.component';
import { RouterModule } from '@angular/router';
import { DashboardVerticleHeaderComponent } from './components/header/dashboard-verticle-header/dashboard-verticle-header.component';
import { DashboardVerticleBreadcrumbComponent } from './components/bread-crumb/dashboard-verticle-breadcrumb/dashboard-verticle-breadcrumb.component';
import { DashboardVerticleFooterComponent } from './components/footer/dashboard-verticle-footer/dashboard-verticle-footer.component';

@NgModule({
  declarations: [
    DashboardLayoutComponent,
    DashboardVerticleLightComponent,
    LoginLayoutComponent,
    DashboardVerticleNavMenuComponent,
    DashboardVerticleHeaderComponent,
    DashboardVerticleBreadcrumbComponent,
    DashboardVerticleFooterComponent,
  ],
  imports: [CommonModule, RouterModule],
  exports: [DashboardLayoutComponent, LoginLayoutComponent, RouterModule],
})
export class LayoutModule {}
