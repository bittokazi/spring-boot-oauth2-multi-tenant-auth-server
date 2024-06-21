import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DangerFlatComponent } from './danger-flat.component';

describe('DangerFlatComponent', () => {
  let component: DangerFlatComponent;
  let fixture: ComponentFixture<DangerFlatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DangerFlatComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DangerFlatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
