import { Component, Input, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-file-upload-input',
  templateUrl: './file-upload-input.component.html',
  styleUrls: ['./file-upload-input.component.css'],
})
export class FileUploadInputComponent implements OnInit {
  @Input('label')
  public label: String = '';

  @Input('errorMessage')
  public errorMessage: String = '';

  @Input('dirty')
  public dirty: Boolean = false;

  @Input('disabled')
  public disabled: Boolean = false;

  @Input('fileFormat')
  public fileFormat: String = '*';

  @ViewChild('fileInput') input;

  @Input('size')
  public size: Number = 1;

  public file;

  constructor() {}

  ngOnInit(): void {}

  markDirty() {
    this.dirty = true;
    if (this.input.nativeElement.files.length < 1) {
      this.errorMessage = 'No files Selected';
      return;
    }
    let file: File = this.input.nativeElement.files[0];
    if (!file.type.match(`${this.fileFormat}`)) {
      this.errorMessage = `Please select correct file format (${this.fileFormat})`;
      return;
    }
    if (file.size / 1024 / 1024 > this.size) {
      this.errorMessage = `File exceeds size limit of ${this.size}MB`;
      return;
    }
    this.errorMessage = '';
    this.file = file;
  }
}
