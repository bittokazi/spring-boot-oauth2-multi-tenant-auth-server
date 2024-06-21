import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardVerticleFooterComponent } from './dashboard-verticle-footer.component';

describe('DashboardVerticleFooterComponent', () => {
  let component: DashboardVerticleFooterComponent;
  let fixture: ComponentFixture<DashboardVerticleFooterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardVerticleFooterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardVerticleFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
