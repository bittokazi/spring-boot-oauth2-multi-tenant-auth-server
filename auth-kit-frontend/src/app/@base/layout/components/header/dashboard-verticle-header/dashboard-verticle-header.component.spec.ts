import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardVerticleHeaderComponent } from './dashboard-verticle-header.component';

describe('DashboardVerticleHeaderComponent', () => {
  let component: DashboardVerticleHeaderComponent;
  let fixture: ComponentFixture<DashboardVerticleHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardVerticleHeaderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardVerticleHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
