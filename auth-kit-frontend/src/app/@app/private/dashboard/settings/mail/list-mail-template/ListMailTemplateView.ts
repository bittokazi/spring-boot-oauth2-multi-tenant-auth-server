export interface ListMailTemplateView {
  onMailTemplateListFetchSuccess(mailTypes: String[]);
  onMailTemplateListFetchError(error: any);
  onLoadComplete();
}
