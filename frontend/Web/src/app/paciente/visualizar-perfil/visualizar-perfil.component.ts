import { Component, OnInit } from '@angular/core';
import { PacienteService } from '../../_shared/services/paciente.service';
import { ImageService } from '../../_shared/services/image.service';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';

@Component({
  selector: 'app-visualizar-perfil',
  templateUrl: './visualizar-perfil.component.html',
  styleUrl: './visualizar-perfil.component.css'
})
export class VisualizarPerfilComponent implements OnInit {
  paciente: Paciente | null = null;
  showDadosPessoais: boolean = false;
  currentPhotoUrl: string | null = null;
  uploadError: string = '';
  uploadSuccess: string = '';

  constructor(
    private pacienteService: PacienteService,
    private imageService: ImageService
  ) {}

  ngOnInit(): void {
    const pacienteId = 1;
    this.pacienteService.getPacienteById(pacienteId).subscribe((data) => {
      this.paciente = data;
      this.loadCurrentPhoto(pacienteId);
    });
  }

  loadCurrentPhoto(pacienteId: number): void {
    this.imageService.getPacientePhoto(pacienteId).subscribe({
      next: (response) => {
        this.currentPhotoUrl = response.image;
      },
      error: () => {
        this.currentPhotoUrl = null;
      }
    });
  }

  onImageSelected(base64Image: string): void {
    if (!this.paciente?.id || !base64Image) return;

    this.uploadError = '';
    this.uploadSuccess = '';

    this.imageService.uploadPacientePhoto(this.paciente.id, base64Image).subscribe({
      next: () => {
        this.uploadSuccess = 'Foto atualizada com sucesso!';
        this.currentPhotoUrl = base64Image;
        setTimeout(() => this.uploadSuccess = '', 3000);
      },
      error: (error) => {
        this.uploadError = error.error?.error || 'Erro ao fazer upload da imagem.';
        setTimeout(() => this.uploadError = '', 5000);
      }
    });
  }

  onImageError(error: string): void {
    this.uploadError = error;
    setTimeout(() => this.uploadError = '', 5000);
  }

  toggleDadosPessoais(): void {
    this.showDadosPessoais = !this.showDadosPessoais;
  }
}
