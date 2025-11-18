import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { Endereco } from '../../_shared/models/pessoa/endereco';
import { TecnicoService } from '../../_shared/services/tecnico.service';
import { AuthService } from '../../_shared/services/auth.service';
import { EnumEstadosBrasil } from '../../_shared/models/estadosbrasil.enum';

@Component({
  selector: 'app-cadastrar-tecnico',
  templateUrl: './cadastrar-tecnico.component.html',
  styleUrls: ['./cadastrar-tecnico.component.css']
})
export class CadastrarTecnicoComponent implements OnInit {
  tecnico = new Tecnico();
  endereco = new Endereco();
  isRegistered = false;
  isRegistrationFailed = false;
  errorMessage = '';
  estados = Object.values(EnumEstadosBrasil);
  cepInvalido = false;
  erroTimeout = false;
  repetirSenha = '';
  loading = false;

  constructor(
    private tecnicoService: TecnicoService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.tecnico.ativo = true;
  }

  onImageSelected(base64Image: string): void {
    if (base64Image) {
      this.tecnico.fotoUrl = base64Image;
    }
  }

  onImageError(error: string): void {
    console.error('Erro no upload da imagem:', error);
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
    this.authService.buscarCep(this.endereco.cep?.toString() || '')
      .subscribe({
        next: (endereco) => {
          if (endereco) {
            this.cepInvalido = false;
            this.erroTimeout = false;
            this.endereco = endereco;
            this.tecnico.endereco = this.endereco;
          } else {
            this.cepInvalido = true;
          }
        },
        error: () => {
          this.cepInvalido = true;
        }
      });
  }

  onSubmit(form: NgForm): void {
    if (form.valid && this.tecnico.senha === this.repetirSenha) {
      this.loading = true;
      this.tecnico.idade = this.calcularIdade(this.tecnico.dataNasc!);
      this.tecnico.endereco = this.endereco;
      this.tecnico.ativo = true;

      this.tecnicoService.createTecnico(this.tecnico)
        .subscribe({
          next: () => {
            this.isRegistered = true;
            this.isRegistrationFailed = false;
            this.loading = false;
            setTimeout(() => {
              this.router.navigate(['/tecnico/gerenciar-tecnicos']);
            }, 2000);
          },
          error: (err) => {
            this.errorMessage = err.error?.message || 'Erro ao cadastrar técnico';
            this.isRegistrationFailed = true;
            this.loading = false;
          }
        });
    } else if (this.tecnico.senha !== this.repetirSenha) {
      this.errorMessage = 'As senhas não coincidem!';
      this.isRegistrationFailed = true;
    }
  }

  cancelar(): void {
    this.router.navigate(['/tecnico/gerenciar-tecnicos']);
  }
}
