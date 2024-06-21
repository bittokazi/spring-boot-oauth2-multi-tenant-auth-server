import { SectionTemplateProperty } from '../section-template/SectionTemplateProperty';

export interface SectionProperty {
  id: number;
  sectionTemplateProperty: SectionTemplateProperty;
  content: string;
}
