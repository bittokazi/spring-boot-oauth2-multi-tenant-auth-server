import { SectionTemplate } from '../section-template/SectionTemplate';
import { SectionList } from './SectionList';

export interface Section {
  id: number;
  sectionTemplate: SectionTemplate;
  sectionList: SectionList[];
  user: any;
}
