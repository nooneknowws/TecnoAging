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
  form: Tecnico = new Tecnico();
  
  // Estados de loading e feedback
  isLoading = false;
  isSaving = false;
  
  // Mensagens de sucesso/erro para o formulário
  isUpdateSuccess = false;
  isUpdateFailed = false;
  errorMessage = '';
  
  // Mensagens para upload de foto
  currentPhotoUrl: string | null = null;
  uploadError: string = '';
  uploadSuccess: string = '';
  
  formattedDataNasc: string = '';
  estados = Object.values(EnumEstadosBrasil);
  
  cepInvalido = false;
  erroTimeout = false;

  constructor(
    private authService: AuthService,
    private tecnicoService: TecnicoService,
    private imageService: ImageService,
    private http: HttpClient,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUserData();
  }

  loadUserData(): void {
    this.isLoading = true;

    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser instanceof Tecnico) {
      // Deep copy para evitar modificações no objeto original
      this.form = JSON.parse(JSON.stringify(currentUser));

      // Inicializa o endereço se não existir
      if (!this.form.endereco) {
        this.form.endereco = new Endereco();
      }
      this.endereco = JSON.parse(JSON.stringify(this.form.endereco));

      // Formatar data para o input date
      if (this.form.dataNasc) {
        this.formattedDataNasc = this.formatDateForInput(this.form.dataNasc);
      }

      // Formatar telefone se necessário
      if (this.form.telefone) {
        this.form.telefone = this.formatarTelefoneSeNecessario(this.form.telefone);
      }

      // Carregar foto do perfil
      if (this.form.id) {
        this.loadCurrentPhoto(this.form.id);
      }
    }

    this.isLoading = false;
  }

  private formatDateForInput(date: Date): string {
    if (!date) return '';

    const d = new Date(date);
    if (isNaN(d.getTime())) return '';

    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }

  private formatarTelefoneSeNecessario(telefone: string | undefined): string {
    if (!telefone) return '';
    // Se já estiver formatado com parênteses e hífen
    if (telefone.includes('(') && telefone.includes(')') && telefone.includes('-')) {
      return telefone;
    }
    // Se for apenas números
    const numbers = telefone.replace(/\D/g, '');
    if (numbers.length === 11) {
      return `(${numbers.substring(0,2)}) ${numbers.substring(2,7)}-${numbers.substring(7)}`;
    }
    return telefone;
  }

  onDataNascChange(dateString: string): void {
    if (dateString) {
      this.form.dataNasc = new Date(dateString);
      this.formattedDataNasc = dateString;
    } else {
      this.form.dataNasc = undefined;
      this.formattedDataNasc = '';
    }
  }

  loadCurrentPhoto(tecnicoId: number): void {
    this.imageService.getTecnicoPhoto(tecnicoId).subscribe({
      next: (response) => {
        if (response && response.image) {
          this.currentPhotoUrl = response.image;
        }
      },
      error: (error) => {
        console.log('Erro ao carregar foto:', error);
        this.currentPhotoUrl = null;
      }
    });
  }

  onImageSelected(base64Image: string): void {
    if (!this.form?.id || !base64Image) {
      this.uploadError = 'Erro: dados insuficientes para upload da imagem.';
      return;
    }

    this.clearUploadMessages();

    this.imageService.uploadTecnicoPhoto(this.form.id, base64Image).subscribe({
      next: () => {
        this.uploadSuccess = 'Foto atualizada com sucesso!';
        this.currentPhotoUrl = base64Image;
        this.clearSuccessMessage();
      },
      error: (error) => {
        this.uploadError = error.error?.error || 'Erro ao fazer upload da imagem.';
        this.clearErrorMessage();
      }
    });
  }

  onImageError(error: string): void {
    this.uploadError = error;
    this.clearErrorMessage();
  }

  private clearUploadMessages(): void {
    this.uploadError = '';
    this.uploadSuccess = '';
  }

  private clearSuccessMessage(): void {
    setTimeout(() => {
      this.uploadSuccess = '';
      this.cdRef.detectChanges();
    }, 3000);
  }

  private clearErrorMessage(): void {
    setTimeout(() => {
      this.uploadError = '';
      this.cdRef.detectChanges();
    }, 5000);
  }

  calcularIdade(dataNasc: Date): number {
    if (!dataNasc) return 0;
    
    const hoje = new Date();
    const nascimento = new Date(dataNasc);
    
    if (isNaN(nascimento.getTime())) return 0;
    
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const mesAtual = hoje.getMonth();
    const mesNascimento = nascimento.getMonth();
    
    if (mesNascimento > mesAtual || 
        (mesNascimento === mesAtual && nascimento.getDate() > hoje.getDate())) {
      idade--;
    }
    
    return Math.max(0, idade);
  }

  buscarCep(): void {
    const cep = this.endereco.cep?.toString().replace(/\D/g, '') || '';
    
    if (cep.length !== 8) {
      this.cepInvalido = true;
      this.erroTimeout = false;
      return;
    }

    // Reset dos estados de erro
    this.cepInvalido = false;
    this.erroTimeout = false;

    this.authService.buscarCep(cep)
      .pipe(
        timeout(5000),
        catchError(error => {
          if (error.name === 'TimeoutError') {
            this.erroTimeout = true;
            this.cepInvalido = false;
          } else {
            this.cepInvalido = true;
            this.erroTimeout = false;
          }
          return of(null);
        })
      )
      .subscribe({
        next: (endereco: Endereco | null) => {
          if (endereco) {
            // Preserva o CEP original e o número
            const cepOriginal = this.endereco.cep;
            const numeroOriginal = this.endereco.numero;
            const complementoOriginal = this.endereco.complemento;
            
            this.endereco = { ...endereco };
            this.endereco.cep = cepOriginal;
            this.endereco.numero = numeroOriginal;
            this.endereco.complemento = complementoOriginal;
            
            this.cepInvalido = false;
            this.erroTimeout = false;
            this.cdRef.detectChanges();
          } else if (!this.erroTimeout) {
            this.cepInvalido = true;
          }
        }
      });
  }

  private resetFormMessages(): void {
    this.isUpdateSuccess = false;
    this.isUpdateFailed = false;
    this.errorMessage = '';
  }

  onSubmit(form: NgForm): void {
    this.resetFormMessages();

    if (!form.valid) {
      this.errorMessage = 'Formulário inválido. Verifique todos os campos obrigatórios.';
      this.isUpdateFailed = true;
      return;
    }

    // Validações adicionais
    if (!this.form.dataNasc) {
      this.errorMessage = 'Data de nascimento é obrigatória.';
      this.isUpdateFailed = true;
      return;
    }

    try {
      // Ativar loading
      this.isSaving = true;
      
      // Atualizar idade
      this.form.idade = this.calcularIdade(this.form.dataNasc);
      
      // Atualizar endereço
      this.form.endereco = { ...this.endereco };
      
      // Validar se endereço está completo
      if (!this.isEnderecoValid()) {
        this.errorMessage = 'Endereço incompleto. Verifique todos os campos obrigatórios.';
        this.isUpdateFailed = true;
        this.isSaving = false;
        return;
      }

      // Usar o novo método que atualiza o cache automaticamente
      this.tecnicoService.updateTecnicoAndRefreshCache(this.form).subscribe({
        next: (updatedTecnico: Tecnico) => {
          this.isSaving = false;
          this.isUpdateSuccess = true;
          this.isUpdateFailed = false;
          
          // Atualizar o form com a resposta do servidor
          this.form = { ...updatedTecnico };
          if (this.form.dataNasc) {
            this.formattedDataNasc = this.formatDateForInput(this.form.dataNasc);
          }
          
          // Atualizar endereço local
          if (this.form.endereco) {
            this.endereco = { ...this.form.endereco };
          }
          
          console.log('Perfil atualizado e cache refreshed:', updatedTecnico);
          
          // Limpar mensagem de sucesso após alguns segundos
          setTimeout(() => {
            this.isUpdateSuccess = false;
            this.cdRef.detectChanges();
          }, 5000);
        },
        error: (err) => {
          console.error('Erro ao atualizar perfil:', err);
          this.isSaving = false;
          this.errorMessage = err.error?.message || err.message || 'Erro ao atualizar perfil. Tente novamente.';
          this.isUpdateFailed = true;
          
          // Limpar mensagem de erro após alguns segundos
          setTimeout(() => {
            this.isUpdateFailed = false;
            this.errorMessage = '';
            this.cdRef.detectChanges();
          }, 8000);
        }
      });
    } catch (error) {
      console.error('Erro no processamento do formulário:', error);
      this.isSaving = false;
      this.errorMessage = 'Erro interno. Tente novamente.';
      this.isUpdateFailed = true;
    }
  }

  /**
   * Método alternativo que força refresh dos dados do servidor
   */
  salvarComRefreshDoServidor(form: NgForm): void {
    this.resetFormMessages();

    if (!form.valid) {
      this.errorMessage = 'Formulário inválido. Verifique todos os campos obrigatórios.';
      this.isUpdateFailed = true;
      return;
    }

    try {
      this.isSaving = true;
      
      // Preparar dados
      this.form.idade = this.calcularIdade(this.form.dataNasc!);
      this.form.endereco = { ...this.endereco };
      
      if (!this.isEnderecoValid()) {
        this.errorMessage = 'Endereço incompleto. Verifique todos os campos obrigatórios.';
        this.isUpdateFailed = true;
        this.isSaving = false;
        return;
      }

      // Salvar primeiro
      this.tecnicoService.updateTecnico(this.form).subscribe({
        next: () => {
          // Depois refresh dos dados do servidor
          this.authService.refreshCurrentUserFromServer().subscribe({
            next: (userAtualizado) => {
              this.isSaving = false;
              this.isUpdateSuccess = true;
              
              if (userAtualizado) {
                this.form = { ...userAtualizado as Tecnico };
                if (this.form.dataNasc) {
                  this.formattedDataNasc = this.formatDateForInput(this.form.dataNasc);
                }
                if (this.form.endereco) {
                  this.endereco = { ...this.form.endereco };
                }
              }
              
              console.log('Dados refreshed do servidor:', userAtualizado);
              
              setTimeout(() => {
                this.isUpdateSuccess = false;
                this.cdRef.detectChanges();
              }, 5000);
            },
            error: (refreshError) => {
              this.isSaving = false;
              this.isUpdateSuccess = true; // Dados foram salvos
              this.errorMessage = 'Dados salvos, mas erro ao atualizar cache local.';
              console.error('Erro ao refresh dados:', refreshError);
            }
          });
        },
        error: (err) => {
          console.error('Erro ao atualizar perfil:', err);
          this.isSaving = false;
          this.errorMessage = err.error?.message || err.message || 'Erro ao atualizar perfil. Tente novamente.';
          this.isUpdateFailed = true;
        }
      });
    } catch (error) {
      console.error('Erro no processamento do formulário:', error);
      this.isSaving = false;
      this.errorMessage = 'Erro interno. Tente novamente.';
      this.isUpdateFailed = true;
    }
  }

  private isEnderecoValid(): boolean {
    return !!(
      this.endereco.cep &&
      this.endereco.logradouro &&
      this.endereco.numero &&
      this.endereco.bairro &&
      this.endereco.municipio &&
      this.endereco.uf
    );
  }
}