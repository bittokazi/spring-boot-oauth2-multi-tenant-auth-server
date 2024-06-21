import { WebsiteLayout } from './WebsiteLayout';

export interface WebsiteLayoutProperty {
  id: Number;
  name: String;
  websiteLayout?: WebsiteLayout;
  serialNumber?: Number;
  delete: Boolean;
}
