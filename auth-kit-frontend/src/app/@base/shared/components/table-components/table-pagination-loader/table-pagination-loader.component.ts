import { Component, Input, OnInit } from '@angular/core';
import { PaginationInterface } from '../../pagination/PaginationInterface';

@Component({
  selector: 'app-table-pagination-loader',
  templateUrl: './table-pagination-loader.component.html',
  styleUrls: ['./table-pagination-loader.component.css'],
})
export class TablePaginationLoaderComponent implements OnInit {
  @Input('currentPage')
  public currentPage = 0;

  @Input('currentCount')
  public currentCount = 10;

  @Input('totalPage')
  public totalPage = [];

  @Input('totalRecord')
  public totalRecord = 0;

  @Input('paginationInterface')
  public paginationInterface: PaginationInterface;

  @Input('showNumbers')
  public showNumbers = true;

  @Input('showLoader')
  public showLoader = true;

  @Input('showPagination')
  public showPagination = true;

  constructor() {}

  ngOnInit(): void {}
}
