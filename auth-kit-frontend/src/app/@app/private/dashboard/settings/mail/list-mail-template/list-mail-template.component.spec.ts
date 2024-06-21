import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListMailTemplateComponent } from './list-mail-template.component';

describe('ListMailTemplateComponent', () => {
  let component: ListMailTemplateComponent;
  let fixture: ComponentFixture<ListMailTemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListMailTemplateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListMailTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
