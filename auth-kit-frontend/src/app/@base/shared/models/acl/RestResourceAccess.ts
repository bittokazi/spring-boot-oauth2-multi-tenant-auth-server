export interface RestResourceAccess {
  id: Number;
  backendResource: String;
  resourceType: String;
  backendRequestType: String;
  roleId: Number;
  resourceGroup: String;
  linkedFrontend: Boolean;
  title: String;
}
