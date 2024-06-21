import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from 'src/app/@base/authentication/services/auth.service';
import { Website } from 'src/app/@base/shared/models/website/Website';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-user-profile-card',
  templateUrl: './user-profile-card.component.html',
  styleUrls: ['./user-profile-card.component.css'],
})
export class UserProfileCardComponent implements OnInit {
  @Input('user')
  public user: any = {};

  @Input('websites')
  public websites: Website[] = [];

  public env: any = environment;

  constructor() {}

  ngOnInit(): void {}
}
