import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateMailTemplateComponent } from './update-mail-template.component';

describe('UpdateMailTemplateComponent', () => {
  let component: UpdateMailTemplateComponent;
  let fixture: ComponentFixture<UpdateMailTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateMailTemplateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateMailTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
