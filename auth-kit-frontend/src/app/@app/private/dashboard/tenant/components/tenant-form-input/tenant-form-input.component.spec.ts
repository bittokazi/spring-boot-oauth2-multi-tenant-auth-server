import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TenantFormInputComponent } from './tenant-form-input.component';

describe('TenantFormInputComponent', () => {
  let component: TenantFormInputComponent;
  let fixture: ComponentFixture<TenantFormInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TenantFormInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TenantFormInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
