import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { catchError, timeout } from 'rxjs/operators';
import { of } from 'rxjs';
import { Endereco } from '../../_shared/models/pessoa/endereco';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { AuthService } from '../../_shared/services/auth.service';
import { TecnicoService } from '../../_shared/services/tecnico.service';
import { ImageService } from '../../_shared/services/image.service';
import { EnumEstadosBrasil } from '../../_shared/models/estadosbrasil.enum';

@Component({
  selector: 'app-editar-perfil',
  templateUrl: './editar-perfil.component.html',
  styleUrl: './editar-perfil.component.css'
})
export class EditarPerfilComponent implements OnInit {
  endereco: Endereco = new Endereco();
  form: Tecnico;
  isUpdateSuccess = false;
  isUpdateFailed = false;
  errorMessage = '';
  currentPhotoUrl: string | null = null;
  uploadError: string = '';
  uploadSuccess: string = '';
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  
  estados = Object.values(EnumEstadosBrasil);
  
  cepInvalido = false;
  erroTimeout = false;

  constructor(
    private authService: AuthService,
    private tecnicoService: TecnicoService,
    private imageService: ImageService,
    private http: HttpClient,
    private cdRef: ChangeDetectorRef
  ) {
    this.form = new Tecnico();
  }

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser instanceof Tecnico) {
      this.form = { ...currentUser };
      this.endereco = { ...currentUser.endereco! };
      this.loadCurrentPhoto(currentUser.id!);
    }
  }

  loadCurrentPhoto(tecnicoId: number): void {
    this.imageService.getTecnicoPhoto(tecnicoId).subscribe({
      next: (response) => {
        this.currentPhotoUrl = response.image;
      },
      error: () => {
        this.currentPhotoUrl = null;
      }
    });
  }

  onImageSelected(base64Image: string): void {
    if (!this.form?.id || !base64Image) return;

    this.uploadError = '';
    this.uploadSuccess = '';

    this.imageService.uploadTecnicoPhoto(this.form.id, base64Image).subscribe({
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

  calcularIdade(dataNasc: Date): number {
    const hoje = new Date();
    const nascimento = new Date(dataNasc);
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const mesAtual = hoje.getMonth();
    const mesNascimento = nascimento.getMonth();
    
    if (mesNascimento > mesAtual || 
        (mesNascimento === mesAtual && nascimento.getDate() > hoje.getDate())) {
      idade--;
    }
    
    return idade;
  }


  buscarCep(): void {
    const cep = this.endereco.cep?.toString().replace(/\D/g, '') || '';
    this.authService.buscarCep(cep) // Use service method
      .pipe(
        timeout(5000) // Keep timeout if needed, but service might handle it
      )
      .subscribe({
        next: (endereco: Endereco | null) => {
          if (endereco) {
            this.endereco = endereco;
            this.form.endereco = this.endereco;
            this.cepInvalido = false;
            this.erroTimeout = false;
            this.cdRef.detectChanges();
          } else {
            this.cepInvalido = true;
          }
        },
        error: (error) => {
          if (error.name === 'TimeoutError') {
            this.erroTimeout = true;
            this.cepInvalido = false;
          } else {
            this.cepInvalido = true;
            this.erroTimeout = false;
          }
        }
      });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];
      if (allowedTypes.includes(file.type)) {
        this.selectedFile = file;
        // Create preview URL
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.previewUrl = e.target.result;
        };
        reader.readAsDataURL(file);
      } else {
        this.errorMessage = 'Apenas arquivos de imagem são permitidos (JPEG, PNG, GIF)';
        this.isUpdateFailed = true;
      }
    }
  }

  onSubmit(form: NgForm): void {
    if (form.valid) {
      this.form.idade = this.calcularIdade(this.form.dataNasc!);
      this.form.endereco = this.endereco;
      
      this.tecnicoService.updateTecnico(this.form).subscribe({
        next: (response: Tecnico) => {
          this.isUpdateSuccess = true;
          this.isUpdateFailed = false;
          // Update session storage with new user data
          sessionStorage.setItem('userData', JSON.stringify(response));
        },
        error: err => {
          this.errorMessage = err.error?.message || 'Erro ao atualizar perfil';
          this.isUpdateFailed = true;
        }
      });
    }
  }
}