import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { SectionTemplate } from '../../models/section-template/SectionTemplate';
import { SectionTemplateProperty } from '../../models/section-template/SectionTemplateProperty';

export function SectionTemplateFormHelper(sectionTemplates: SectionTemplate[]) {
  let form = new FormArray([]);
  if (sectionTemplates.length > 0) {
    sectionTemplates.map((sectionTemplate: SectionTemplate) =>
      AddSectionTemplate(form, sectionTemplate)
    );
  }
  return form;
}

export function AddSectionTemplate(
  form: FormArray,
  sectionTemplate: SectionTemplate = null
) {
  let formGroup = {
    name: new FormControl(sectionTemplate?.name ? sectionTemplate.name : '', [
      Validators.required,
    ]),
    key: new FormControl(sectionTemplate?.key ? sectionTemplate.key : '', [
      Validators.required,
    ]),
    sectionType: new FormControl(
      sectionTemplate?.sectionType ? sectionTemplate.sectionType : '',
      [Validators.required]
    ),
    sectionTemplateProperties: new FormArray([]),
    grouping: new FormControl(
      sectionTemplate?.grouping ? sectionTemplate.grouping : false,
      [Validators.required]
    ),
    delete: new FormControl(false),
  };
  if (sectionTemplate?.id)
    formGroup['id'] = new FormControl(
      sectionTemplate?.id ? sectionTemplate.id : ''
    );
  form.push(new FormGroup(formGroup));

  if (sectionTemplate?.sectionTemplateProperties?.length > 0) {
    sectionTemplate.sectionTemplateProperties
      .sort((a, b) => (a.serialNumber > b.serialNumber ? 1 : -1))
      .map((sectionTemplateProperty: SectionTemplateProperty) =>
        AddSectionTemplateProperty(
          form,
          sectionTemplateProperty,
          form.controls.length - 1
        )
      );
  }
}

export function AddSectionTemplateProperty(
  form: FormArray,
  sectionTemplateProperty: SectionTemplateProperty = null,
  sectionIndex: number
) {
  let formGroup = {
    name: new FormControl(
      sectionTemplateProperty?.name ? sectionTemplateProperty.name : '',
      [Validators.required]
    ),
    key: new FormControl(
      sectionTemplateProperty?.key ? sectionTemplateProperty.key : '',
      [Validators.required]
    ),
    valueType: new FormControl(
      sectionTemplateProperty?.valueType
        ? sectionTemplateProperty.valueType
        : '',
      [Validators.required]
    ),
    defaultValue: new FormControl(
      sectionTemplateProperty?.defaultValue
        ? sectionTemplateProperty.defaultValue
        : ''
    ),
    serialNumber: new FormControl(
      sectionTemplateProperty?.serialNumber
        ? sectionTemplateProperty.serialNumber
        : form.controls[sectionIndex].value.sectionTemplateProperties.length,
      [Validators.required]
    ),
    delete: new FormControl(false),
  };
  if (sectionTemplateProperty?.id)
    formGroup['id'] = new FormControl(
      sectionTemplateProperty?.id ? sectionTemplateProperty.id : ''
    );
  console.log(form);

  let _group: any = form.at(sectionIndex);
  _group.controls.sectionTemplateProperties.push(new FormGroup(formGroup));
}

export const SectionTemplateFormError = () => {
  return {
    key: [
      {
        key: 'dublicate',
        message: 'Dublicate Key',
      },
    ],
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
