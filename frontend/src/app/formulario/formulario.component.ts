import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Formulario } from '../_shared/models/formulario/formulario';
import { Pergunta } from '../_shared/models/formulario/pergunta';
import { FormularioService } from '../_shared/services/formulario.service';
import { TipoFormulario } from '../_shared/models/tipos.formulario.enum';
import { Avaliacao } from '../_shared/models/avaliacao/avaliacao';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-formulario',
  templateUrl: './formulario.component.html',
  styleUrl: './formulario.component.css'
})
export class FormularioComponent implements OnInit {
  @Input() tipoFormulario!: TipoFormulario;
  formulario?: Formulario;
  formGroup!: FormGroup;
  etapaAtual = 0;
  respostas: Record<string, any> = {};
  pacienteId?: number;
 
  constructor(
    private fb: FormBuilder,
    private formularioService: FormularioService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.tipoFormulario = params['tipo'] as TipoFormulario;
      this.carregarFormulario();
    });

    this.route.queryParams.subscribe(params => {
      this.pacienteId = params['pacienteId'];
    });
  }

  private async carregarFormulario() {
    try {
      this.formulario = await this.formularioService.getFormularioConfig(this.tipoFormulario).toPromise();
      this.inicializarFormulario();
    } catch (error) {
      console.error('Erro ao carregar formulário:', error);
    }
  }

  private inicializarFormulario() {
    if (!this.formulario) return;

    const groupConfig: Record<string, any> = {};
    
    this.formulario.etapas.forEach((etapa, etapaIndex) => {
      etapa.perguntas.forEach((pergunta, perguntaIndex) => {
        const controlName = this.getControlName(etapaIndex, perguntaIndex);
        groupConfig[controlName] = ['', this.getValidators(pergunta)];
      });
    });
    
    this.formGroup = this.fb.group(groupConfig);
  }

  private getValidators(pergunta: Pergunta) {
    const validators = [];
    if (pergunta.validacao?.required) validators.push(Validators.required);
    if (pergunta.tipo === 'numero' || pergunta.tipo === 'dias') {
      if (pergunta.validacao?.min !== undefined) validators.push(Validators.min(pergunta.validacao.min));
      if (pergunta.validacao?.max !== undefined) validators.push(Validators.max(pergunta.validacao.max));
    }
    if (pergunta.tipo === 'tempo') {
      validators.push(Validators.pattern(/^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/));
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
    if (this.formulario && this.etapaAtual < this.formulario.etapas.length - 1) {
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
    
    const etapaAtual = this.formulario.etapas[this.etapaAtual];
    return etapaAtual.perguntas.every((_, index) => {
      const controlName = this.getControlName(this.etapaAtual, index);
      return this.formGroup.get(controlName)?.valid;
    });
  }

  async salvarAvaliacao() {
    if (!this.formGroup.valid || !this.formulario) return;

    try {
      const avaliacao = new Avaliacao(
        undefined, // id será gerado pelo backend
        undefined, // preenchidoPor será definido pelo service
        undefined, // referenteA será definido pelo service
        this.formulario,
        this.calcularPontuacao(),
        new Date(),
        new Date()
      );

      await this.formularioService.salvarAvaliacao(avaliacao).toPromise();
    } catch (error) {
      console.error('Erro ao salvar avaliação:', error);
    }
  }

  private calcularPontuacao(): number {
    // Implemente a lógica de cálculo da pontuação específica para cada tipo de formulário
    return 0;
  }
}