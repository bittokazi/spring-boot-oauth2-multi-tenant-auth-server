import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerticleSelectInputComponent } from './verticle-select-input.component';

describe('VerticleSelectInputComponent', () => {
  let component: VerticleSelectInputComponent;
  let fixture: ComponentFixture<VerticleSelectInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerticleSelectInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerticleSelectInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
