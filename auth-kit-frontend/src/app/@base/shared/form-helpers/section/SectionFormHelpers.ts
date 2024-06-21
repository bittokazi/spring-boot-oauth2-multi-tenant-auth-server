import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import {
  SectionTemplate,
  SectionType,
  ValueType,
} from '../../models/section-template/SectionTemplate';
import { SectionTemplateProperty } from '../../models/section-template/SectionTemplateProperty';
import { Section } from '../../models/section/Section';
import { SectionList } from '../../models/section/SectionList';
import { SectionProperty } from '../../models/section/SectionProperty';

export function SectionFormHelper(
  sections: Section[],
  sectionTemplates: SectionTemplate[],
  user: any
) {
  let form = new FormArray([]);
  if (sectionTemplates.length > 0) {
    sectionTemplates.map((sectionTemplate) => {
      let section: Section = sections.find(
        (section) => section.sectionTemplate.key == sectionTemplate.key
      );
      AddSection(form, section, sectionTemplate, user);
    });
  }
  return form;
}

export function AddSection(
  form: FormArray,
  section: Section = null,
  sectionTemplate: SectionTemplate,
  user: any
) {
  let formGroup = {
    sectionTemplate: new FormGroup({
      id: new FormControl(sectionTemplate?.id ? sectionTemplate.id : '', [
        Validators.required,
      ]),
      key: new FormControl(sectionTemplate?.key ? sectionTemplate.key : '', [
        Validators.required,
      ]),
      sectionType: new FormControl(
        sectionTemplate?.sectionType ? sectionTemplate.sectionType : '',
        [Validators.required]
      ),
      grouping: new FormControl(
        sectionTemplate?.grouping ? sectionTemplate.grouping : false,
        [Validators.required]
      ),
    }),
    sectionList: new FormArray([]),
    user: new FormGroup({
      id: new FormControl(user?.id ? user.id : '', [Validators.required]),
    }),
  };
  if (section?.id)
    formGroup['id'] = new FormControl(section?.id ? section.id : '');
  form.push(new FormGroup(formGroup));

  if (section?.sectionList.length > 0) {
    section.sectionList
      .sort((a, b) => (a.serialNumber > b.serialNumber ? 1 : -1))
      .map((sectionList, index) => {
        if (sectionTemplate.sectionType == SectionType.SINGLE && index == 0)
          AddSectionList(
            form,
            sectionList,
            sectionTemplate,
            form.controls.length - 1
          );
        else if (sectionTemplate.sectionType == SectionType.MULTIPLE)
          AddSectionList(
            form,
            sectionList,
            sectionTemplate,
            form.controls.length - 1
          );
      });
  }
}

export function AddSectionList(
  form: FormArray,
  sectionList: SectionList = null,
  sectionTemplate: SectionTemplate,
  sectionIndex: number
) {
  let formGroup = {
    sectionProperties: new FormArray([]),
    serialNumber: new FormControl(
      sectionList?.serialNumber
        ? sectionList.serialNumber
        : form.controls[sectionIndex].value.sectionList.length,
      [Validators.required]
    ),
    groupName: new FormControl(
      sectionList?.groupName ? sectionList.groupName : '',
      sectionTemplate?.grouping
        ? [Validators.required, Validators.pattern(/^[0-9A-Za-z ]+$/)]
        : []
    ),
    delete: new FormControl(false),
  };

  if (sectionList?.id)
    formGroup['id'] = new FormControl(sectionList?.id ? sectionList.id : '');

  let _group: any = form.at(sectionIndex);
  _group.controls.sectionList.push(new FormGroup(formGroup));

  sectionTemplate.sectionTemplateProperties
    .sort((a, b) => (a.serialNumber > b.serialNumber ? 1 : -1))
    .map((sectionTemplateProperty) => {
      let sectionProperty: SectionProperty =
        sectionList?.sectionProperties.find(
          (sectionProperty) =>
            sectionProperty?.sectionTemplateProperty.key ==
            sectionTemplateProperty.key
        );
      AddSectionProperty(
        form,
        sectionProperty,
        sectionTemplateProperty,
        sectionIndex,
        _group.controls.sectionList.length - 1
      );
    });
}

export function AddSectionProperty(
  form: FormArray,
  sectionProperty: SectionProperty = null,
  sectionTemplateProperty: SectionTemplateProperty,
  sectionIndex: number,
  listIndex: number
) {
  let formGroup = {
    sectionTemplateProperty: new FormGroup({
      id: new FormControl(
        sectionTemplateProperty?.id ? sectionTemplateProperty.id : '',
        [Validators.required]
      ),
      key: new FormControl(
        sectionTemplateProperty?.key ? sectionTemplateProperty.key : '',
        [Validators.required]
      ),
    }),
    content: new FormControl(
      sectionProperty?.content
        ? sectionProperty.content
        : sectionTemplateProperty.defaultValue == ''
        ? ''
        : sectionTemplateProperty.defaultValue,
      getValidators(sectionTemplateProperty.valueType)
    ),
  };
  if (sectionProperty?.id)
    formGroup['id'] = new FormControl(
      sectionProperty?.id ? sectionProperty.id : ''
    );
  let _group: any = form.at(sectionIndex);
  _group.controls.sectionList
    .at(listIndex)
    .controls.sectionProperties.push(new FormGroup(formGroup));
}

export function getValidators(valueType: ValueType): any[] {
  if (valueType == ValueType.TEXT) {
    return [];
  } else if (valueType == ValueType.NUMBER) {
    return [Validators.pattern(/^[0-9]+$/)];
  } else if (valueType == ValueType.DECIMAL_NUMBER) {
    return [Validators.pattern(/^\d*\.?\d*$/)];
  } else if (valueType == ValueType.TEXTAREA) {
    return [];
  }
  return [];
}
