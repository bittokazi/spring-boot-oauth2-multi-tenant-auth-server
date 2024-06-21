import { ValueType } from './SectionTemplate';

export interface SectionTemplateProperty {
  id: number;
  name: string;
  key: string;
  valueType: ValueType;
  defaultValue: string;
  serialNumber: number;
  delete: boolean;
}
