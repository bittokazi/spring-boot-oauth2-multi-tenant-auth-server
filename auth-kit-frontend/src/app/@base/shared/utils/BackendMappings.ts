import { environment } from 'src/environments/environment';

let API_BASE: String = `${environment.baseUrl}/api`;

export const BackendMappings = {
  // Website Layout Mappings
  WEBSITE_LAYOUT: () => `${API_BASE}/layouts`,
  WEBSITE_LAYOUT_BY_ID: (id: Number) => `${API_BASE}/layouts/${id}`,
  WEBSITE_LAYOUT_REQUIRED_PROPS: () => `${API_BASE}/layouts/required-fields`,
  WEBSITE_LAYOUT_FILE_UPLOAD: () => `${API_BASE}/layouts/upload`,
  // Section Template
  SECTION_TEMPLATE: () => `${API_BASE}/section-templates`,
  SECTION_TEMPLATE_REQUIRED_PROPS: () =>
    `${API_BASE}/section-templates/required-fields`,
  // Section
  SECTION: () => `${API_BASE}/sections`,
  WEBSITE: () => `${API_BASE}/websites`,
};
