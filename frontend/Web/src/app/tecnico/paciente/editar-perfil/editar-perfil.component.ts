import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Paciente } from '../../../_shared/models/pessoa/paciente/paciente';
import { PacienteService } from '../../../_shared/services/paciente.service';
import { Contato } from '../../../_shared/models/pessoa/paciente/contato';
import { EnumEstadosBrasil } from '../../../_shared/models/estadosbrasil.enum';
import { EnumEstadoCivil } from '../../../_shared/models/estadocivil.enum';
import { EnumEscolaridade } from '../../../_shared/models/escolaridade.enum';
import { EnumClasseSocioeconomica } from '../../../_shared/models/classe-socioeconomica.enum';
import { AuthService } from '../../../_shared/services/auth.service';
import { ImageService } from '../../../_shared/services/image.service';
import { finalize, of, switchMap } from 'rxjs';
import { EnumParentesco } from '../../../_shared/models/parentesco.enum';

@Component({
  selector: 'app-editar-paciente',
  templateUrl: './editar-perfil.component.html',
  styleUrls: ['./editar-perfil.component.css']
})
export class EditarPerfilPacienteComponent implements OnInit {
  pacienteForm: FormGroup;
  paciente?: Paciente;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private pacienteService: PacienteService,
    private router: Router,
    private route: ActivatedRoute,
    private imageService: ImageService
  ) {
    this.pacienteForm = this.inicializarFormulario();
  }

  ngOnInit() {
    this.loading = true;
    this.route.queryParams.pipe(
      switchMap(params => {
        const id = params['id'];
        if (id) {
          return this.pacienteService.getPacienteById(id);
        }
        return of(null);
      }),
      finalize(() => this.loading = false)
    ).subscribe({
      next: (paciente) => {
        if (paciente) {
          this.carregarPaciente(paciente.id!);
        } else {
          console.error('Paciente não encontrado');
          this.router.navigate(['/tecnico']);
        }
      },
      error: (error) => {
        console.error('Erro ao carregar paciente:', error);
        this.router.navigate(['/tecnico']);
      }
    });
  }

  // Métodos para obter opções dos enums
  getEstadoCivilOptions() {
    return Object.entries(EnumEstadoCivil).map(([key, value]) => ({
      value: value,
      label: value
    }));
  }

  getEstadosOptions() {
    return Object.entries(EnumEstadosBrasil).map(([key, value]) => ({
      value: value,
      label: value
    }));
  }

  getParentescoOptions() {
    return Object.entries(EnumParentesco).map(([key, value]) => ({
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

  getClasseSocioeconomicaOptions() {
    return Object.entries(EnumClasseSocioeconomica).map(([key, value]) => ({
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

  private inicializarFormulario(): FormGroup {
    return this.fb.group({
      dadosPessoais: this.fb.group({
        nome: ['', Validators.required],
        dataNasc: ['', [Validators.required, this.validarData]],
        sexo: ['', Validators.required],
        estadoCivil: ['', Validators.required],
        nacionalidade: ['Brasileiro', Validators.required],
        municipioNasc: ['', Validators.required],
        ufNasc: ['', Validators.required],
        corRaca: ['', Validators.required],
        escolaridade: ['', Validators.required],
        peso: ['', Validators.required],
        altura: ['', Validators.required],
        socioeconomico: ['', Validators.required],
        telefone: ['', Validators.required],
        idade: [{ value: '', disabled: true }],
        imc: [{ value: '', disabled: true }]
      }),
      documentacao: this.fb.group({
        rg: ['', Validators.required],
        cpf: ['', [Validators.required, Validators.pattern(/^\d{3}\.\d{3}\.\d{3}\-\d{2}$/)]],
        dataExpedicao: ['', [Validators.required, this.validarData]],
        orgaoEmissor: ['', Validators.required],
        ufEmissor: ['', Validators.required]
      }),
      endereco: this.fb.group({
        cep: ['', [Validators.required, Validators.pattern(/^\d{5}\-\d{3}$/)]],
        logradouro: ['', Validators.required],
        numero: ['', Validators.required],
        complemento: [''],
        bairro: ['', Validators.required],
        municipio: ['', Validators.required],
        uf: ['', Validators.required]
      }),
      contatos: this.fb.array([])
    });
  }

  private preencherFormulario(paciente: Paciente) {
  if (!paciente) return;

  console.log('Dados do paciente recebidos:', paciente);
  console.log('Contatos do paciente:', paciente.contatos);
  console.log('Quantidade de contatos:', paciente.contatos?.length || 0);

  const dadosPessoais = this.pacienteForm.get('dadosPessoais');
  const documentacao = this.pacienteForm.get('documentacao');
  const endereco = this.pacienteForm.get('endereco');

  if (dadosPessoais && documentacao && endereco) {
    dadosPessoais.patchValue({
      nome: paciente.nome || '',
      dataNasc: this.formatarData(paciente.dataNasc),
      sexo: paciente.sexo || '',
      estadoCivil: paciente.estadoCivil || '',
      nacionalidade: paciente.nacionalidade || 'Brasileiro',
      municipioNasc: paciente.municipioNasc || '',
      ufNasc: paciente.ufNasc || '',
      corRaca: paciente.corRaca || '',
      escolaridade: paciente.escolaridade || '',
      peso: paciente.peso || '',
      altura: paciente.altura || '',
      socioeconomico: paciente.socioeconomico || '',
      telefone: this.formatarTelefoneSeNecessario(paciente.telefone),
      idade: paciente.idade || '',
      imc: paciente.imc || ''
    });

    documentacao.patchValue({
      rg: this.formatarRgSeNecessario(paciente.rg),
      cpf: this.formatarCpfSeNecessario(paciente.cpf),
      dataExpedicao: this.formatarData(paciente.dataExpedicao),
      orgaoEmissor: paciente.orgaoEmissor || '',
      ufEmissor: paciente.ufEmissor || ''
    });

    if (paciente.endereco) {
      endereco.patchValue({
        cep: this.formatarCepSeNecessario(paciente.endereco.cep),
        logradouro: paciente.endereco.logradouro || '',
        numero: paciente.endereco.numero || '',
        complemento: paciente.endereco.complemento || '',
        bairro: paciente.endereco.bairro || '',
        municipio: paciente.endereco.municipio || '',
        uf: paciente.endereco.uf || ''
      });
    }

    // Limpa e recria os contatos
    this.contatosFormArray.clear();
    paciente.contatos?.forEach(contato => {
      this.adicionarContato({
        nome: contato.nome,
        telefone: this.formatarTelefoneSeNecessario(contato.telefone),
        // CORREÇÃO: Usar EnumParentesco[chave] para converter PAI -> Pai
        parentesco: EnumParentesco[contato.parentesco as unknown as keyof typeof EnumParentesco] || contato.parentesco
      });
    });
  }

  console.log('Estado do formulário após preencher:');
  console.log('Valid:', this.pacienteForm.valid);
  console.log('Errors:', this.getFormValidationErrors());
}

  private getFormValidationErrors() {
    const errors: any = {};
    Object.keys(this.pacienteForm.controls).forEach(key => {
      const control = this.pacienteForm.get(key);
      if (control instanceof FormGroup) {
        Object.keys(control.controls).forEach(k => {
          const ctrl = control.get(k);
          if (ctrl?.errors) {
            errors[`${key}.${k}`] = ctrl.errors;
          }
        });
      } else if (control instanceof FormArray) {
        control.controls.forEach((ctrl, index) => {
          if (ctrl instanceof FormGroup) {
            Object.keys(ctrl.controls).forEach(k => {
              const c = ctrl.get(k);
              if (c?.errors) {
                errors[`${key}[${index}].${k}`] = c.errors;
              }
            });
          }
        });
      } else if (control?.errors) {
        errors[key] = control.errors;
      }
    });
    return errors;
  }

  // Métodos auxiliares para formatação
  private formatarCpfSeNecessario(cpf: string | undefined): string {
    if (!cpf) return '';
    // Se já estiver formatado, retorna como está
    if (cpf.includes('.') && cpf.includes('-')) return cpf;
    // Se for apenas números e tiver 11 dígitos
    if (/^\d{11}$/.test(cpf)) {
      return `${cpf.substring(0,3)}.${cpf.substring(3,6)}.${cpf.substring(6,9)}-${cpf.substring(9)}`;
    }
    return cpf;
  }

  private formatarCepSeNecessario(cep: string | number | undefined): string {
    if (!cep) return '';
    const cepStr = cep.toString().trim();
    const hasHyphen = cepStr.includes('-');
    
    if (hasHyphen) {
      return cepStr;
    }
    
    const cleaned = cepStr.replace(/\D/g, '');
    if (cleaned.length === 8) {
      return `${cleaned.slice(0, 5)}-${cleaned.slice(5)}`;
    }
    return cepStr;
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

  private formatarTelefoneParaExibicao(telefone: string | undefined): string {
    if (!telefone || telefone.length !== 11) return telefone || '';
    return `(${telefone.slice(0,2)}) ${telefone.slice(2,7)}-${telefone.slice(7)}`;
  }

  private formatarData(data: Date | undefined): string {
    if (!data) return '';
    const d = new Date(data);
    return d.toISOString().split('T')[0];
  }

  buscarCep(cep: string) {
    if (!cep) return;

    this.loading = true;
    const enderecoGroup = this.pacienteForm.get('endereco');

    this.authService.buscarCep(cep)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (endereco) => {
          if (endereco && enderecoGroup) {
            enderecoGroup.patchValue({
              logradouro: endereco.logradouro,
              bairro: endereco.bairro,
              municipio: endereco.municipio,
              uf: endereco.uf,
              cep: endereco.cep
            });
          }
        },
        error: (error) => {
          console.error('Erro ao buscar CEP:', error);
          alert('Erro ao buscar CEP. Por favor, verifique o CEP informado.');
        }
      });
  }

  get contatosFormArray() {
    return this.pacienteForm.get('contatos') as FormArray;
  }

  private carregarPaciente(id: number) {
    this.pacienteService.getPacienteById(id).subscribe({
      next: (paciente) => {
        this.paciente = paciente;
        this.carregarFotoPaciente(id);
        this.preencherFormulario(paciente);
      },
      error: (error) => console.error('Erro ao carregar paciente:', error)
    });
  }

  private carregarFotoPaciente(pacienteId: number) {
    this.imageService.getPacientePhoto(pacienteId).subscribe({
      next: (response) => {
        if (response && response.image && this.paciente) {
          this.paciente.fotoUrl = response.image;
        }
      },
      error: (error) => {
        console.log(`Foto não encontrada para paciente ${pacienteId}`);
      }
    });
  }

  private marcarCamposInvalidos() {
    const form = this.pacienteForm;
    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      if (control instanceof FormGroup) {
        Object.keys(control.controls).forEach(k => {
          const ctrl = control.get(k);
          if (ctrl?.invalid) {
            ctrl.markAsTouched();
          }
        });
      } else if (control instanceof FormArray) {
        control.controls.forEach(ctrl => {
          if (ctrl instanceof FormGroup) {
            Object.keys(ctrl.controls).forEach(k => {
              const c = ctrl.get(k);
              if (c?.invalid) {
                c.markAsTouched();
              }
            });
          }
        });
      }
    });
  }

  adicionarContato(contato?: Contato) {
  // Formatação/normalização do telefone (opcional)
  let telefoneFormatado = '';
  if (contato?.telefone) {
    const digits = contato.telefone.replace(/\D/g, '');
    const digitosValidos = digits.length > 11 ? digits.slice(0,11) : digits;
    if (digitosValidos.length === 11) {
      telefoneFormatado = `(${digitosValidos.slice(0,2)}) ${digitosValidos.slice(2,7)}-${digitosValidos.slice(7)}`;
    } else if (digitosValidos.length === 10) {
      telefoneFormatado = `(${digitosValidos.slice(0,2)}) ${digitosValidos.slice(2,6)}-${digitosValidos.slice(6)}`;
    } else {
      telefoneFormatado = '';
    }
  }

  // CRIAÇÃO DO FORM DO CONTATO SEM VALIDADORES
  const contatoForm = this.fb.group({
    nome: [contato?.nome || ''],            // sem Validators
    telefone: [telefoneFormatado || ''],    // sem Validators
    parentesco: [contato?.parentesco || ''] // sem Validators
  });

  // Garantir que não apareça como "tocado" ou "sujo"
  contatoForm.markAsPristine();
  contatoForm.markAsUntouched();
  contatoForm.get('telefone')?.markAsPristine();
  contatoForm.get('telefone')?.markAsUntouched();

  // Forçar sem validadores (se por acaso houverem herdados)
  contatoForm.get('telefone')?.setValidators([]);
  contatoForm.get('telefone')?.updateValueAndValidity({ emitEvent: false });

  this.contatosFormArray.push(contatoForm);
}

  formatarTelefone(event: any, index: number) {
    let telefone = event.target.value.replace(/\D/g, '');
    if (telefone.length === 11) {
      telefone = `(${telefone.slice(0,2)}) ${telefone.slice(2,7)}-${telefone.slice(7)}`;
      const contatoControl = this.contatosFormArray.at(index);
      contatoControl.patchValue({ telefone }, { emitEvent: false });
    }
  }

  private formatarCpf(cpf: string | undefined): string {
    if (!cpf || cpf.length !== 11) return '';
    return `${cpf.substr(0, 3)}.${cpf.substr(3, 3)}.${cpf.substr(6, 3)}-${cpf.substr(9, 2)}`;
  }

  async salvar() {
  // Marca o formulário como touched para mostrar erros de validação
  this.pacienteForm.markAllAsTouched();
  
  if (this.pacienteForm.valid) {
    try {
      this.loading = true;
      const dadosPessoais = this.pacienteForm.get('dadosPessoais')?.value;
      const documentacao = this.pacienteForm.get('documentacao')?.value;
      const endereco = this.pacienteForm.get('endereco')?.value;
      
      const contatos = this.contatosFormArray.value.map((contato: any) => ({
        nome: contato.nome.trim(),
        telefone: contato.telefone.replace(/\D/g, ''),
        // CORREÇÃO: Encontrar a chave do enum que corresponde ao valor
        parentesco: Object.keys(EnumParentesco).find(key => 
          EnumParentesco[key as keyof typeof EnumParentesco] === contato.parentesco
        ) || contato.parentesco
      }));

      const pacienteAtualizado: any = {
        ...this.paciente,
        nome: dadosPessoais.nome,
        dataNasc: dadosPessoais.dataNasc,
        sexo: dadosPessoais.sexo,
        estadoCivil: dadosPessoais.estadoCivil,
        nacionalidade: dadosPessoais.nacionalidade,
        municipioNasc: dadosPessoais.municipioNasc,
        ufNasc: dadosPessoais.ufNasc,
        corRaca: dadosPessoais.corRaca,
        escolaridade: dadosPessoais.escolaridade,
        peso: dadosPessoais.peso,
        altura: dadosPessoais.altura,
        socioeconomico: dadosPessoais.socioeconomico,
        telefone: dadosPessoais.telefone,
        rg: documentacao.rg,
        cpf: documentacao.cpf,
        dataExpedicao: documentacao.dataExpedicao,
        orgaoEmissor: documentacao.orgaoEmissor,
        ufEmissor: documentacao.ufEmissor,
        endereco: {
          cep: endereco.cep.replace(/\D/g, ''),
          logradouro: endereco.logradouro,
          numero: endereco.numero,
          complemento: endereco.complemento || '',
          bairro: endereco.bairro,
          municipio: endereco.municipio,
          uf: endereco.uf
        },
        contatos
      };

      // Remover undefined ou null para não sobrescrever valores existentes
      Object.keys(pacienteAtualizado).forEach(key => {
        delete pacienteAtualizado.fotoUrl;
        delete pacienteAtualizado.fotoPerfil;
        if (pacienteAtualizado[key] === undefined || pacienteAtualizado[key] === null) {
          delete pacienteAtualizado[key];
        }
      });

      // Garantir que o ID existe e está correto
      if (!pacienteAtualizado.id && this.paciente?.id) {
        pacienteAtualizado.id = this.paciente.id;
      }

      console.log('Enviando paciente:', pacienteAtualizado);
      const resultado = await this.pacienteService.updatePaciente(pacienteAtualizado).toPromise();
      console.log('Resposta da API:', resultado);

      this.router.navigate(['/tecnico/paciente/ver-perfil'], {
        queryParams: { id: this.paciente?.id }
      });
    } catch (error) {
      console.error('Erro ao salvar paciente:', error);
      alert('Erro ao salvar as alterações. Por favor, tente novamente.');
    } finally {
      this.loading = false;
    }
  } else {
    this.marcarCamposInvalidos();
    // Debug information for contact validation
    const contatos = this.contatosFormArray.controls;
    contatos.forEach((contato, index) => {
      if (contato.invalid) {
        console.log(`Contato ${index + 1} inválido:`, {
          nome: contato.get('nome')?.errors,
          telefone: contato.get('telefone')?.errors,
          parentesco: contato.get('parentesco')?.errors
        });
      }
    });
    alert('Por favor, preencha todos os campos obrigatórios corretamente.');
  }
}

  removerContato(index: number) {
    this.contatosFormArray.removeAt(index);
  }

  voltar() {
    this.router.navigate(['/tecnico/paciente/ver-perfil'], {
      queryParams: { id: this.paciente?.id }
    });
  }

  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const base64Image = e.target.result;
        this.uploadProfileImage(base64Image);
      };
      reader.readAsDataURL(file);
    }
  }

  private uploadProfileImage(base64Image: string) {
    if (!this.paciente?.id) return;

    this.loading = true;
    this.imageService.uploadPacientePhoto(this.paciente.id, base64Image).subscribe({
      next: (response) => {
        if (this.paciente) {
          this.paciente.fotoUrl = base64Image;
        }
        alert('Foto atualizada com sucesso!');
      },
      error: (error) => {
        console.error('Erro ao fazer upload da foto:', error);
        alert('Erro ao fazer upload da foto. Tente novamente.');
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  removerFoto() {
    if (!this.paciente?.id) return;

    if (confirm('Tem certeza que deseja remover a foto do perfil?')) {
      this.loading = true;
      this.imageService.uploadPacientePhoto(this.paciente.id, '').subscribe({
        next: (response) => {
          if (this.paciente) {
            this.paciente.fotoUrl = undefined;
          }
          alert('Foto removida com sucesso!');
        },
        error: (error) => {
          console.error('Erro ao remover foto:', error);
          alert('Erro ao remover foto. Tente novamente.');
        },
        complete: () => {
          this.loading = false;
        }
      });
    }
  }

  onImageError(event: any) {
    if (event.target) {
      event.target.src = 'https://place-hold.it/192x256';
    }
  }

  private validarData(control: FormControl): { [s: string]: boolean } | null {
    if (!control.value) return null;

    // Accept Date objects, ISO strings, or YYYY-MM-DD format
    let dateToValidate: Date;

    if (control.value instanceof Date) {
      dateToValidate = control.value;
    } else if (typeof control.value === 'string') {
      // Try to parse the string
      const data = control.value.split('-');
      if (data.length !== 3) {
        // Try ISO format or other formats
        dateToValidate = new Date(control.value);
        if (isNaN(dateToValidate.getTime())) {
          return { dataInvalida: true };
        }
      } else {
        const ano = parseInt(data[0], 10);
        const mes = parseInt(data[1], 10);
        const dia = parseInt(data[2], 10);
        dateToValidate = new Date(ano, mes - 1, dia);
      }
    } else {
      return { dataInvalida: true };
    }

    // Check if the date is valid
    if (isNaN(dateToValidate.getTime())) {
      return { dataInvalida: true };
    }

    return null;
  }
}