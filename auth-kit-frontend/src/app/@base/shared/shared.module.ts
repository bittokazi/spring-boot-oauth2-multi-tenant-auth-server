import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VerticleTextInputComponent } from './components/form-components/verticle-text-input/verticle-text-input.component';
import { ReactiveFormsModule } from '@angular/forms';
import { VerticleSelectInputComponent } from './components/form-components/verticle-select-input/verticle-select-input.component';
import { BasicPaginationComponent } from './components/pagination/basic-pagination/basic-pagination.component';
import { DangerFlatComponent } from './components/alerts/danger-flat/danger-flat.component';
import { TablePaginationLoaderComponent } from './components/table-components/table-pagination-loader/table-pagination-loader.component';
import { DisableControlDirective } from './directives/input-disabled/disable-control.directive';
import { VerticalCheckboxInputComponent } from './components/form-components/vertical-checkbox-input/vertical-checkbox-input.component';
import { VerticleSwitchInputComponent } from './components/form-components/verticle-switch-input/verticle-switch-input.component';
import { VertitcleTextareaInputComponent } from './components/form-components/vertitcle-textarea-input/vertitcle-textarea-input.component';
import { FileUploadInputComponent } from './components/form-components/file-upload-input/file-upload-input.component';
import { LoadingButtonComponent } from './components/buttons/loading-button/loading-button.component';

@NgModule({
  declarations: [
    VerticleTextInputComponent,
    VerticleSelectInputComponent,
    BasicPaginationComponent,
    DangerFlatComponent,
    TablePaginationLoaderComponent,
    DisableControlDirective,
    VerticalCheckboxInputComponent,
    VerticleSwitchInputComponent,
    VertitcleTextareaInputComponent,
    FileUploadInputComponent,
    LoadingButtonComponent,
  ],
  imports: [CommonModule, ReactiveFormsModule],
  exports: [
    VerticleTextInputComponent,
    VerticleSelectInputComponent,
    BasicPaginationComponent,
    DangerFlatComponent,
    TablePaginationLoaderComponent,
    DisableControlDirective,
    VerticalCheckboxInputComponent,
    VerticleSwitchInputComponent,
    VertitcleTextareaInputComponent,
    FileUploadInputComponent,
    LoadingButtonComponent,
  ],
})
export class SharedModule {}
