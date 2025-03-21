import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { catchError, timeout } from 'rxjs/operators';
import { of } from 'rxjs';
import { Endereco } from '../../_shared/models/pessoa/endereco';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { AuthService } from '../../_shared/services/auth.service';
import { TecnicoService } from '../../_shared/services/tecnico.service';

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
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  
  //TODO: Tirar essa ´porra e colocar o EnumEstadosBrasil
  estados = [
    'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG',
    'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'
  ];
  
  cepInvalido = false;
  erroTimeout = false;

  constructor(
    private authService: AuthService,
    private tecnicoService: TecnicoService,
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
    }
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

  // TODO: Trocar pelo buscarCep do AuthService
  buscarCep(): void {
    const cep = this.endereco.cep?.toString().replace(/\D/g, '');
    
    if (cep?.length === 8) {
      this.http.get(`https://viacep.com.br/ws/${cep}/json/`).pipe(
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
      ).subscribe((data: any) => {
        if (data && !data.erro) {
          this.cepInvalido = false;
          this.erroTimeout = false;
          this.endereco.cep = parseInt(cep);
          this.endereco.logradouro = data.logradouro;
          this.endereco.complemento = data.complemento || '';
          this.endereco.bairro = data.bairro;
          this.endereco.municipio = data.localidade;
          this.endereco.uf = data.uf;
          this.form.endereco = this.endereco;
          this.cdRef.detectChanges();
        } else {
          this.cepInvalido = true;
        }
      });
    } else {
      this.cepInvalido = true;
    }
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