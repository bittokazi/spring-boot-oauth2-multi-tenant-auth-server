import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccessControlUpdateComponent } from './access-control-update.component';

describe('AccessControlUpdateComponent', () => {
  let component: AccessControlUpdateComponent;
  let fixture: ComponentFixture<AccessControlUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AccessControlUpdateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AccessControlUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
