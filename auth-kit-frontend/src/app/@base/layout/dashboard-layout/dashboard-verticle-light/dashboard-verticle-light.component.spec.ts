import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardVerticleLightComponent } from './dashboard-verticle-light.component';

describe('DashboardVerticleLightComponent', () => {
  let component: DashboardVerticleLightComponent;
  let fixture: ComponentFixture<DashboardVerticleLightComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardVerticleLightComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardVerticleLightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
