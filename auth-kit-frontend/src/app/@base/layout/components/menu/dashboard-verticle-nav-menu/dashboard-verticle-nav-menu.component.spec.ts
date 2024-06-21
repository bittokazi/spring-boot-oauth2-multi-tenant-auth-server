import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardVerticleNavMenuComponent } from './dashboard-verticle-nav-menu.component';

describe('DashboardVerticleNavMenuComponent', () => {
  let component: DashboardVerticleNavMenuComponent;
  let fixture: ComponentFixture<DashboardVerticleNavMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardVerticleNavMenuComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardVerticleNavMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
