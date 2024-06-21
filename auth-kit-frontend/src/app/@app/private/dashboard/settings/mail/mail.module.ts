import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MailConfigComponent } from './mail-config/mail-config.component';
import { ListMailTemplateComponent } from './list-mail-template/list-mail-template.component';
import { UpdateMailTemplateComponent } from './update-mail-template/update-mail-template.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LayoutModule } from 'src/app/@base/layout/layout.module';
import { SharedModule } from 'src/app/@base/shared/shared.module';
import { CKEditorModule } from 'ngx-ckeditor';

const routes: Routes = [
  { path: 'config', component: MailConfigComponent },
  { path: 'templates', component: ListMailTemplateComponent },
  {
    path: 'templates/type/:id',
    component: UpdateMailTemplateComponent,
  },
];

@NgModule({
  declarations: [
    MailConfigComponent,
    ListMailTemplateComponent,
    UpdateMailTemplateComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    LayoutModule,
    SharedModule,
    CKEditorModule,
  ],
  exports: [RouterModule],
})
export class MailModule {}
