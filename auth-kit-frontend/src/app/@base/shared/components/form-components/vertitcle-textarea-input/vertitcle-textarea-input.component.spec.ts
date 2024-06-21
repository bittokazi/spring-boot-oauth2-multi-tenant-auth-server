import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VertitcleTextareaInputComponent } from './vertitcle-textarea-input.component';

describe('VertitcleTextareaInputComponent', () => {
  let component: VertitcleTextareaInputComponent;
  let fixture: ComponentFixture<VertitcleTextareaInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VertitcleTextareaInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VertitcleTextareaInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
