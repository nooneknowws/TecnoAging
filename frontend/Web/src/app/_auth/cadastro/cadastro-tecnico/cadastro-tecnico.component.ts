import { Component, OnInit } from '@angular/core';
import { Tecnico } from '../../../_shared/models/pessoa/tecnico/tecnico';
import { Endereco } from '../../../_shared/models/pessoa/endereco';
import { AuthService } from '../../../_shared/services/auth.service';
import { EnumEstadosBrasil } from '../../../_shared/models/estadosbrasil.enum';
import { NgForm } from '@angular/forms';
import { ImageService } from '../../../_shared/services/image.service';

@Component({
  selector: 'app-cadastro-tecnico',
  templateUrl: './cadastro-tecnico.component.html',
  styleUrl: './cadastro-tecnico.component.css'
})
export class CadastroTecnicoComponent implements OnInit {
  tecnico = new Tecnico();
  endereco = new Endereco();
  isRegistered = false;
  isRegistrationFailed = false;
  errorMessage = '';
  estados = Object.values(EnumEstadosBrasil);
  cepInvalido = false;
  erroTimeout = false;
  repetirSenha = '';

  constructor(
    private authService: AuthService,
    private imageService: ImageService
  ) { }

  ngOnInit(): void { }

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
      this.tecnico.idade = this.calcularIdade(this.tecnico.dataNasc!);
      this.tecnico.endereco = this.endereco;

      this.authService.registrarTecnico(this.tecnico)
        .subscribe({
          next: () => {
            this.isRegistered = true;
            this.isRegistrationFailed = false;
            form.reset();
          },
          error: (err) => {
            this.errorMessage = err.error?.message || 'Erro ao registrar técnico';
            this.isRegistrationFailed = true;
          }
        });
    } else if (this.tecnico.senha !== this.repetirSenha) {
      console.error('As senhas não coincidem!');
    }
  }
}