import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthHolder } from 'src/app/@base/authentication/AuthHolder';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';
import { EventBusService } from 'src/app/@base/shared/event/event-bus.service';
import { EventInterface } from 'src/app/@base/shared/event/EventInterface';
import { UserInfoChange } from 'src/app/@base/shared/event/user/UserInfoChange';
import { DashboardMenuToggle } from 'src/app/@base/shared/js/DashboardMenuToggle';
import { User } from 'src/app/@base/shared/models/user/User';

@Component({
  selector: 'app-dashboard-verticle-header',
  templateUrl: './dashboard-verticle-header.component.html',
  styleUrls: ['./dashboard-verticle-header.component.css'],
})
export class DashboardVerticleHeaderComponent
  implements OnInit, UserInfoChange
{
  public user: User;
  public authHolder: any;

  constructor(
    private authServie: AuthService,
    private eventBusService: EventBusService,
    private router: Router
  ) {
    this.eventBusService.register<UserInfoChange>({
      key: 'DashboardVerticleHeaderComponent',
      interface: this,
    });
    this.authHolder = AuthHolder;
  }

  ngOnInit(): void {
    this.user = this.authServie.userInfo;
  }

  public btnLogout() {
    this.authServie.logout();
  }

  menuToggle() {
    DashboardMenuToggle();
  }

  onInfoChange(user: User): void {
    this.authServie.userInfo.user = this.user = user;
  }

  switchTenant() {
    AuthHolder.setTenant(null);
    this.router.navigate(['/']);
  }
}
