import { SectionTemplateProperty } from './SectionTemplateProperty';

export interface SectionTemplate {
  id: number;
  name: string;
  key: string;
  sectionType: SectionType;
  sectionTemplateProperties: SectionTemplateProperty[];
  grouping: boolean;
  delete: boolean;
}

export enum SectionType {
  SINGLE = 'SINGLE',
  MULTIPLE = 'MULTIPLE',
}

export enum ValueType {
  TEXT = 'TEXT',
  NUMBER = 'NUMBER',
  DECIMAL_NUMBER = 'DECIMAL_NUMBER',
  TEXTAREA = 'TEXTAREA',
}

export interface SectionTemplateDataSource {
  sectionTypes: SectionType[];
  valueTypes: ValueType[];
}
