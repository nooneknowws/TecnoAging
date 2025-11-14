import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Avaliacao } from '../../../_shared/models/avaliacao/avaliacao';
import { AvaliacaoService } from '../../../_shared/services/avaliacao.service';
import { Resposta } from '../../../_shared/models/avaliacao/resposta';

interface PerguntaValor {
  pergunta: string;
  valor: string;
  tipo: string;
  validacao?: {
    min?: number;
    max?: number;
    required?: boolean;
  };
  opcoes?: string[];
}

@Component({
  selector: 'app-editar-avaliacao',
  templateUrl: './editar-avaliacao.component.html',
  styleUrls: ['./editar-avaliacao.component.css']
})
export class EditarAvaliacaoComponent implements OnInit {
  avaliacaoData?: any;
  formGroup!: FormGroup;
  perguntasValores: PerguntaValor[] = [];
  
  showSuccessAlert = false;
  showErrorAlert = false;
  errorMessage = '';
  isEditMode = true;

  constructor(
    private fb: FormBuilder,
    private avaliacaoService: AvaliacaoService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    const avaliacaoId = this.route.snapshot.params['id'];
    if (avaliacaoId) {
      this.avaliacaoService.getAvaliacaoById(avaliacaoId).subscribe({
        next: (avaliacao) => {
          this.avaliacaoData = avaliacao;
          this.parseAvaliacaoData(avaliacao);
          this.inicializarFormulario();
          console.log(avaliacao.paciente);
          console.log(avaliacao.tecnico);
        },
        error: (error) => {
          console.error('Erro ao carregar avaliação:', error);
          this.showErrorAlert = true;
          this.errorMessage = 'Erro ao carregar avaliação';
        }
      });
    }
  }

  private parseAvaliacaoData(avaliacaoData: any) {
    this.perguntasValores = avaliacaoData.respostas?.map((resposta: any) => {
      const perguntaTexto = (resposta.pergunta && typeof resposta.pergunta.texto === 'string') ? resposta.pergunta.texto : '';
      const perguntaTipo = (resposta.pergunta && typeof resposta.pergunta.tipo === 'string') ? resposta.pergunta.tipo : this.inferirTipo(perguntaTexto, resposta.valor);
      const perguntaValidacao = (resposta.pergunta && resposta.pergunta.validacao) ? resposta.pergunta.validacao : this.criarValidacao(perguntaTexto);
      const perguntaOpcoes = (resposta.pergunta && Array.isArray(resposta.pergunta.opcoes)) ? resposta.pergunta.opcoes : this.extrairOpcoes(perguntaTexto, resposta.valor);

      return {
        pergunta: perguntaTexto,
        valor: this.parseValor(resposta.valor),
        tipo: perguntaTipo,
        validacao: perguntaValidacao,
        opcoes: perguntaOpcoes
      };
    }) || [];
  }

  private parseValor(valor: any): string {
    if (typeof valor === 'string' && valor.startsWith('"') && valor.endsWith('"')) {
      return valor.slice(1, -1);
    }
    return String(valor || '');
  }

  private inferirTipo(pergunta: any, valor: any): string {
    const perguntaStr = String(pergunta || '');
    const perguntaLower = perguntaStr.toLowerCase();
    
    if (perguntaLower.includes('tempo') || perguntaLower.includes('(hh:mm)')) {
      return 'tempo';
    }
    
    if (perguntaLower.includes('dias') || perguntaLower.includes('quantos dias')) {
      return 'numero';
    }
    
    if (!isNaN(Number(valor)) && !perguntaLower.includes('tempo')) {
      return 'numero';
    }
    
    return 'texto';
  }

  private criarValidacao(pergunta: any) {
    const perguntaStr = String(pergunta || '');
    const perguntaLower = perguntaStr.toLowerCase();
    
    if (perguntaLower.includes('dias')) {
      return { min: 0, max: 7, required: false };
    }
    
    return { required: false };
  }

  private extrairOpcoes(pergunta: any, valor: any): string[] | undefined {
    const perguntaStr = String(pergunta || '');
    const perguntaLower = perguntaStr.toLowerCase();
    return undefined;
  }

  private inicializarFormulario() {
    const groupConfig: Record<string, any> = {};
    
    this.perguntasValores.forEach((perguntaValor, index) => {
      const controlName = this.getControlName(index);
      
      if (perguntaValor.tipo === 'checkbox' && perguntaValor.opcoes) {
        const checkboxControls = perguntaValor.opcoes.map(() => 
          this.fb.control(false)
        );
        
        groupConfig[controlName] = this.fb.array(
          checkboxControls,
          perguntaValor.validacao?.required ? [this.atLeastOneCheckboxSelected()] : []
        );
      } else if (perguntaValor.tipo === 'range') {
        const valorInicial = perguntaValor.validacao?.min || 0;
        groupConfig[controlName] = [Number(perguntaValor.valor) || valorInicial, this.getValidators(perguntaValor)];
      } else {
        groupConfig[controlName] = [perguntaValor.valor || '', this.getValidators(perguntaValor)];
      }
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

  private getValidators(perguntaValor: PerguntaValor) {
    const validators = [];
    if (perguntaValor.validacao?.required) validators.push(Validators.required);
    
    if (perguntaValor.tipo === 'numero' || perguntaValor.tipo === 'dias' || perguntaValor.tipo === 'range') {
      if (perguntaValor.validacao?.min !== undefined) {
        validators.push(Validators.min(perguntaValor.validacao.min));
      }
      if (perguntaValor.validacao?.max !== undefined) {
        validators.push(Validators.max(perguntaValor.validacao.max));
      }
    }
    
    if (perguntaValor.tipo === 'tempo') {
      validators.push(Validators.pattern(/^([01]\d|2[0-3]):([0-5]\d)$/));
    }
    
    return validators;
  }

  getControlName(index: number): string {
    return `pergunta_${index}`;
  }

  getFieldError(index: number): string {
    const control = this.formGroup.get(this.getControlName(index));
    if (control?.errors) {
      if (control.errors['required']) return 'Campo obrigatório';
      if (control.errors['min']) return `Valor mínimo: ${control.errors['min'].min}`;
      if (control.errors['max']) return `Valor máximo: ${control.errors['max'].max}`;
      if (control.errors['pattern']) return 'Formato inválido';
    }
    return '';
  }

  getCheckboxFormArray(index: number): FormArray {
    const controlName = this.getControlName(index);
    return this.formGroup.get(controlName) as FormArray;
  }

  isAnyCheckboxSelected(formArray: FormArray): boolean {
    return formArray.value.some((value: boolean) => value);
  }

  async salvarAvaliacao() {
    if (!this.formGroup.valid || !this.avaliacaoData) return;

    try {
      const updatedAvaliacao = { ...this.avaliacaoData };

      // Update the respostas with form values while keeping the pergunta information
      updatedAvaliacao.respostas = this.avaliacaoData.respostas?.map((resposta: any, index: number) => {
        const controlName = this.getControlName(index);
        let valorResposta = this.formGroup.get(controlName)?.value;

        const perguntaValor = this.perguntasValores[index];
        if (perguntaValor.tipo === 'checkbox' && perguntaValor.opcoes) {
          const checkboxArray = this.getCheckboxFormArray(index);
          valorResposta = perguntaValor.opcoes.filter((_, optIndex) =>
            checkboxArray.at(optIndex).value
          );
        }

        return {
          pergunta: resposta.pergunta, // Keep the full pergunta object
          valor: valorResposta
        };
      }) || [];

      await this.avaliacaoService.updateAvaliacao(updatedAvaliacao).toPromise();

      this.showSuccessAlert = true;
      this.showErrorAlert = false;
      window.scrollTo(0, 0);

    } catch (error) {
      console.error('Erro ao salvar avaliação:', error);
      this.showErrorAlert = true;
      this.showSuccessAlert = false;
      this.errorMessage = 'Ocorreu um erro ao salvar a avaliação. Por favor, tente novamente.';
      window.scrollTo(0, 0);
    }
  }

  cancelar() {
    this.router.navigate(['/tecnico/avaliacoes/consultar', this.avaliacaoData.paciente?.id]);
  }

  fecharAlerta() {
    this.showSuccessAlert = false;
    this.showErrorAlert = false;
  }
}