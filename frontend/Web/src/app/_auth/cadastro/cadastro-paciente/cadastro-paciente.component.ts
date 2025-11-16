import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Paciente } from '../../../_shared/models/pessoa/paciente/paciente';
import { Endereco } from '../../../_shared/models/pessoa/endereco';
import { EnumEstadosBrasil } from '../../../_shared/models/estadosbrasil.enum';
import { EnumEstadoCivil } from '../../../_shared/models/estadocivil.enum';
import { EnumClasseSocioeconomica } from '../../../_shared/models/classe-socioeconomica.enum';
import { EnumEscolaridade } from '../../../_shared/models/escolaridade.enum';
import { AuthService } from '../../../_shared/services/auth.service';
import { ImageService } from '../../../_shared/services/image.service';

@Component({
  selector: 'app-cadastro-paciente',
  templateUrl: './cadastro-paciente.component.html',
  styleUrls: ['./cadastro-paciente.component.css']
})
export class CadastroPacienteComponent implements OnInit {
  paciente = new Paciente();
  endereco = new Endereco();
  isRegistered = false;
  isRegistrationFailed = false;
  errorMessage = '';
  estados = Object.values(EnumEstadosBrasil);
  estadosCivis = Object.values(EnumEstadoCivil);
  classesSocioeconomicas = Object.values(EnumClasseSocioeconomica);
  escolaridades = Object.values(EnumEscolaridade);
  cepInvalido = false;
  erroTimeout = false;
  repetirSenha = '';
  isSenhaVisible: boolean = false;
  isRepetirSenhaVisible: boolean = false;

  constructor(
    private authService: AuthService,
    private imageService: ImageService
  ) { }

  ngOnInit(): void { 
  }

  onImageSelected(base64Image: string): void {
    if (base64Image) {
      this.paciente.fotoUrl = base64Image;
    }
  }

  onImageError(error: string): void {
    console.error('Erro no upload da imagem:', error);
  }

  getEstadoCivilOptions() {
    return Object.entries(EnumEstadoCivil).map(([key, value]) => ({
      value: value,
      label: value
    }));
  }

  getEscolaridadeOptions() {
    return Object.entries(EnumEscolaridade).map(([key, value]) => ({
      value: value,
      label: value
    }));
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

  calcularIMC(peso: number, altura: number): number {
    if (peso && altura && altura > 0) {
      return parseFloat((peso / (altura * altura)).toFixed(2));
    }
    return 0;
  }

  buscarCep(): void {
    this.authService.buscarCep(this.endereco.cep?.toString() || '')
      .subscribe({
        next: (endereco) => {
          if (endereco) {
            this.cepInvalido = false;
            this.erroTimeout = false;
            this.endereco = endereco;
            this.paciente.endereco = this.endereco;
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
    if (form.valid && this.paciente.senha === this.repetirSenha) {
      // Calcular idade e IMC antes de enviar
      if (this.paciente.dataNasc) {
        this.paciente.idade = this.calcularIdade(this.paciente.dataNasc);
      }
      
      if (this.paciente.peso && this.paciente.altura) {
        this.paciente.imc = this.calcularIMC(this.paciente.peso, this.paciente.altura);
      }

      this.paciente.endereco = this.endereco;

      this.authService.registrarPaciente(this.paciente)
        .subscribe({
          next: () => {
            this.isRegistered = true;
            this.isRegistrationFailed = false;
            form.reset();
          },
          error: (err) => {
            this.errorMessage = err.error?.message || 'Erro ao registrar paciente';
            this.isRegistrationFailed = true;
          }
        });
    } else if (this.paciente.senha !== this.repetirSenha) {
      console.error('As senhas não coincidem!');
    }
  }

  getCorRacaOptions() {
    return [
      { value: 'Branca', label: 'Branca' },
      { value: 'Preta', label: 'Preta' },
      { value: 'Parda', label: 'Parda' },
      { value: 'Amarela', label: 'Amarela' },
      { value: 'Indígena', label: 'Indígena' }
    ];
  }

  toggleSenhaVisibility(): void {
    this.isSenhaVisible = !this.isSenhaVisible;
  }

  toggleRepetirSenhaVisibility(): void {
    this.isRepetirSenhaVisible = !this.isRepetirSenhaVisible;
  }
}