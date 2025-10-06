import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageUploadComponent } from './components/image-upload/image-upload.component';

@NgModule({
  declarations: [
    ImageUploadComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    ImageUploadComponent
  ]
})
export class SharedModule { }