import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablePaginationLoaderComponent } from './table-pagination-loader.component';

describe('TablePaginationLoaderComponent', () => {
  let component: TablePaginationLoaderComponent;
  let fixture: ComponentFixture<TablePaginationLoaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TablePaginationLoaderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TablePaginationLoaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
