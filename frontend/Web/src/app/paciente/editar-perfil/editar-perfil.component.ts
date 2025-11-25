import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { catchError, timeout, finalize } from 'rxjs/operators';
import { of } from 'rxjs';
import { Endereco } from '../../_shared/models/pessoa/endereco';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';
import { Contato } from '../../_shared/models/pessoa/paciente/contato';
import { AuthService } from '../../_shared/services/auth.service';
import { PacienteService } from '../../_shared/services/paciente.service';
import { ImageService } from '../../_shared/services/image.service';
import { EnumEstadosBrasil } from '../../_shared/models/estadosbrasil.enum';
import { EnumEstadoCivil } from '../../_shared/models/estadocivil.enum';
import { EnumClasseSocioeconomica } from '../../_shared/models/classe-socioeconomica.enum';
import { EnumEscolaridade } from '../../_shared/models/escolaridade.enum';
import { EnumParentesco } from '../../_shared/models/parentesco.enum';

@Component({
  selector: 'app-editar-perfil',
  templateUrl: './editar-perfil.component.html',
  styleUrl: './editar-perfil.component.css'
})
export class EditarPerfilComponentPaciente implements OnInit {
  endereco: Endereco = new Endereco();
  form: Paciente = new Paciente();
  
  // Estados de loading e feedback
  isLoading = false;
  isSaving = false;
  
  // Estrutura consolidada de feedback
  public feedback = {
    upload: { success: '', error: '' },
    form: { success: false, error: '' }
  };

  currentPhotoUrl: string | null = null;
  
  formattedDataNasc: string = '';
  formattedDataExpedicao: string = '';
  estados = Object.values(EnumEstadosBrasil);
  estadosCivil = Object.values(EnumEstadoCivil);
  classesSocioeconomicas = Object.values(EnumClasseSocioeconomica);
  escolaridades = Object.values(EnumEscolaridade);
  parentescos = Object.values(EnumParentesco);

  cepInvalido = false;
  erroTimeout = false;

  // Gerenciamento de contatos
  novoContato: Contato = new Contato();
  editandoContatoIndex: number | null = null;

  constructor(
    private authService: AuthService,
    private pacienteService: PacienteService,
    private imageService: ImageService,
    private http: HttpClient,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadUserData();
  }

  getEscolaridadeOptions() {
    return Object.entries(EnumEscolaridade).map(([key, value]) => ({
      value: value,
      label: value
    }));
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

  loadUserData(): void {
    this.isLoading = true;

    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser instanceof Paciente) {
      // Shallow copy para evitar modificações no objeto original
      this.form = { ...currentUser };

      // Inicializa o endereço se não existir
      this.endereco = this.form.endereco ? { ...this.form.endereco } : new Endereco();

      // Formatar CEP se necessário
      if (this.endereco.cep) {
        this.endereco.cep = this.formatarCepSeNecessario(this.endereco.cep);
      }

      this.form.endereco = this.endereco;

      // Formatar datas para os inputs date
      if (this.form.dataNasc) {
        this.formattedDataNasc = this.formatDateForInput(this.form.dataNasc);
      }

      if (this.form.dataExpedicao) {
        this.formattedDataExpedicao = this.formatDateForInput(this.form.dataExpedicao);
      }

      // Formatar telefone e RG se necessário
      if (this.form.telefone) {
        this.form.telefone = this.formatarTelefoneSeNecessario(this.form.telefone);
      }

      if (this.form.rg) {
        this.form.rg = this.formatarRgSeNecessario(this.form.rg);
      }

      // Inicializar contatos se não existir
      if (!this.form.contatos) {
        this.form.contatos = [];
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
    return isNaN(d.getTime()) ? '' : d.toISOString().split('T')[0];
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

  private formatarRgSeNecessario(rg: string | undefined): string {
    if (!rg) return '';
    // Se já estiver formatado (com pontos e hífen)
    if (rg.includes('.') && rg.includes('-')) {
      return rg;
    }
    // Se for apenas números e tiver 9 dígitos
    const numbers = rg.replace(/\D/g, '');
    if (numbers.length === 9) {
      return `${numbers.substring(0,2)}.${numbers.substring(2,5)}.${numbers.substring(5,8)}-${numbers.substring(8)}`;
    }
    return rg;
  }

  private formatarCepSeNecessario(cep: string | number | undefined): string {
    if (!cep) return '';
    const cepStr = cep.toString().trim();
    // Se já estiver formatado (com hífen)
    if (cepStr.includes('-')) {
      return cepStr;
    }
    // Se for apenas números e tiver 8 dígitos
    const numbers = cepStr.replace(/\D/g, '');
    if (numbers.length === 8) {
      return `${numbers.substring(0,5)}-${numbers.substring(5)}`;
    }
    return cepStr;
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

  onDataExpedicaoChange(dateString: string): void {
    if (dateString) {
      this.form.dataExpedicao = new Date(dateString);
      this.formattedDataExpedicao = dateString;
    } else {
      this.form.dataExpedicao = undefined;
      this.formattedDataExpedicao = '';
    }
  }

  loadCurrentPhoto(pacienteId: number): void {
    // Assumindo que existe um método similar para paciente no ImageService
    this.imageService.getPacientePhoto(pacienteId).subscribe({
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
      this.feedback.upload.error = 'Erro: dados insuficientes para upload da imagem.';
      return;
    }

    this.clearUploadMessages();

    // Assumindo que existe um método similar para paciente no ImageService
    this.imageService.uploadPacientePhoto(this.form.id, base64Image).subscribe({
      next: () => {
        this.feedback.upload.success = 'Foto atualizada com sucesso!';
        this.currentPhotoUrl = base64Image;
        this.clearMessage('upload-success');
      },
      error: (error) => {
        this.feedback.upload.error = error.error?.error || 'Erro ao fazer upload da imagem.';
        this.clearMessage('upload-error', 5000);
      }
    });
  }

  onImageError(error: string): void {
    this.feedback.upload.error = error;
    this.clearMessage('upload-error', 5000);
  }

  private clearUploadMessages(): void {
    this.feedback.upload.error = '';
    this.feedback.upload.success = '';
  }

  private clearMessage(type: 'upload-success' | 'upload-error' | 'form-success', delay: number = 3000): void {
    setTimeout(() => {
      if (type === 'upload-success') {
        this.feedback.upload.success = '';
      } else if (type === 'upload-error') {
        this.feedback.upload.error = '';
      } else if (type === 'form-success') {
        this.feedback.form.success = false;
      }
      this.cdRef.detectChanges();
    }, delay);
  }

  private calcularIdade(dataNasc: Date): number {
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

  calcularIMC(): number {
    if (this.form.peso && this.form.altura) {
      return this.pacienteService.calculateIMC(this.form.peso, this.form.altura);
    }
    return 0;
  }

  buscarCep(): void {
    const cep = this.endereco.cep?.toString().replace(/\D/g, '') || '';

    const isValid = cep.length === 8;
    this.cepInvalido = !isValid;
    this.erroTimeout = false;

    if (!isValid) return;

    this.isSaving = true;

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
        }),
        finalize(() => this.isSaving = false)
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
    this.feedback.form.success = false;
    this.feedback.form.error = '';
  }

  private getValidationError(form: NgForm): string {
    // Verificar campos específicos inválidos
    const invalidFields: string[] = [];

    if (!form.valid) {
      Object.keys(form.controls).forEach(key => {
        const control = form.controls[key];
        if (control.invalid) {
          // Converter nome do campo para formato legível
          const fieldName = this.getFieldLabel(key);
          invalidFields.push(fieldName);
        }
      });
    }

    if (invalidFields.length > 0) {
      return `Campos obrigatórios faltando ou inválidos: ${invalidFields.join(', ')}`;
    }

    if (!this.form.dataNasc) return 'Data de nascimento é obrigatória.';
    if (!this.isEnderecoValid()) {
      const missingAddressFields: string[] = [];
      if (!this.endereco.cep) missingAddressFields.push('CEP');
      if (!this.endereco.logradouro) missingAddressFields.push('Logradouro');
      if (!this.endereco.numero) missingAddressFields.push('Número');
      if (!this.endereco.bairro) missingAddressFields.push('Bairro');
      if (!this.endereco.municipio) missingAddressFields.push('Município');
      if (!this.endereco.uf) missingAddressFields.push('UF');
      return `Endereço incompleto. Campos faltando: ${missingAddressFields.join(', ')}`;
    }

    return 'Erro de validação.';
  }

  private getFieldLabel(fieldName: string): string {
    const fieldLabels: { [key: string]: string } = {
      'nome': 'Nome',
      'telefone': 'Telefone',
      'sexo': 'Sexo',
      'dataNasc': 'Data de Nascimento',
      'escolaridade': 'Escolaridade',
      'corRaca': 'Cor/Raça',
      'cep': 'CEP',
      'logradouro': 'Logradouro',
      'numero': 'Número',
      'bairro': 'Bairro',
      'municipio': 'Município',
      'uf': 'UF'
    };
    return fieldLabels[fieldName] || fieldName;
  }

  onSubmit(form: NgForm): void {
    this.resetFormMessages();

    if (!form.valid || !this.form.dataNasc || !this.isEnderecoValid()) {
      this.feedback.form.error = this.getValidationError(form);
      return;
    }

    try {
      this.isSaving = true;

      const contatosPayload = this.form.contatos?.map(contato => ({
        nome: contato.nome,
        telefone: contato.telefone ? contato.telefone.replace(/\D/g, '') : '',
        parentesco: (Object.keys(EnumParentesco).find(key => 
          EnumParentesco[key as keyof typeof EnumParentesco] === contato.parentesco
        ) || contato.parentesco) as EnumParentesco
      })) || [];

      const pacienteAtualizado: any = {
        id: this.form.id,
        nome: this.form.nome,
        dataNasc: this.form.dataNasc,
        sexo: this.form.sexo,
        estadoCivil: this.form.estadoCivil,
        nacionalidade: this.form.nacionalidade,
        municipioNasc: this.form.municipioNasc,
        ufNasc: this.form.ufNasc,
        corRaca: this.form.corRaca,
        escolaridade: this.form.escolaridade,
        peso: this.form.peso,
        altura: this.form.altura,
        socioeconomico: this.form.socioeconomico,
        telefone: this.form.telefone ? this.form.telefone.replace(/\D/g, '') : '',
        rg: this.form.rg ? this.form.rg.replace(/\D/g, '') : '',
        cpf: this.form.cpf ? this.form.cpf.replace(/\D/g, '') : '',
        dataExpedicao: this.form.dataExpedicao,
        orgaoEmissor: this.form.orgaoEmissor,
        ufEmissor: this.form.ufEmissor,
        endereco: {
          cep: this.endereco.cep ? this.endereco.cep.toString().replace(/\D/g, '') : '',
          logradouro: this.endereco.logradouro,
          numero: this.endereco.numero,
          complemento: this.endereco.complemento || '',
          bairro: this.endereco.bairro,
          municipio: this.endereco.municipio,
          uf: this.endereco.uf
        },
        contatos: contatosPayload,
        // Campos calculados
        idade: this.calcularIdade(this.form.dataNasc),
        imc: this.calcularIMC()
      };
      
      // Remover propriedades que não devem ser enviadas
      delete pacienteAtualizado.fotoUrl;
      delete pacienteAtualizado.currentPhotoUrl;


      this.pacienteService.updatePacienteAndRefreshCache(pacienteAtualizado).subscribe({
        next: (updatedPaciente: Paciente) => {
          this.isSaving = false;
          this.feedback.form.success = true;
          
          this.form = { ...updatedPaciente };
          if (this.form.dataNasc) {
            this.formattedDataNasc = this.formatDateForInput(this.form.dataNasc);
          }
          if (this.form.dataExpedicao) {
            this.formattedDataExpedicao = this.formatDateForInput(this.form.dataExpedicao);
          }
          
          if (this.form.endereco) {
            this.endereco = { ...this.form.endereco };
          }
          
          console.log('Perfil atualizado e cache refreshed:', updatedPaciente);

          this.clearMessage('form-success', 5000);
        },
        error: (err) => {
          console.error('Erro ao atualizar perfil:', err);
          this.isSaving = false;
          this.feedback.form.error = err.error?.message || err.message || 'Erro ao atualizar perfil. Tente novamente.';

          setTimeout(() => {
            this.feedback.form.error = '';
            this.cdRef.detectChanges();
          }, 8000);
        }
      });
    } catch (error) {
      console.error('Erro no processamento do formulário:', error);
      this.isSaving = false;
      this.feedback.form.error = 'Erro interno. Tente novamente.';
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

  // ============================================
  // MÉTODOS DE GERENCIAMENTO DE CONTATOS
  // ============================================

  adicionarContato(): void {
    // Validar se todos os campos do contato estão preenchidos
    if (!this.novoContato.nome || !this.novoContato.telefone || !this.novoContato.parentesco) {
      this.feedback.form.error = 'Preencha todos os campos do contato (Nome, Telefone e Parentesco).';
      setTimeout(() => {
        this.feedback.form.error = '';
        this.cdRef.detectChanges();
      }, 3000);
      return;
    }

    if (!this.form.contatos) {
      this.form.contatos = [];
    }

    // Se estiver editando, atualizar o contato existente
    if (this.editandoContatoIndex !== null) {
      this.form.contatos[this.editandoContatoIndex] = { ...this.novoContato };
      this.editandoContatoIndex = null;
    } else {
      // Adicionar novo contato
      this.form.contatos.push({ ...this.novoContato });
    }

    // Limpar o formulário de contato
    this.limparFormularioContato();
  }

  editarContato(index: number): void {
    if (this.form.contatos && this.form.contatos[index]) {
      this.novoContato = { ...this.form.contatos[index] };
      this.editandoContatoIndex = index;
    }
  }

  removerContato(index: number): void {
    if (this.form.contatos && this.form.contatos[index]) {
      this.form.contatos.splice(index, 1);

      // Se estava editando este contato, cancelar a edição
      if (this.editandoContatoIndex === index) {
        this.cancelarEdicaoContato();
      }
    }
  }

  cancelarEdicaoContato(): void {
    this.limparFormularioContato();
    this.editandoContatoIndex = null;
  }

  private limparFormularioContato(): void {
    this.novoContato = new Contato();
  }

  isContatoFormValid(): boolean {
    return !!(this.novoContato.nome && this.novoContato.telefone && this.novoContato.parentesco);
  }
}