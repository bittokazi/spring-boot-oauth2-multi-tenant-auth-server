import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MailConfigComponent } from './mail-config.component';

describe('MailConfigComponent', () => {
  let component: MailConfigComponent;
  let fixture: ComponentFixture<MailConfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MailConfigComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MailConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
