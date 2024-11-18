export interface Pergunta {
    id: number,
    texto: string;
    tipo: 'numero' | 'radio' | 'tempo' | 'dias' | 'texto' | 'checkbox' | 'range';
    resposta: string | string[] | number | boolean |undefined;
    opcoes?: string[];
    validacao?: {
      min?: number;
      max?: number;
      required?: boolean;
    };
}