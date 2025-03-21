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
        nome: paciente.nome,
        dataNasc: this.formatarData(paciente.dataNasc),
        sexo: paciente.sexo,
        estadoCivil: paciente.estadoCivil,
        nacionalidade: paciente.nacionalidade,
        municipioNasc: paciente.municipioNasc,
        ufNasc: paciente.ufNasc,
        corRaca: paciente.corRaca,
        idade: paciente.idade
      });

      documentacao.patchValue({
        rg: paciente.rg,
        cpf: paciente.cpf,
        dataExpedicao: this.formatarData(paciente.dataExpedicao),
        orgaoEmissor: paciente.orgaoEmissor,
        ufEmissor: paciente.ufEmissor
      });

      if (paciente.endereco) {
        endereco.patchValue({
          cep: paciente.endereco.cep,
          logradouro: paciente.endereco.logradouro,
          numero: paciente.endereco.numero,
          complemento: paciente.endereco.complemento,
          bairro: paciente.endereco.bairro,
          municipio: paciente.endereco.municipio,
          uf: paciente.endereco.uf
        });
      }

      // Limpa e recria os contatos
      this.contatosFormArray.clear();
      paciente.contatos?.forEach(contato => {
        this.adicionarContato(contato);
      });
    }
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


  async salvar() {
    if (this.pacienteForm.valid) {
      try {
        this.loading = true;
        
        const dadosPessoais = this.pacienteForm.get('dadosPessoais')?.value;
        const documentacao = this.pacienteForm.get('documentacao')?.value;
        const endereco = this.pacienteForm.get('endereco')?.value;
        console.log(JSON.stringify(endereco))
        const contatos = this.contatosFormArray.value.map((contato: any) => ({
          nome: contato.nome.trim(),
          telefone: contato.telefone.replace(/\D/g, ''), // Remove formatting before saving
          parentesco: contato.parentesco as EnumParentesco
        }));
    
        const pacienteAtualizado: Paciente = {
          ...this.paciente,
          nome: dadosPessoais.nome,
          dataNasc: dadosPessoais.dataNasc, // Mantém como string "yyyy-mm-dd"
          sexo: dadosPessoais.sexo,
          estadoCivil: dadosPessoais.estadoCivil as EnumEstadoCivil,
          nacionalidade: dadosPessoais.nacionalidade,
          municipioNasc: dadosPessoais.municipioNasc,
          ufNasc: dadosPessoais.ufNasc as EnumEstadosBrasil,
          corRaca: dadosPessoais.corRaca,
          
          rg: documentacao.rg,
          cpf: documentacao.cpf,
          dataExpedicao: documentacao.dataExpedicao, // Mantém como string "yyyy-mm-dd"
          orgaoEmissor: documentacao.orgaoEmissor,
          ufEmissor: documentacao.ufEmissor as EnumEstadosBrasil,
          
          endereco: {
            cep: endereco.cep.replace(/\D/g, ''),
            logradouro: endereco.logradouro,
            numero: endereco.numero,
            complemento: endereco.complemento,
            bairro: endereco.bairro,
            municipio: endereco.municipio,
            uf: endereco.uf as EnumEstadosBrasil
          },
          
          contatos
        };
    
        // Antes de enviar, garantimos que as datas estão no formato correto
        pacienteAtualizado.dataNasc = pacienteAtualizado.dataNasc;
        pacienteAtualizado.dataExpedicao = pacienteAtualizado.dataExpedicao;
    
        await this.pacienteService.updatePaciente(pacienteAtualizado).toPromise();
        
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