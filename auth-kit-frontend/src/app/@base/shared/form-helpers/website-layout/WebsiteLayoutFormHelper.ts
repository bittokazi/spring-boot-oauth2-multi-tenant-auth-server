import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { WebsiteLayout } from '../../models/website-layout/WebsiteLayout';
import { WebsiteLayoutProperty } from '../../models/website-layout/WebsiteLayoutProperty';

export function BuildWebsiteLayoutForm(
  websiteLayout: WebsiteLayout,
  update?: Boolean
) {
  let form: any = {
    name: new FormControl(websiteLayout.name ? websiteLayout.name : '', [
      Validators.required,
    ]),
    type: new FormControl(websiteLayout.type ? websiteLayout.type : '', [
      Validators.required,
    ]),
    layoutAbsolutePath: new FormControl(
      websiteLayout.layoutAbsolutePath ? websiteLayout.layoutAbsolutePath : ''
    ),
    uploaded: new FormControl(
      websiteLayout.uploaded ? websiteLayout.uploaded : true,
      [Validators.required]
    ),
    folderPath: new FormControl(
      websiteLayout.folderPath ? websiteLayout.folderPath : '',
      [Validators.required]
    ),
    enabled: new FormControl(
      websiteLayout.enabled ? websiteLayout.enabled : false,
      [Validators.required]
    ),
  };

  if (update) {
    form = {
      ...form,
      id: new FormControl(websiteLayout.id ? websiteLayout.id : null, [
        Validators.required,
      ]),
    };
  }
  return new FormGroup(form);
}

export const WebsiteLayoutFormError = () => {
  return {
    name: [
      {
        key: 'exist',
        message: 'Name Already Exist',
      },
      {
        key: 'empty',
        message: 'Name Empty',
      },
    ],
    type: [
      {
        key: 'empty',
        message: 'Type Not Selected',
      },
    ],
  };
};
