import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSecuritySettingsComponent } from './user-security-settings.component';

describe('UserSecuritySettingsComponent', () => {
  let component: UserSecuritySettingsComponent;
  let fixture: ComponentFixture<UserSecuritySettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserSecuritySettingsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserSecuritySettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
