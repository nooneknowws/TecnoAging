import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Formulario } from '../_shared/models/formulario/formulario';
import { Pergunta } from '../_shared/models/formulario/pergunta';
import { FormularioService } from '../_shared/services/formulario.service';
import { AvaliacaoService } from '../_shared/services/avaliacao.service';
import { Avaliacao } from '../_shared/models/avaliacao/avaliacao';
import { Resposta } from '../_shared/models/avaliacao/resposta';
import { Etapa } from '../_shared/models/formulario/etapa';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-formulario',
  templateUrl: './formulario.component.html',
  styleUrl: './formulario.component.css'
})
export class FormularioComponent implements OnInit {
  @Input() formularioId!: number;
  formulario?: Formulario;
  formGroup!: FormGroup;
  etapaAtual = 0;
  respostasTemp: Map<number, Resposta> = new Map();
  pacienteId?: number;

  showSuccessAlert = false;
  showErrorAlert = false;
  errorMessage = '';
  avaliacaoSalva = false;

  constructor(
    private fb: FormBuilder,
    private formularioService: FormularioService,
    private avaliacaoService: AvaliacaoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.formularioId = +params['id'];
      this.carregarFormulario();
    });

    this.route.queryParams.subscribe(params => {
      this.pacienteId = params['pacienteId'];
    });
  }

  private async carregarFormulario() {
    try {
      this.formulario = await this.formularioService.getFormularioPorId(this.formularioId).toPromise();
      this.inicializarFormulario();

      this.formGroup.valueChanges.subscribe(values => {
        this.atualizarRespostas(values);
      });
    } catch (error) {
      console.error('Erro ao carregar formulário:', error);
    }
  }

  private inicializarFormulario() {
    if (!this.formulario) return;
  
    const groupConfig: Record<string, any> = {};
    
    this.formulario.etapas!.forEach((etapa, etapaIndex) => {
      etapa.perguntas.forEach((pergunta, perguntaIndex) => {
        const controlName = this.getControlName(etapaIndex, perguntaIndex);
        
        if (pergunta.tipo === 'checkbox' && pergunta.opcoes) {
          const checkboxControls = pergunta.opcoes.map(() => 
            this.fb.control(false)
          );
          
          groupConfig[controlName] = this.fb.array(
            checkboxControls,
            pergunta.validacao?.required ? [this.atLeastOneCheckboxSelected()] : []
          );
        } else if (pergunta.tipo === 'range') {
          const valorInicial = pergunta.validacao?.min || 0;
          groupConfig[controlName] = [valorInicial, this.getValidators(pergunta)];
        } else {
          groupConfig[controlName] = ['', this.getValidators(pergunta)];
        }
      });
    });
    
    this.formGroup = this.fb.group(groupConfig);
  }

  private atLeastOneCheckboxSelected(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (control instanceof FormArray) {
        const checked = (control as FormArray).controls
          .map(item => item.value)
          .some(value => value === true);
        return checked ? null : { required: true };
      }
      return null;
    };
  }

  atualizarRespostas(values: any) {
    if (!this.formulario) return;
    
    this.formulario.etapas!.forEach((etapa, etapaIndex) => {
      etapa.perguntas.forEach((pergunta, perguntaIndex) => {
        const controlName = this.getControlName(etapaIndex, perguntaIndex);
        let valorResposta: string | string[] | number | boolean | undefined;
        
        if (pergunta.tipo === 'checkbox' && pergunta.opcoes) {
          const checkboxArray = this.getCheckboxFormArray(etapaIndex, perguntaIndex);
          valorResposta = pergunta.opcoes.filter((_, index) => 
            checkboxArray.at(index).value
          );
        } else if (pergunta.tipo === 'range') {
          valorResposta = Number(values[controlName]);
        } else {
          valorResposta = values[controlName] || '';
        }

        this.respostasTemp.set(pergunta.id!, new Resposta(
          pergunta,
          valorResposta
        ));
      });
    });
  }

  async salvarAvaliacao() {
    if (!this.formGroup.valid || !this.formulario) return;

    try {
      this.atualizarRespostas(this.formGroup.value);

      const respostas = Array.from(this.respostasTemp.values());

      const avaliacao = new Avaliacao(
        undefined,
        undefined,
        undefined,
        this.formulario,
        respostas,
        undefined,
        undefined,
        new Date(),
        new Date()
      );

      console.log('JSON da avaliação a ser enviada:', JSON.stringify(avaliacao, null, 2));

      await this.avaliacaoService.createAvaliacao(avaliacao, this.pacienteId).toPromise();

      this.showSuccessAlert = true;
      this.showErrorAlert = false;
      this.avaliacaoSalva = true;
      window.scrollTo(0, 0);

    } catch (error) {
      console.error('Erro ao salvar avaliação:', error);
      this.showErrorAlert = true;
      this.showSuccessAlert = false;
      this.errorMessage = 'Ocorreu um erro ao salvar a avaliação. Por favor, tente novamente.';
      window.scrollTo(0, 0);
    }
  }  

  getCheckboxFormArray(etapaIndex: number, perguntaIndex: number): FormArray {
    const controlName = this.getControlName(etapaIndex, perguntaIndex);
    return this.formGroup.get(controlName) as FormArray;
  }

  isAnyCheckboxSelected(formArray: FormArray): boolean {
    return formArray.value.some((value: boolean) => value);
  }

  private getValidators(pergunta: Pergunta) {
    const validators = [];
    if (pergunta.validacao?.required) validators.push(Validators.required);
    
    if (pergunta.tipo === 'numero' || pergunta.tipo === 'dias' || pergunta.tipo === 'range') {
      if (pergunta.validacao?.min !== undefined) {
        validators.push(Validators.min(pergunta.validacao.min));
      }
      if (pergunta.validacao?.max !== undefined) {
        validators.push(Validators.max(pergunta.validacao.max));
      }
    }
    
    if (pergunta.tipo === 'tempo') {
      validators.push(Validators.pattern(/^([01]\d|2[0-3]):([0-5]\d)$/));
    }
    
    return validators;
  }

  getControlName(etapaIndex: number, perguntaIndex: number): string {
    return `etapa${etapaIndex}_pergunta${perguntaIndex}`;
  }

  getFieldError(etapaIndex: number, perguntaIndex: number): string {
    const control = this.formGroup.get(this.getControlName(etapaIndex, perguntaIndex));
    if (control?.errors) {
      if (control.errors['required']) return 'Campo obrigatório';
      if (control.errors['min']) return `Valor mínimo: ${control.errors['min'].min}`;
      if (control.errors['max']) return `Valor máximo: ${control.errors['max'].max}`;
      if (control.errors['pattern']) return 'Formato inválido';
    }
    return '';
  }

  proximaEtapa() {
    if (this.formulario && this.etapaAtual < this.formulario.etapas!.length - 1) {
      this.etapaAtual++;
    }
  }

  etapaAnterior() {
    if (this.etapaAtual > 0) {
      this.etapaAtual--;
    }
  }

  verificarValidadeEtapaAtual(): boolean {
    if (!this.formulario || !this.formGroup) return false;
    
    const etapaAtual = this.formulario.etapas![this.etapaAtual];
    return etapaAtual.perguntas.every((_, index) => {
      const controlName = this.getControlName(this.etapaAtual, index);
      return this.formGroup.get(controlName)?.valid;
    });
  }


  voltarParaInicio() {
    this.router.navigate(['/']);
  }

  verPerfilPaciente() {
    this.router.navigate(['/tecnico/paciente/ver-perfil'], {
      queryParams: { id: this.pacienteId }
    });
  }

  fecharAlerta() {
    this.showSuccessAlert = false;
    this.showErrorAlert = false;
  }
}