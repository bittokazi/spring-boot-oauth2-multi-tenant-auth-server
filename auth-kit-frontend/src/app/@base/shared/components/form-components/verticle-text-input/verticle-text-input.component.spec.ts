import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerticleTextInputComponent } from './verticle-text-input.component';

describe('VerticleTextInputComponent', () => {
  let component: VerticleTextInputComponent;
  let fixture: ComponentFixture<VerticleTextInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerticleTextInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerticleTextInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
