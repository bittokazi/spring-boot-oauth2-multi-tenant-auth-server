import { WebsiteLayoutProperty } from './WebsiteLayoutProperty';

export interface WebsiteLayout {
  id: Number;
  name: String;
  type: String;
  layoutAbsolutePath: String;
  uploaded: Boolean;
  folderPath: String;
  enabled: boolean;
}
