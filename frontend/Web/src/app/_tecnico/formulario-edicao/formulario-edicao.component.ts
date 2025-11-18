import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators, AbstractControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { FormularioService, FormularioDTO } from '../../_shared/services/formulario.service';
import { Formulario } from '../../_shared/models/formulario/formulario';
import { Etapa } from '../../_shared/models/formulario/etapa';
import { Pergunta, ConfiguracaoPontuacao, MetadadosCampo, Validacao } from '../../_shared/models/formulario/pergunta';
import { RegraCalculo } from '../../_shared/models/formulario/formulario';

@Component({
  selector: 'app-formulario-edicao',
  templateUrl: './formulario-edicao.component.html',
  styleUrl: './formulario-edicao.component.css'
})
export class FormularioEdicaoComponent implements OnInit {
  formularioForm!: FormGroup;
  formularioId!: number;
  formularioOriginal?: Formulario;
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
    { value: 'VALOR_DIRETO', label: 'Valor Direto' },
    { value: 'MAPEAMENTO', label: 'Mapeamento' },
    { value: 'MAPEAMENTO_DIRETO', label: 'Mapeamento Direto' },
    { value: 'MAPEAMENTO_REVERSO', label: 'Mapeamento Reverso' },
    { value: 'MAPEAMENTO_MAX', label: 'Mapeamento Máximo' },
    { value: 'FORMULA', label: 'Fórmula Personalizada' }
  ];

  tiposCalculo = [
    { value: '', label: 'Nenhum' },
    { value: 'SOMA_PERGUNTAS', label: 'Soma das Perguntas' },
    { value: 'MAX_PERGUNTAS', label: 'Máximo das Perguntas' },
    { value: 'SOMA_ETAPAS', label: 'Soma das Etapas' },
    { value: 'MEDIA_AJUSTADA', label: 'Média Ajustada' },
    { value: 'FORMULA_CUSTOM', label: 'Fórmula Personalizada' }
  ];

  loading = false;
  loadingForm = true;
  showSuccessAlert = false;
  showErrorAlert = false;
  errorMessage = '';
  etapaAtual = 0;

  constructor(
    private fb: FormBuilder,
    private formularioService: FormularioService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.formularioId = +this.route.snapshot.params['id'];
    this.loadFormulario();
  }

  loadFormulario(): void {
    this.loadingForm = true;
    this.formularioService.getFormularioPorId(this.formularioId).subscribe({
      next: (formulario) => {
        this.formularioOriginal = formulario;
        this.initializeFormWithData(formulario);
        this.loadingForm = false;
      },
      error: (error) => {
        this.loadingForm = false;
        this.showErrorAlert = true;
        this.errorMessage = 'Erro ao carregar formulário: ' + (error.error?.message || error.message);
      }
    });
  }

  initializeFormWithData(formulario: Formulario): void {
    this.formularioForm = this.fb.group({
      tipo: [formulario.tipo, Validators.required],
      titulo: [formulario.titulo, Validators.required],
      descricao: [formulario.descricao, Validators.required],
      calculaPontuacao: [formulario.calculaPontuacao || false],
      regraCalculoFinal: this.fb.group({
        tipoCalculo: [formulario.regraCalculoFinal?.tipoCalculo || 'SOMA_ETAPAS'],
        formulaCustom: [formulario.regraCalculoFinal?.formulaCustom || ''],
        pesos: this.fb.group(formulario.regraCalculoFinal?.pesos || {})
      }),
      etapas: this.fb.array([])
    });

    // Carregar etapas
    const etapasArray = this.formularioForm.get('etapas') as FormArray;
    if (formulario.etapas) {
      formulario.etapas.forEach(etapa => {
        etapasArray.push(this.createEtapaFormWithData(etapa));
      });
    } else {
      etapasArray.push(this.createEtapaForm());
    }
  }

  createEtapaFormWithData(etapa: Etapa): FormGroup {
    const etapaForm = this.fb.group({
      titulo: [etapa.titulo, Validators.required],
      descricao: [etapa.descricao, Validators.required],
      regraCalculoEtapa: this.fb.group({
        tipoCalculo: [etapa.regraCalculoEtapa?.tipoCalculo || 'SOMA_PERGUNTAS'],
        formulaCustom: [etapa.regraCalculoEtapa?.formulaCustom || ''],
        pesos: this.fb.group(etapa.regraCalculoEtapa?.pesos || {})
      }),
      perguntas: this.fb.array([])
    });

    // Carregar perguntas
    const perguntasArray = etapaForm.get('perguntas') as FormArray;
    if (etapa.perguntas) {
      etapa.perguntas.forEach(pergunta => {
        perguntasArray.push(this.createPerguntaFormWithData(pergunta));
      });
    } else {
      perguntasArray.push(this.createPerguntaForm());
    }

    return etapaForm;
  }

  createPerguntaFormWithData(pergunta: Pergunta): FormGroup {
    // Criar FormGroup para mapeamento de pontos
    const mapeamentoPontosGroup = this.fb.group({});
    if (pergunta.configuracaoPontuacao?.mapeamentoPontos) {
      Object.entries(pergunta.configuracaoPontuacao.mapeamentoPontos).forEach(([key, value]) => {
        mapeamentoPontosGroup.addControl(key, this.fb.control(value));
      });
    }

    const perguntaForm = this.fb.group({
      texto: [pergunta.texto, Validators.required],
      tipo: [pergunta.tipo, Validators.required],
      opcoes: this.fb.array([]),
      validacao: this.fb.group({
        min: [pergunta.validacao?.min || ''],
        max: [pergunta.validacao?.max || ''],
        required: [pergunta.validacao?.required || false]
      }),
      configuracaoPontuacao: this.fb.group({
        tipoPontuacao: [pergunta.configuracaoPontuacao?.tipoPontuacao || 'MAPEAMENTO'],
        mapeamentoPontos: mapeamentoPontosGroup,
        formula: [pergunta.configuracaoPontuacao?.formula || ''],
        pontosMinimos: [pergunta.configuracaoPontuacao?.pontosMinimos || ''],
        pontosMaximos: [pergunta.configuracaoPontuacao?.pontosMaximos || '']
      }),
      metadadosCampo: this.fb.group({
        subTipo: [pergunta.metadadosCampo?.subTipo || ''],
        mascara: [pergunta.metadadosCampo?.mascara || ''],
        multiplaEscolha: [pergunta.metadadosCampo?.multiplaEscolha || false],
        minOpcoes: [pergunta.metadadosCampo?.minOpcoes || ''],
        maxOpcoes: [pergunta.metadadosCampo?.maxOpcoes || '']
      })
    });

    // Carregar opções
    const opcoesArray = perguntaForm.get('opcoes') as FormArray;
    if (pergunta.opcoes) {
      pergunta.opcoes.forEach(opcao => {
        opcoesArray.push(this.fb.control(opcao, Validators.required));
      });
    }

    return perguntaForm;
  }

  get etapas(): FormArray {
    return this.formularioForm.get('etapas') as FormArray;
  }

  createEtapaForm(): FormGroup {
    return this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      regraCalculoEtapa: this.fb.group({
        tipoCalculo: ['SOMA_PERGUNTAS'],
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
        tipoPontuacao: ['MAPEAMENTO'],
        mapeamentoPontos: this.fb.group({}),
        formula: [''],
        pontosMinimos: [''],
        pontosMaximos: ['']
      }),
      metadadosCampo: this.fb.group({
        subTipo: [''],
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

    // Se pontuação habilitada, sincronizar mapeamento
    if (this.formularioForm.get('calculaPontuacao')?.value) {
      this.sincronizarMapeamentoPontos(etapaIndex, perguntaIndex);
    }
  }

  removerOpcao(etapaIndex: number, perguntaIndex: number, opcaoIndex: number): void {
    const opcoes = this.getOpcoes(etapaIndex, perguntaIndex);
    const valorOpcao = opcoes.at(opcaoIndex).value;
    opcoes.removeAt(opcaoIndex);

    // Se pontuação habilitada, remover do mapeamento
    if (this.formularioForm.get('calculaPontuacao')?.value && valorOpcao) {
      const mapeamento = this.getMapeamentoPontos(etapaIndex, perguntaIndex);
      mapeamento.removeControl(valorOpcao);
    }
  }

  getMapeamentoPontos(etapaIndex: number, perguntaIndex: number): FormGroup {
    return this.getPerguntas(etapaIndex).at(perguntaIndex).get('configuracaoPontuacao.mapeamentoPontos') as FormGroup;
  }

  sincronizarMapeamentoPontos(etapaIndex: number, perguntaIndex: number): void {
    const opcoes = this.getOpcoes(etapaIndex, perguntaIndex);
    const mapeamento = this.getMapeamentoPontos(etapaIndex, perguntaIndex);

    // Adicionar controles para novas opções
    opcoes.controls.forEach(opcaoControl => {
      const valor = opcaoControl.value;
      if (valor && !mapeamento.get(valor)) {
        mapeamento.addControl(valor, this.fb.control(0));
      }
    });

    // Remover controles de opções que não existem mais
    Object.keys(mapeamento.controls).forEach(key => {
      const existe = opcoes.controls.some(c => c.value === key);
      if (!existe) {
        mapeamento.removeControl(key);
      }
    });
  }

  onTipoPerguntaChange(etapaIndex: number, perguntaIndex: number): void {
    const pergunta = this.getPerguntas(etapaIndex).at(perguntaIndex);
    const tipo = pergunta.get('tipo')?.value;
    const opcoes = pergunta.get('opcoes') as FormArray;
    const mapeamento = this.getMapeamentoPontos(etapaIndex, perguntaIndex);

    // Limpar opções e mapeamento existentes
    while (opcoes.length > 0) {
      opcoes.removeAt(0);
    }
    Object.keys(mapeamento.controls).forEach(key => {
      mapeamento.removeControl(key);
    });

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

  atualizarFormulario(): void {
    if (this.formularioForm.valid) {
      this.loading = true;
      const formularioData = this.prepararDados();

      this.formularioService.atualizarFormulario(this.formularioId, formularioData).subscribe({
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

          // Tratar erro específico de formulário com avaliações
          if (error.status === 400 && error.error?.error) {
            this.errorMessage = error.error.error;
          } else {
            this.errorMessage = 'Erro ao atualizar formulário: ' + (error.error?.message || error.message);
          }
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
      regraCalculoFinal: formValue.calculaPontuacao ? this.limparRegraCalculo(formValue.regraCalculoFinal) : undefined,
      etapas: formValue.etapas.map((etapa: any) => ({
        titulo: etapa.titulo,
        descricao: etapa.descricao,
        regraCalculoEtapa: formValue.calculaPontuacao ? this.limparRegraCalculo(etapa.regraCalculoEtapa) : undefined,
        perguntas: etapa.perguntas.map((pergunta: any) => ({
          texto: pergunta.texto,
          tipo: pergunta.tipo,
          opcoes: pergunta.opcoes || [],
          validacao: this.limparValidacao(pergunta.validacao),
          configuracaoPontuacao: formValue.calculaPontuacao ? this.limparConfiguracaoPontuacao(pergunta.configuracaoPontuacao) : undefined,
          metadadosCampo: this.limparMetadadosCampo(pergunta.metadadosCampo)
        }))
      }))
    };
  }

  private limparValidacao(validacao: any): any {
    return {
      min: validacao.min !== '' ? validacao.min : undefined,
      max: validacao.max !== '' ? validacao.max : undefined,
      required: validacao.required || false
    };
  }

  private limparConfiguracaoPontuacao(config: any): any {
    if (!config) return undefined;

    return {
      tipoPontuacao: config.tipoPontuacao,
      mapeamentoPontos: config.mapeamentoPontos || undefined,
      formula: config.formula || undefined,
      pontosMinimos: config.pontosMinimos !== '' ? config.pontosMinimos : undefined,
      pontosMaximos: config.pontosMaximos !== '' ? config.pontosMaximos : undefined
    };
  }

  private limparMetadadosCampo(metadados: any): any {
    if (!metadados) return undefined;

    const hasValues = metadados.subTipo || metadados.mascara || metadados.multiplaEscolha ||
                     metadados.minOpcoes !== '' || metadados.maxOpcoes !== '';

    if (!hasValues) return undefined;

    return {
      subTipo: metadados.subTipo || undefined,
      mascara: metadados.mascara || undefined,
      multiplaEscolha: metadados.multiplaEscolha || false,
      minOpcoes: metadados.minOpcoes !== '' ? metadados.minOpcoes : undefined,
      maxOpcoes: metadados.maxOpcoes !== '' ? metadados.maxOpcoes : undefined
    };
  }

  private limparRegraCalculo(regra: any): any {
    if (!regra) return undefined;

    return {
      tipoCalculo: regra.tipoCalculo,
      formulaCustom: regra.formulaCustom || undefined,
      pesos: (regra.pesos && Object.keys(regra.pesos).length > 0) ? regra.pesos : undefined
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
