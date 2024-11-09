import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { catchError, timeout } from 'rxjs/operators';
import { of } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { Endereco } from '../../_shared/models/pessoa/endereco';
import { AuthService } from '../../_shared/services/auth.service';

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrl: './cadastro.component.css'
})
export class CadastroComponent implements OnInit {
  endereco: Endereco = new Endereco();
  form: Tecnico;
  isRegistered = false;
  isRegistrationFailed = false;
  errorMessage = '';
  estados = [
    'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG',
    'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'
  ];
  cepInvalido = false;
  erroTimeout = false;

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private cdRef: ChangeDetectorRef
  ) {
    this.form = new Tecnico(
      undefined, // matricula
      true, // ativo por padrão
      undefined, // id
      undefined, // nome
      undefined, // sexo
      undefined, // idade
      this.endereco, // endereco
      undefined, // dataNasc
      undefined, // cpf
      undefined  // telefone
    );
  }

  ngOnInit(): void { }

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
    const cep = this.endereco.CEP?.toString().replace(/\D/g, '');
   
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
        if (data.erro != 'true') {
          this.cepInvalido = false;
          this.erroTimeout = false;
          this.endereco.CEP = parseInt(cep);
          this.endereco.logradouro = data.logradouro;
          this.endereco.complemento = data.complemento || '';
          this.endereco.bairro = data.bairro;
          this.endereco.municipio = data.localidade;
          this.endereco.UF = data.uf;
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

  onSubmit(form: NgForm): void {
    if (form.valid) {
      // Calcular idade baseado na data de nascimento
      this.form.idade = this.calcularIdade(this.form.dataNasc!);
      this.form.endereco = this.endereco;
     
      this.http.post(`${AppComponent.API_URL}/tecnicos`, this.form).subscribe({
        next: (response: any) => {
          this.isRegistered = true;
          this.isRegistrationFailed = false;
          form.reset();
        },
        error: err => {
          this.errorMessage = err.error?.message || 'Erro ao registrar técnico';
          this.isRegistrationFailed = true;
        }
      });
    }
  }
}