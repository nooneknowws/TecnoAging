export interface Pergunta {
    texto: string;
    tipo: 'numero' | 'radio' | 'tempo' | 'dias' | 'texto' | 'checkbox' | 'range';
    resposta: string | string[] | number | boolean ;
    opcoes?: string[];
    validacao?: {
      min?: number;
      max?: number;
      required?: boolean;
      pattern?: string;
    };
}