export interface Validacao {
  min?: number;
  max?: number;
  required?: boolean;
}

export interface ConfiguracaoPontuacao {
  tipoPontuacao: string;
  mapeamentoPontos?: { [key: string]: number };
  formula?: string;
  pontosMinimos?: number;
  pontosMaximos?: number;
}

export interface MetadadosCampo {
  subTipo?: string;
  mascara?: string;
  multiplaEscolha?: boolean;
  minOpcoes?: number;
  maxOpcoes?: number;
}

export interface Pergunta {
    id?: number;
    texto: string;
    tipo: 'numero' | 'radio' | 'tempo' | 'dias' | 'texto' | 'checkbox' | 'range' | undefined;
    resposta?: string;
    opcoes?: string[];
    validacao?: Validacao;
    configuracaoPontuacao?: ConfiguracaoPontuacao;
    metadadosCampo?: MetadadosCampo;
}