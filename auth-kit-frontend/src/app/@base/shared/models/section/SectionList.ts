import { SectionProperty } from './SectionProperty';

export interface SectionList {
  id: number;
  sectionProperties: SectionProperty[];
  serialNumber: number;
  delete: boolean;
  groupName: string;
}
