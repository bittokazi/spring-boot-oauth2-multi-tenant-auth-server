import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicPaginationComponent } from './basic-pagination.component';

describe('BasicPaginationComponent', () => {
  let component: BasicPaginationComponent;
  let fixture: ComponentFixture<BasicPaginationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BasicPaginationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicPaginationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
