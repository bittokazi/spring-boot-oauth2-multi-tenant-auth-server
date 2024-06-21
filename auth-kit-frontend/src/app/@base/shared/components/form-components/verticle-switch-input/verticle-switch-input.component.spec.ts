import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerticleSwitchInputComponent } from './verticle-switch-input.component';

describe('VerticleSwitchInputComponent', () => {
  let component: VerticleSwitchInputComponent;
  let fixture: ComponentFixture<VerticleSwitchInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerticleSwitchInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerticleSwitchInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
