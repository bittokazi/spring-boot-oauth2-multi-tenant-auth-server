import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SweetAlartService {
  constructor() {}

  showDeleteConfirmation(callback) {
    let Swal = window['Swal'];
    Swal.fire({
      title: 'Are you sure?',
      text: "You won't be able to revert this!",
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
    }).then((result) => {
      if (result.value) {
        callback();
      }
    });
  }

  showConfirmation(
    title,
    text,
    buttonTxt,
    callback,
    allowOutsideClick = true,
    html = null
  ) {
    let Swal = window['Swal'];
    let _config = {
      title,
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: buttonTxt,
      allowOutsideClick: allowOutsideClick,
    };
    if (html) _config['html'] = html;
    else _config['text'] = text;
    Swal.fire(_config).then((result) => {
      if (result.value) {
        callback();
      }
    });
  }

  errorDialog(title, text) {
    let Swal = window['Swal'];
    Swal.fire({
      type: 'error',
      title,
      text,
    });
  }

  successDialog(title, text, html = false) {
    let Swal = window['Swal'];
    if (html) Swal.fire({ title, text, type: 'success', html: text });
    else Swal.fire({ title, text, type: 'success' });
  }
}
