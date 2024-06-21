import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountSettingsTopBarComponent } from './account-settings-top-bar.component';

describe('AccountSettingsTopBarComponent', () => {
  let component: AccountSettingsTopBarComponent;
  let fixture: ComponentFixture<AccountSettingsTopBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccountSettingsTopBarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountSettingsTopBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
