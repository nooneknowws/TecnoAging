import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Paciente } from '../../../_shared/models/pessoa/paciente/paciente';
import { PacienteService } from '../../../_shared/services/paciente.service';
import { Contato } from '../../../_shared/models/pessoa/paciente/contato';
import { EnumEstadosBrasil } from '../../../_shared/models/estadosbrasil.enum';
import { EnumEstadoCivil } from '../../../_shared/models/estadocivil.enum';
import { AuthService } from '../../../_shared/services/auth.service';
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

  estadosCivis = Object.values(EnumEstadoCivil);
  parentescos = Object.values(EnumParentesco);
  ufs = Object.values(EnumEstadosBrasil);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private pacienteService: PacienteService,
    private router: Router,
    private route: ActivatedRoute
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
          this.router.navigate(['/tecnico/dashboard']);
        }
      },
      error: (error) => {
        console.error('Erro ao carregar paciente:', error);
        this.router.navigate(['/tecnico/dashboard']);
      }
    });
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
        idade: [{ value: '', disabled: true }]
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
  
    const dadosPessoais = this.pacienteForm.get('dadosPessoais');
    const documentacao = this.pacienteForm.get('documentacao');
    const endereco = this.pacienteForm.get('endereco');
  
    if (dadosPessoais && documentacao && endereco) {
      dadosPessoais.patchValue({
        nome: paciente.nome || '',
        dataNasc: paciente.dataNasc || '',  // Já vem como string
        sexo: paciente.sexo || '',
        estadoCivil: paciente.estadoCivil || '',  // Tratar como string
        nacionalidade: paciente.nacionalidade || 'Brasileiro',
        municipioNasc: paciente.municipioNasc || '',
        ufNasc: paciente.ufNasc || '',  // Tratar como string
        corRaca: paciente.corRaca || ''
      });
  
      documentacao.patchValue({
        rg: paciente.rg || '',
        cpf: this.formatarCpfSeNecessario(paciente.cpf),
        dataExpedicao: paciente.dataExpedicao || '',  // Já vem como string
        orgaoEmissor: paciente.orgaoEmissor || '',
        ufEmissor: paciente.ufEmissor || ''  // Tratar como string
      });
  
      if (paciente.endereco) {
        endereco.patchValue({
          cep: this.formatarCepSeNecessario(paciente.endereco.cep),
          logradouro: paciente.endereco.logradouro || '',
          numero: paciente.endereco.numero || '',
          complemento: paciente.endereco.complemento || '',
          bairro: paciente.endereco.bairro || '',
          municipio: paciente.endereco.municipio || '',
          uf: paciente.endereco.uf || ''  // Tratar como string
        });
      }
  
      // Limpa e recria os contatos
      this.contatosFormArray.clear();
      paciente.contatos?.forEach(contato => {
        this.adicionarContato({
          nome: contato.nome,
          telefone: this.formatarTelefoneSeNecessario(contato.telefone),
          parentesco: contato.parentesco
        });
      });
    }
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
    
    const cepStr = cep.toString().trim(); // Convert to string and trim whitespace
    const hasHyphen = cepStr.includes('-');
  
    // Check if already formatted (has hyphen and 8 total digits)
    if (hasHyphen) {
      return cepStr;
    }
  
    // Clean to only digits
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
        this.preencherFormulario(paciente);
      },
      error: (error) => console.error('Erro ao carregar paciente:', error)
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
    // Format phone number if it exists
    let telefoneFormatado = '';
    if (contato?.telefone) {
      // Remove all non-digits
      const digits = contato.telefone.replace(/\D/g, '');
      if (digits.length === 11) {
        telefoneFormatado = `(${digits.slice(0,2)}) ${digits.slice(2,7)}-${digits.slice(7)}`;
      } else {
        telefoneFormatado = contato.telefone; // Keep original if can't format
      }
    }

    const contatoForm = this.fb.group({
      nome: [contato?.nome || '', [
        Validators.required,
        Validators.minLength(3)
      ]],
      telefone: [telefoneFormatado || '', [
        Validators.required,
        Validators.pattern(/^\(\d{2}\) \d{5}\-\d{4}$/)
      ]],
      parentesco: [contato?.parentesco || '', [
        Validators.required
      ]]
    });

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
    if (this.pacienteForm.valid) {
      try {
        this.loading = true;
        
        const dadosPessoais = this.pacienteForm.get('dadosPessoais')?.value;
        const documentacao = this.pacienteForm.get('documentacao')?.value;
        const endereco = this.pacienteForm.get('endereco')?.value;
        const contatos = this.contatosFormArray.value.map((contato: any) => ({
          nome: contato.nome.trim(),
          telefone: contato.telefone.replace(/\D/g, ''), // Remove formatação
          parentesco: contato.parentesco // Vai como string, não enum
        }));
    
        const pacienteAtualizado: any = { // Use `any` para evitar problemas de tipo
          ...this.paciente,
          nome: dadosPessoais.nome,
          dataNasc: dadosPessoais.dataNasc, // Já está no formato correto como string
          sexo: dadosPessoais.sexo,
          estadoCivil: dadosPessoais.estadoCivil, // Enviar como string, não enum
          nacionalidade: dadosPessoais.nacionalidade,
          municipioNasc: dadosPessoais.municipioNasc,
          ufNasc: dadosPessoais.ufNasc, // Enviar como string, não enum
          corRaca: dadosPessoais.corRaca,
          
          rg: documentacao.rg,
          cpf: documentacao.cpf, // Manter formatação ou remover conforme necessário
          dataExpedicao: documentacao.dataExpedicao, // Já está no formato correto
          orgaoEmissor: documentacao.orgaoEmissor,
          ufEmissor: documentacao.ufEmissor, // Enviar como string, não enum
          
          endereco: {
            cep: endereco.cep.replace(/\D/g, ''), // Remover formatação
            logradouro: endereco.logradouro,
            numero: endereco.numero,
            complemento: endereco.complemento || '',
            bairro: endereco.bairro,
            municipio: endereco.municipio,
            uf: endereco.uf // Enviar como string, não enum
          },
          
          contatos
        };
        
        // Remover undefined ou null para não sobrescrever valores existentes
        Object.keys(pacienteAtualizado).forEach(key => {
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

  private validarData(control: FormControl): { [s: string]: boolean } | null {
    if (!control.value) return null;
    
    const data = control.value.split('-');
    if (data.length !== 3) return { dataInvalida: true };
    
    const dia = parseInt(data[2], 10);
    const mes = parseInt(data[1], 10);
    const ano = parseInt(data[0], 10);
    
    const dataValida = new Date(ano, mes - 1, dia).getTime() === new Date(ano, mes - 1, dia).getTime();
    if (!dataValida) return { dataInvalida: true };
    
    return null;
  }
}