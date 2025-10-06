import { Component, OnInit } from '@angular/core';
import { PacienteService } from '../../_shared/services/paciente.service';
import { ImageService } from '../../_shared/services/image.service';
import { AuthService } from '../../_shared/services/auth.service';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';
import { Contato } from '../../_shared/models/pessoa/paciente/contato';
import { EnumParentesco } from '../../_shared/models/parentesco.enum';

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
  showContatoForm: boolean = false;
  
  // Upload de imagem
  currentPhotoUrl: string | null = null;
  uploadError: string = '';
  uploadSuccess: string = '';
  
  // Novo contato
  novoContato: Contato = new Contato();
  contatoError: string = '';
  contatoSuccess: string = '';
  
  // Enum para o template
  parentescoOptions = Object.values(EnumParentesco);

  constructor(
    private pacienteService: PacienteService,
    private imageService: ImageService,
    private authService: AuthService
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

  toggleContatoForm(): void {
    if (this.hasContato()) {
      return; // Não permite abrir o formulário se já tem contato
    }
    this.showContatoForm = !this.showContatoForm;
    if (!this.showContatoForm) {
      this.resetContatoForm();
    }
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
    
    // Aplica máscara baseado no tamanho
    if (numeros.length === 11) {
      return `(${numeros.slice(0, 2)}) ${numeros.slice(2, 7)}-${numeros.slice(7)}`;
    } else if (numeros.length === 10) {
      return `(${numeros.slice(0, 2)}) ${numeros.slice(2, 6)}-${numeros.slice(6)}`;
    }
    
    return telefone;
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

  salvarContato(): void {
    if (!this.paciente?.id || this.hasContato()) {
      return;
    }

    if (!this.novoContato.nome || !this.novoContato.telefone || !this.novoContato.parentesco) {
      this.contatoError = 'Todos os campos são obrigatórios.';
      setTimeout(() => this.contatoError = '', 5000);
      return;
    }

    // Adiciona o contato ao paciente
    if (!this.paciente.contatos) {
      this.paciente.contatos = [];
    }
    
    this.paciente.contatos.push({ ...this.novoContato });

    // Atualiza o paciente no backend
    this.pacienteService.updatePaciente(this.paciente).subscribe({
      next: (updatedPaciente) => {
        this.paciente = updatedPaciente;
        this.contatoSuccess = 'Contato adicionado com sucesso!';
        this.showContatoForm = false;
        this.resetContatoForm();
        setTimeout(() => this.contatoSuccess = '', 3000);
      },
      error: (error) => {
        this.contatoError = 'Erro ao salvar contato. Tente novamente.';
        setTimeout(() => this.contatoError = '', 5000);
      }
    });
  }

  cancelarContato(): void {
    this.showContatoForm = false;
    this.resetContatoForm();
  }

  private resetContatoForm(): void {
    this.novoContato = new Contato();
    this.contatoError = '';
    this.contatoSuccess = '';
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
}