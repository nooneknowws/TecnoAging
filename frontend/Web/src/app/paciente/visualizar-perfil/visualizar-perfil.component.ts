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
  showDadosPessoais: boolean = false;
  showContatoForm: boolean = false;
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
      this.pacienteService.getPacienteById(pacienteId).subscribe((data) => {
        this.paciente = data;
        this.loadCurrentPhoto(pacienteId);
      });
    } else {
      console.error('Paciente não encontrado ou não logado');
    }
  }

  loadCurrentPhoto(pacienteId: number): void {
    this.imageService.getPacientePhoto(pacienteId).subscribe({
      next: (response) => {
        this.currentPhotoUrl = response.image;
      },
      error: () => {
        this.currentPhotoUrl = null;
      }
    });
  }

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

  onImageError(error: string): void {
    this.uploadError = error;
    setTimeout(() => this.uploadError = '', 5000);
  }

  toggleDadosPessoais(): void {
    this.showDadosPessoais = !this.showDadosPessoais;
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