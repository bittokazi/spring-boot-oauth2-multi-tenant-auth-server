import { TestBed } from '@angular/core/testing';

import { SweetAlartService } from './sweet-alart.service';

describe('SweetAlartService', () => {
  let service: SweetAlartService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SweetAlartService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
