import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebsiteNumberCardComponent } from './website-number-card.component';

describe('WebsiteNumberCardComponent', () => {
  let component: WebsiteNumberCardComponent;
  let fixture: ComponentFixture<WebsiteNumberCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WebsiteNumberCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WebsiteNumberCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
