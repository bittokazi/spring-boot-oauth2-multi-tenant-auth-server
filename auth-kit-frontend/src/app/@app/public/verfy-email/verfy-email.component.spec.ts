import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerfyEmailComponent } from './verfy-email.component';

describe('VerfyEmailComponent', () => {
  let component: VerfyEmailComponent;
  let fixture: ComponentFixture<VerfyEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerfyEmailComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VerfyEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
