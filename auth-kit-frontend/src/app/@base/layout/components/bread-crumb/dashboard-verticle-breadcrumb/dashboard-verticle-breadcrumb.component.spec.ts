import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardVerticleBreadcrumbComponent } from './dashboard-verticle-breadcrumb.component';

describe('DashboardVerticleBreadcrumbComponent', () => {
  let component: DashboardVerticleBreadcrumbComponent;
  let fixture: ComponentFixture<DashboardVerticleBreadcrumbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardVerticleBreadcrumbComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardVerticleBreadcrumbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
