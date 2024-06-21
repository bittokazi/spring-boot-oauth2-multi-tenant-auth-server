import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFormComponentComponent } from './user-form-component.component';

describe('UserFormComponentComponent', () => {
  let component: UserFormComponentComponent;
  let fixture: ComponentFixture<UserFormComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserFormComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserFormComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
