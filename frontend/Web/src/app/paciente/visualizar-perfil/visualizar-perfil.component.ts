import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PacienteService } from '../../_shared/services/paciente.service';
import { ImageService } from '../../_shared/services/image.service';
import { AuthService } from '../../_shared/services/auth.service';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';

@Component({
  selector: 'app-visualizar-perfil',
  templateUrl: './visualizar-perfil.component.html',
  styleUrl: './visualizar-perfil.component.css'
})
export class VisualizarPerfilComponent implements OnInit {
  paciente: Paciente | null = null;
  
  // Controles de visibilidade das seções
  showDadosPessoais: boolean = true; // Começar expandido
  showEndereco: boolean = false;
  showDadosMedicos: boolean = false;
  showContatos: boolean = false;
  
  // Upload de imagem
  currentPhotoUrl: string | null = null;
  uploadError: string = '';
  uploadSuccess: string = '';

  // Mensagens de contato
  contatoError: string = '';
  contatoSuccess: string = '';

  constructor(
    private pacienteService: PacienteService,
    private imageService: ImageService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser instanceof Paciente && currentUser.id) {
      const pacienteId = currentUser.id;
      this.loadPaciente(pacienteId);
    } else {
      console.error('Paciente não encontrado ou não logado');
    }
  }

  /**
   * Carrega os dados completos do paciente
   */
  private loadPaciente(pacienteId: number): void {
    this.pacienteService.getPacienteById(pacienteId).subscribe({
      next: (data) => {
        this.paciente = data;
        this.loadCurrentPhoto(pacienteId);
      },
      error: (error) => {
        console.error('Erro ao carregar dados do paciente:', error);
      }
    });
  }

  /**
   * Carrega a foto atual do paciente
   */
  private loadCurrentPhoto(pacienteId: number): void {
    this.imageService.getPacientePhoto(pacienteId).subscribe({
      next: (response) => {
        this.currentPhotoUrl = response.image;
      },
      error: () => {
        this.currentPhotoUrl = null;
      }
    });
  }

  /**
   * Manipula o upload de nova imagem
   */
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

  /**
   * Manipula erros de imagem
   */
  onImageError(error: string): void {
    this.uploadError = error;
    setTimeout(() => this.uploadError = '', 5000);
  }

  // Métodos de controle de visibilidade das seções
  toggleDadosPessoais(): void {
    this.showDadosPessoais = !this.showDadosPessoais;
  }

  toggleEndereco(): void {
    this.showEndereco = !this.showEndereco;
  }

  toggleDadosMedicos(): void {
    this.showDadosMedicos = !this.showDadosMedicos;
  }

  toggleContatos(): void {
    this.showContatos = !this.showContatos;
  }

  /**
   * Retorna o endereço completo formatado
   */
  getEnderecoCompleto(): string {
    if (!this.paciente?.endereco) {
      return 'Endereço não cadastrado';
    }

    const endereco = this.paciente.endereco;
    const partes = [
      endereco.logradouro,
      endereco.numero ? `nº ${endereco.numero}` : 'S/N',
      endereco.bairro,
      endereco.municipio,
      endereco.uf,
      endereco.cep ? `CEP: ${endereco.cep}` : null
    ].filter(parte => parte); // Remove valores falsy

    return partes.join(', ');
  }

  /**
   * Calcula o IMC quando não estiver salvo
   */
  calculateIMC(): number {
    if (!this.paciente?.peso || !this.paciente?.altura) {
      return 0;
    }

    const alturaMetros = this.paciente.altura / 100;
    return this.paciente.peso / (alturaMetros * alturaMetros);
  }

  /**
   * Expande/recolhe todas as seções de uma vez
   */
  toggleAllSections(): void {
    const newState = !(this.showDadosPessoais && this.showEndereco && 
                      this.showDadosMedicos && this.showContatos);
    
    this.showDadosPessoais = newState;
    this.showEndereco = newState;
    this.showDadosMedicos = newState;
    this.showContatos = newState;
  }

  /**
   * Verifica se o paciente tem dados médicos completos
   */
  hasCompleteMedicalData(): boolean {
    return !!(this.paciente?.peso && this.paciente?.altura);
  }

  /**
   * Retorna a classificação do IMC
   */
  getIMCClassification(): string {
    const imc = this.paciente?.imc || this.calculateIMC();
    
    if (imc < 18.5) return 'Abaixo do peso';
    if (imc < 25) return 'Peso normal';
    if (imc < 30) return 'Sobrepeso';
    if (imc < 35) return 'Obesidade grau I';
    if (imc < 40) return 'Obesidade grau II';
    return 'Obesidade grau III';
  }

  /**
   * Formata telefone para exibição
   */
  formatTelefone(telefone: string): string {
    if (!telefone) return '';

    // Remove todos os caracteres não numéricos
    const numeros = telefone.replace(/\D/g, '');

    // Se tiver mais de 11 dígitos, pegar apenas os primeiros 11
    const numerosValidos = numeros.length > 11 ? numeros.slice(0, 11) : numeros;

    // Aplica máscara baseado no tamanho
    if (numerosValidos.length === 11) {
      return `(${numerosValidos.slice(0, 2)}) ${numerosValidos.slice(2, 7)}-${numerosValidos.slice(7)}`;
    } else if (numerosValidos.length === 10) {
      return `(${numerosValidos.slice(0, 2)}) ${numerosValidos.slice(2, 6)}-${numerosValidos.slice(6)}`;
    }

    // Se o formato for inválido, retorna vazio ou os números como estão
    return numerosValidos || telefone;
  }

  /**
   * Formata CPF para exibição
   */
  formatCPF(cpf: string): string {
    if (!cpf) return '';
    
    const numeros = cpf.replace(/\D/g, '');
    
    if (numeros.length === 11) {
      return `${numeros.slice(0, 3)}.${numeros.slice(3, 6)}.${numeros.slice(6, 9)}-${numeros.slice(9)}`;
    }
    
    return cpf;
  }

  hasContato(): boolean | undefined {
    return this.paciente?.contatos && this.paciente.contatos.length > 0;
  }

  getIdade(): number {
    if (!this.paciente?.dataNasc) return 0;
    
    const hoje = new Date();
    const nascimento = new Date(this.paciente.dataNasc);
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const mes = hoje.getMonth() - nascimento.getMonth();
    
    if (mes < 0 || (mes === 0 && hoje.getDate() < nascimento.getDate())) {
      idade--;
    }
    
    return idade;
  }

  formatarData(data: Date | undefined): string {
    if (!data) return 'Não informado';
    return new Date(data).toLocaleDateString('pt-BR');
  }

  formatarEndereco(): string {
    const endereco = this.paciente?.endereco;
    if (!endereco) return 'Não informado';

    return `${endereco.logradouro || ''}, ${endereco.numero || ''} - ${endereco.bairro || ''}, ${endereco.municipio || ''} - ${endereco.uf || ''}, CEP: ${endereco.cep || ''}`;
  }

  /**
   * Navega para a tela de editar perfil
   */
  editarPerfil(): void {
    this.router.navigate(['/paciente/editar-perfil']);
  }
}