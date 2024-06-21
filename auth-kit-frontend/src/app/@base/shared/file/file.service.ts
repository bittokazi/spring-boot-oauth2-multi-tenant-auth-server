import { Injectable } from '@angular/core';
import { SweetAlartService } from '../components/alerts/sweet-alert/sweet-alart.service';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  constructor(private sweetAlartService: SweetAlartService) {}

  uploadFile(files: any, type = 'image.*', limit = 3, uploadFunc, error) {
    let formData;
    let fileList: FileList = files;
    if (fileList.length > 0) {
      let file: File = fileList[0];
      formData = new FormData();
      formData.append('file', file, file.name);
      if (!file.type.match(type) || file.size / 1024 / 1024 > limit) {
        error();
        this.sweetAlartService.errorDialog(
          'Errro',
          'File have to of type ' + type + ' and under ' + limit + 'MB'
        );
        return;
      }
    }
    if (fileList.length > 0) {
      uploadFunc(formData);
    }
  }
}
