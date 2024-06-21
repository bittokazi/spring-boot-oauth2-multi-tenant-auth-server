import { TestBed } from '@angular/core/testing';

import { EmailVerifyGuard } from './email-verify.guard';

describe('EmailVerifyGuard', () => {
  let guard: EmailVerifyGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(EmailVerifyGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
