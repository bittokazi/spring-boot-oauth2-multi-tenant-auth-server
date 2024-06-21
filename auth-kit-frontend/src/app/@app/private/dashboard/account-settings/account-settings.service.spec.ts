import { TestBed } from '@angular/core/testing';

import { AccountSettingsService } from './account-settings.service';

describe('AccountSettingsService', () => {
  let service: AccountSettingsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountSettingsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
