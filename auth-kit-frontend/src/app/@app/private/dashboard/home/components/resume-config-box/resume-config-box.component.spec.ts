import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumeConfigBoxComponent } from './resume-config-box.component';

describe('ResumeConfigBoxComponent', () => {
  let component: ResumeConfigBoxComponent;
  let fixture: ComponentFixture<ResumeConfigBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResumeConfigBoxComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResumeConfigBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
