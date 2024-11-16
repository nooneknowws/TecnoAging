import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Paciente } from '../../../_shared/models/pessoa/paciente/paciente';
import { PacienteService } from '../../../_shared/services/paciente.service';
import { Contato } from '../../../_shared/models/pessoa/paciente/contato';
import { EnumEstadosBrasil } from '../../../_shared/models/estadosbrasil.enum';
import { EnumEstadoCivil } from '../../../_shared/models/estadocivil.enum';
import { AuthService } from '../../../_shared/services/auth.service';

@Component({
  selector: 'app-editar-perfil',
  templateUrl: './editar-perfil.component.html',
  styleUrls: ['./editar-perfil.component.css']
})
export class EditarPerfilPacienteComponent implements OnInit {
  pacienteForm: FormGroup;
  paciente?: Paciente;
  estadosCivis = Object.values(EnumEstadoCivil)
  ufs = Object.values(EnumEstadosBrasil)
  
  constructor(
    private fb: FormBuilder,
    private pacienteService: PacienteService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {
    this.pacienteForm = this.inicializarFormulario();
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const pacienteId = params['id'];
      if (pacienteId) {
        this.carregarPaciente(pacienteId);
      }
    });
  }

  private inicializarFormulario(): FormGroup {
    return this.fb.group({
      dadosPessoais: this.fb.group({
        nome: ['', Validators.required],
        dataNasc: ['', Validators.required],
        sexo: ['', Validators.required],
        estadoCivil: ['', Validators.required],
        nacionalidade: ['', Validators.required],
        municipioNasc: ['', Validators.required],
        ufNasc: ['', Validators.required],
        corRaca: ['', Validators.required]
      }),
      documentacao: this.fb.group({
        rg: ['', Validators.required],
        cpf: ['', [Validators.required, Validators.pattern(/^\d{3}\.\d{3}\.\d{3}\-\d{2}$/)]],
        dataExpedicao: ['', Validators.required],
        orgaoEmissor: ['', Validators.required],
        ufEmissor: ['', Validators.required]
      }),
      endereco: this.fb.group({
        cep: ['', [Validators.required, Validators.pattern(/^\d{5}\-\d{3}$/)]],
        logradouro: ['', Validators.required],
        numero: ['', Validators.required],
        bairro: ['', Validators.required],
        municipio: ['', Validators.required],
        uf: ['', Validators.required]
      }),
      contatos: this.fb.array([])
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

  private preencherFormulario(paciente: Paciente) {
    const dadosPessoais = this.pacienteForm.get('dadosPessoais');
    const documentacao = this.pacienteForm.get('documentacao');
    const endereco = this.pacienteForm.get('endereco');

    if (dadosPessoais && documentacao && endereco) {
      dadosPessoais.patchValue({
        nome: paciente.nome,
        dataNasc: paciente.dataNasc,
        sexo: paciente.sexo,
        estadoCivil: paciente.estadoCivil,
        nacionalidade: paciente.nacionalidade,
        municipioNasc: paciente.municipioNasc,
        ufNasc: paciente.ufNasc,
        corRaca: paciente.corRaca
      });

      documentacao.patchValue({
        rg: paciente.rg,
        cpf: paciente.cpf,
        dataExpedicao: paciente.dataExpedicao,
        orgaoEmissor: paciente.orgaoEmissor,
        ufEmissor: paciente.ufEmissor
      });

      endereco.patchValue({
        cep: paciente.endereco?.CEP,
        logradouro: paciente.endereco?.logradouro,
        numero: paciente.endereco?.numero,
        bairro: paciente.endereco?.bairro,
        municipio: paciente.endereco?.municipio,
        uf: paciente.endereco?.UF
      });

      while (this.contatosFormArray.length) {
        this.contatosFormArray.removeAt(0);
      }

      paciente.contatos?.forEach(contato => {
        this.adicionarContato(contato);
      });
    }
  }

  adicionarContato(contato?: Contato) {
    const contatoForm = this.fb.group({
      nome: [contato?.nome || '', Validators.required],
      telefone: [contato?.telefone || '', [Validators.required, Validators.pattern(/^\(\d{2}\) \d{5}\-\d{4}$/)]],
      parentesco: [contato?.parentesco || '', Validators.required]
    });

    this.contatosFormArray.push(contatoForm);
  }

  removerContato(index: number) {
    this.contatosFormArray.removeAt(index);
  }

  async salvar() {
    if (this.pacienteForm.valid) {
      try {
        const pacienteAtualizado = {
          ...this.paciente,
          ...this.pacienteForm.get('dadosPessoais')?.value,
          ...this.pacienteForm.get('documentacao')?.value,
          endereco: this.pacienteForm.get('endereco')?.value,
          contatos: this.pacienteForm.get('contatos')?.value
        };

        await this.pacienteService.updatePaciente(pacienteAtualizado).toPromise();
        this.router.navigate(['/tecnico/paciente/ver-perfil'], { 
          queryParams: { id: this.paciente?.id } 
        });
      } catch (error) {
        console.error('Erro ao salvar paciente:', error);
      }
    }
  }

  voltar() {
    this.router.navigate(['/tecnico/paciente/ver-perfil'], { 
      queryParams: { id: this.paciente?.id } 
    });
  }

  buscarCep(cep: string) {
    const cepLimpo = cep.replace(/\D/g, '');
    
    if (cepLimpo.length === 8) {
      this.authService.buscarCep(cepLimpo).subscribe({
        next: (dados: any) => {
          if (dados) {
            const endereco = this.pacienteForm.get('endereco');
            if (endereco) {
              endereco.patchValue({
                logradouro: dados.logradouro,
                bairro: dados.bairro,
                municipio: dados.localidade, 
                uf: dados.uf
              });
            }
          }
        },
        error: (error) => {
          console.error('Erro ao buscar CEP:', error);
          alert('Erro ao buscar CEP. Por favor, verifique o CEP informado.');
        }
      });
    }
  }
}