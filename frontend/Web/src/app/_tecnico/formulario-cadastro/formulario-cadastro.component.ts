import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { FormularioService, FormularioDTO } from '../../_shared/services/formulario.service';
import { Etapa } from '../../_shared/models/formulario/etapa';
import { Pergunta, ConfiguracaoPontuacao, MetadadosCampo, Validacao } from '../../_shared/models/formulario/pergunta';
import { RegraCalculo } from '../../_shared/models/formulario/formulario';

interface EtapaForm {
  titulo: string;
  descricao: string;
  perguntas: PerguntaForm[];
  regraCalculoEtapa?: RegraCalculo;
}

interface PerguntaForm {
  texto: string;
  tipo: string;
  opcoes: string[];
  validacao: {
    min?: number;
    max?: number;
    required: boolean;
  };
  configuracaoPontuacao?: ConfiguracaoPontuacao;
  metadadosCampo?: MetadadosCampo;
}

@Component({
  selector: 'app-formulario-cadastro',
  templateUrl: './formulario-cadastro.component.html',
  styleUrl: './formulario-cadastro.component.css'
})
export class FormularioCadastroComponent implements OnInit {
  formularioForm!: FormGroup;
  tiposCampo = [
    { value: 'numero', label: 'Número' },
    { value: 'texto', label: 'Texto' },
    { value: 'radio', label: 'Múltipla Escolha (Radio)' },
    { value: 'checkbox', label: 'Múltipla Escolha (Checkbox)' },
    { value: 'range', label: 'Escala (Range)' },
    { value: 'tempo', label: 'Tempo' },
    { value: 'dias', label: 'Dias' }
  ];

  tiposPontuacao = [
    { value: 'MAPEAMENTO_DIRETO', label: 'Mapeamento Direto (0-4)' },
    { value: 'MAPEAMENTO_REVERSO', label: 'Mapeamento Reverso (4-0)' },
    { value: 'SOMA_SIMPLES', label: 'Soma Simples' },
    { value: 'MEDIA_SIMPLES', label: 'Média Simples' },
    { value: 'FORMULA', label: 'Fórmula Personalizada' }
  ];

  tiposCalculo = [
    { value: 'SOMA_SIMPLES', label: 'Soma Simples' },
    { value: 'MEDIA_SIMPLES', label: 'Média Simples' },
    { value: 'MEDIA_AJUSTADA', label: 'Média Ajustada' },
    { value: 'SOMA_ETAPAS', label: 'Soma das Etapas' },
    { value: 'FORMULA_CUSTOM', label: 'Fórmula Personalizada' }
  ];

  loading = false;
  showSuccessAlert = false;
  showErrorAlert = false;
  errorMessage = '';
  etapaAtual = 0;

  constructor(
    private fb: FormBuilder,
    private formularioService: FormularioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    this.formularioForm = this.fb.group({
      tipo: ['', Validators.required],
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      calculaPontuacao: [false],
      regraCalculoFinal: this.fb.group({
        tipoCalculo: ['SOMA_SIMPLES'],
        formulaCustom: [''],
        pesos: this.fb.group({})
      }),
      etapas: this.fb.array([this.createEtapaForm()])
    });
  }

  get etapas(): FormArray {
    return this.formularioForm.get('etapas') as FormArray;
  }

  createEtapaForm(): FormGroup {
    return this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      regraCalculoEtapa: this.fb.group({
        tipoCalculo: ['SOMA_SIMPLES'],
        formulaCustom: [''],
        pesos: this.fb.group({})
      }),
      perguntas: this.fb.array([])
    });
  }

  createPerguntaForm(): FormGroup {
    return this.fb.group({
      texto: ['', Validators.required],
      tipo: ['texto', Validators.required],
      opcoes: this.fb.array([]),
      validacao: this.fb.group({
        min: [''],
        max: [''],
        required: [false]
      }),
      configuracaoPontuacao: this.fb.group({
        tipoPontuacao: ['MAPEAMENTO_DIRETO'],
        mapeamentoPontos: this.fb.group({}),
        formula: [''],
        pontosMinimos: [''],
        pontosMaximos: ['']
      }),
      metadadosCampo: this.fb.group({
        subTipo: [''],
        placeholder: [''],
        unidade: [''],
        mascara: [''],
        multiplaEscolha: [false],
        minOpcoes: [''],
        maxOpcoes: ['']
      })
    });
  }

  getEtapaFormGroup(etapaIndex: number): FormGroup {
    return this.etapas.at(etapaIndex) as FormGroup;
  }

  getPerguntas(etapaIndex: number): FormArray {
    return (this.etapas.at(etapaIndex) as FormGroup).get('perguntas') as FormArray;
  }

  getOpcoes(etapaIndex: number, perguntaIndex: number): FormArray {
    return this.getPerguntas(etapaIndex).at(perguntaIndex).get('opcoes') as FormArray;
  }

  adicionarEtapa(): void {
    this.etapas.push(this.createEtapaForm());
  }

  removerEtapa(index: number): void {
    if (this.etapas.length > 1) {
      this.etapas.removeAt(index);
      if (this.etapaAtual >= this.etapas.length) {
        this.etapaAtual = this.etapas.length - 1;
      }
    }
  }

  adicionarPergunta(etapaIndex: number): void {
    const perguntas = this.getPerguntas(etapaIndex);
    perguntas.push(this.createPerguntaForm());
  }

  removerPergunta(etapaIndex: number, perguntaIndex: number): void {
    const perguntas = this.getPerguntas(etapaIndex);
    perguntas.removeAt(perguntaIndex);
  }

  adicionarOpcao(etapaIndex: number, perguntaIndex: number): void {
    const opcoes = this.getOpcoes(etapaIndex, perguntaIndex);
    opcoes.push(this.fb.control('', Validators.required));
  }

  removerOpcao(etapaIndex: number, perguntaIndex: number, opcaoIndex: number): void {
    const opcoes = this.getOpcoes(etapaIndex, perguntaIndex);
    opcoes.removeAt(opcaoIndex);
  }

  onTipoPerguntaChange(etapaIndex: number, perguntaIndex: number): void {
    const pergunta = this.getPerguntas(etapaIndex).at(perguntaIndex);
    const tipo = pergunta.get('tipo')?.value;
    const opcoes = pergunta.get('opcoes') as FormArray;

    // Limpar opções existentes
    while (opcoes.length > 0) {
      opcoes.removeAt(0);
    }

    // Adicionar opções para tipos que precisam
    if (tipo === 'radio' || tipo === 'checkbox') {
      this.adicionarOpcao(etapaIndex, perguntaIndex);
      this.adicionarOpcao(etapaIndex, perguntaIndex);
    }
  }

  navegarEtapa(index: number): void {
    this.etapaAtual = index;
  }

  proximaEtapa(): void {
    if (this.etapaAtual < this.etapas.length - 1) {
      this.etapaAtual++;
    }
  }

  etapaAnterior(): void {
    if (this.etapaAtual > 0) {
      this.etapaAtual--;
    }
  }

  salvarFormulario(): void {
    if (this.formularioForm.valid) {
      this.loading = true;
      const formularioData = this.prepararDados();

      this.formularioService.criarFormulario(formularioData).subscribe({
        next: (response) => {
          this.loading = false;
          this.showSuccessAlert = true;
          setTimeout(() => {
            this.router.navigate(['/tecnico']);
          }, 2000);
        },
        error: (error) => {
          this.loading = false;
          this.showErrorAlert = true;
          this.errorMessage = 'Erro ao salvar formulário: ' + (error.error?.message || error.message);
        }
      });
    } else {
      this.markFormGroupTouched(this.formularioForm);
      this.showErrorAlert = true;
      this.errorMessage = 'Por favor, preencha todos os campos obrigatórios.';
    }
  }

  private prepararDados(): FormularioDTO {
    const formValue = this.formularioForm.value;

    return {
      tipo: formValue.tipo,
      titulo: formValue.titulo,
      descricao: formValue.descricao,
      calculaPontuacao: formValue.calculaPontuacao,
      regraCalculoFinal: formValue.calculaPontuacao ? formValue.regraCalculoFinal : undefined,
      etapas: formValue.etapas.map((etapa: any) => ({
        titulo: etapa.titulo,
        descricao: etapa.descricao,
        regraCalculoEtapa: formValue.calculaPontuacao ? etapa.regraCalculoEtapa : undefined,
        perguntas: etapa.perguntas.map((pergunta: any) => ({
          texto: pergunta.texto,
          tipo: pergunta.tipo,
          opcoes: pergunta.opcoes || [],
          validacao: pergunta.validacao,
          configuracaoPontuacao: formValue.calculaPontuacao ? pergunta.configuracaoPontuacao : undefined,
          metadadosCampo: pergunta.metadadosCampo
        }))
      }))
    };
  }

  private markFormGroupTouched(formGroup: FormGroup | FormArray): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      if (control instanceof FormGroup || control instanceof FormArray) {
        this.markFormGroupTouched(control);
      } else {
        control?.markAsTouched();
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/tecnico']);
  }

  fecharAlerta(): void {
    this.showSuccessAlert = false;
    this.showErrorAlert = false;
    this.errorMessage = '';
  }

  isFieldInvalid(fieldName: string, etapaIndex?: number, perguntaIndex?: number): boolean {
    let control: AbstractControl | null;

    if (etapaIndex !== undefined && perguntaIndex !== undefined) {
      control = this.getPerguntas(etapaIndex).at(perguntaIndex).get(fieldName);
    } else if (etapaIndex !== undefined) {
      control = this.etapas.at(etapaIndex).get(fieldName);
    } else {
      control = this.formularioForm.get(fieldName);
    }

    return !!(control && control.invalid && control.touched);
  }

  shouldShowOptions(tipo: string): boolean {
    return tipo === 'radio' || tipo === 'checkbox';
  }

  shouldShowMinMax(tipo: string): boolean {
    return tipo === 'numero' || tipo === 'range' || tipo === 'dias';
  }
}
