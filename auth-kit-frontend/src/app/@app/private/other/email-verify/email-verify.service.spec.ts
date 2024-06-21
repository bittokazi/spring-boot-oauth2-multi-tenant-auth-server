import { TestBed } from '@angular/core/testing';

import { EmailVerifyService } from './email-verify.service';

describe('EmailVerifyService', () => {
  let service: EmailVerifyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmailVerifyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
