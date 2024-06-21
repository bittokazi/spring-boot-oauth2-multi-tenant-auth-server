import { Component, Input, OnInit } from '@angular/core';
import { PaginationInterface } from '../PaginationInterface';

@Component({
  selector: 'app-basic-pagination',
  templateUrl: './basic-pagination.component.html',
  styleUrls: ['./basic-pagination.component.css'],
})
export class BasicPaginationComponent implements OnInit {
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

  constructor() {}

  ngOnInit(): void {}

  gotoPage(page) {
    if (page < 0 || page >= this.totalPage.length) {
      return;
    }
    this.currentPage = page;
    this.paginationInterface.gotoPage(this.currentPage);
  }

  getFivePages() {
    if (this.currentPage - 3 < 1) {
      let n = [];
      for (let i = 0; i < this.totalPage.length && i < 5; i++) {
        n.push(i);
      }
      return n;
    }
  }

  getLastFivePages() {
    if (this.totalPage.length - this.currentPage < 5) {
      let n = [];
      for (let i = this.totalPage.length - 5; i < this.totalPage.length; i++) {
        n.push(i);
      }
      return n;
    }
  }

  getCurrentPage() {
    return this.currentPage + 1;
  }

  getPrevPage() {
    return this.currentPage;
  }

  getNextPage() {
    return this.currentPage + 2;
  }
}
