import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientFormComponentComponent } from './client-form-component.component';

describe('ClientFormComponentComponent', () => {
  let component: ClientFormComponentComponent;
  let fixture: ComponentFixture<ClientFormComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientFormComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientFormComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
