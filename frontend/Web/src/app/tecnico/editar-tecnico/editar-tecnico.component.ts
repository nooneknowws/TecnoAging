import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { catchError, timeout } from 'rxjs/operators';
import { of } from 'rxjs';
import { Endereco } from '../../_shared/models/pessoa/endereco';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { AuthService } from '../../_shared/services/auth.service';
import { TecnicoService } from '../../_shared/services/tecnico.service';
import { ImageService } from '../../_shared/services/image.service';
import { EnumEstadosBrasil } from '../../_shared/models/estadosbrasil.enum';

@Component({
  selector: 'app-editar-tecnico',
  templateUrl: './editar-tecnico.component.html',
  styleUrls: ['./editar-tecnico.component.css']
})
export class EditarTecnicoComponent implements OnInit {
  tecnicoId!: number;
  endereco: Endereco = new Endereco();
  form: Tecnico = new Tecnico();

  isLoading = false;
  isSaving = false;
  isUpdateSuccess = false;
  isUpdateFailed = false;
  errorMessage = '';

  currentPhotoUrl: string | null = null;
  uploadError: string = '';
  uploadSuccess: string = '';

  formattedDataNasc: string = '';
  estados = Object.values(EnumEstadosBrasil);

  cepInvalido = false;
  erroTimeout = false;
  isCurrentUser = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private tecnicoService: TecnicoService,
    private imageService: ImageService,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.tecnicoId = +id;
      this.loadTecnicoData();
    } else {
      this.errorMessage = 'ID do técnico não fornecido';
      this.router.navigate(['/tecnico/gerenciar-tecnicos']);
    }
  }

  loadTecnicoData(): void {
    this.isLoading = true;

    this.tecnicoService.getTecnicoById(this.tecnicoId).subscribe({
      next: (tecnico) => {
        this.form = JSON.parse(JSON.stringify(tecnico));

        // Verificar se é o técnico logado
        const currentUser = this.authService.getCurrentUser();
        this.isCurrentUser = !!(currentUser && currentUser.id === this.tecnicoId);

        if (!this.form.endereco) {
          this.form.endereco = new Endereco();
        }
        this.endereco = JSON.parse(JSON.stringify(this.form.endereco));

        if (this.form.dataNasc) {
          this.formattedDataNasc = this.formatDateForInput(this.form.dataNasc);
        }

        if (this.form.telefone) {
          this.form.telefone = this.formatarTelefoneSeNecessario(this.form.telefone);
        }

        if (this.form.id) {
          this.loadCurrentPhoto(this.form.id);
        }

        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar técnico:', error);
        this.errorMessage = 'Erro ao carregar dados do técnico';
        this.isLoading = false;
        setTimeout(() => {
          this.router.navigate(['/tecnico/gerenciar-tecnicos']);
        }, 3000);
      }
    });
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
    if (telefone.includes('(') && telefone.includes(')') && telefone.includes('-')) {
      return telefone;
    }
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

    if (!this.form.dataNasc) {
      this.errorMessage = 'Data de nascimento é obrigatória.';
      this.isUpdateFailed = true;
      return;
    }

    try {
      this.isSaving = true;

      this.form.idade = this.calcularIdade(this.form.dataNasc);
      this.form.endereco = { ...this.endereco };

      if (!this.isEnderecoValid()) {
        this.errorMessage = 'Endereço incompleto. Verifique todos os campos obrigatórios.';
        this.isUpdateFailed = true;
        this.isSaving = false;
        return;
      }

      // Se estiver editando o próprio perfil, usa o método que atualiza o cache
      const updateObservable = this.isCurrentUser
        ? this.tecnicoService.updateTecnicoAndRefreshCache(this.form)
        : this.tecnicoService.updateTecnico(this.form);

      updateObservable.subscribe({
        next: (updatedTecnico: Tecnico) => {
          this.isSaving = false;
          this.isUpdateSuccess = true;
          this.isUpdateFailed = false;

          setTimeout(() => {
            this.router.navigate(['/tecnico/gerenciar-tecnicos']);
          }, 2000);
        },
        error: (err) => {
          console.error('Erro ao atualizar técnico:', err);
          this.isSaving = false;
          this.errorMessage = err.error?.message || err.message || 'Erro ao atualizar técnico. Tente novamente.';
          this.isUpdateFailed = true;

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

  cancelar(): void {
    this.router.navigate(['/tecnico/gerenciar-tecnicos']);
  }
}
