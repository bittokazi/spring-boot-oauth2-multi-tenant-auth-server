import { User } from '../user/User';
import { WebsiteLayout } from '../website-layout/WebsiteLayout';

export interface Website {
  id: number;
  name: string;
  key: string;
  layout: WebsiteLayout;
  owner?: User;
  enabled: boolean;
}
