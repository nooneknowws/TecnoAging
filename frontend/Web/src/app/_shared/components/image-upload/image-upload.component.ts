import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.css']
})
export class ImageUploadComponent implements OnInit, OnChanges{

  constructor(private sanitizer: DomSanitizer){}

  @Input() currentImageUrl: string | null = null;
  @Input() placeholderText: string = 'Clique para selecionar uma imagem';
  @Input() acceptedTypes: string = 'image/*';
  @Input() maxSize: number = 5 * 1024 * 1024; // 5MB
  @Output() imageSelected = new EventEmitter<string>();
  @Output() imageError = new EventEmitter<string>();

  previewUrl: string | null = null;
  isUploading = false;

  // placeholder path inside assets
  private readonly placeholderImage = 'assets/default-profile.jpg';

  ngOnInit() {
    if (this.currentImageUrl) {
      this.previewUrl = this.currentImageUrl;
    } else {
      this.previewUrl = this.placeholderImage;
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['currentImageUrl']) {
      this.previewUrl = this.currentImageUrl ? this.currentImageUrl : this.placeholderImage;
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    if (!file.type.startsWith('image/')) {
      this.imageError.emit('Por favor, selecione apenas arquivos de imagem.');
      return;
    }

    if (file.size > this.maxSize) {
      this.imageError.emit('O arquivo é muito grande. Tamanho máximo: 5MB.');
      return;
    }

    this.isUploading = true;
    
    const reader = new FileReader();
    reader.onload = (e: any) => {
      const base64String = e.target.result as string;
      this.previewUrl = base64String;
      this.imageSelected.emit(base64String);
      this.isUploading = false;
    };

    reader.onerror = () => {
      this.imageError.emit('Erro ao processar a imagem.');
      this.isUploading = false;
    };

    reader.readAsDataURL(file);
  }

  removeImage() {
    this.previewUrl = this.placeholderImage;
    this.imageSelected.emit('');
  }

  triggerFileInput() {
    const fileInput = document.getElementById('imageInput') as HTMLInputElement;
    fileInput?.click();
  }
}