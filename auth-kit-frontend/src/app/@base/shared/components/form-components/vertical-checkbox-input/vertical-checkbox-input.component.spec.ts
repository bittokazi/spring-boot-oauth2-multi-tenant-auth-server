import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerticalCheckboxInputComponent } from './vertical-checkbox-input.component';

describe('VerticalCheckboxInputComponent', () => {
  let component: VerticalCheckboxInputComponent;
  let fixture: ComponentFixture<VerticalCheckboxInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerticalCheckboxInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerticalCheckboxInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
