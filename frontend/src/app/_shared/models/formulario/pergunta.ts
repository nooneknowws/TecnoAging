export interface Pergunta {
    id: number | undefined,
    texto: string;
    tipo: 'numero' | 'radio' | 'tempo' | 'dias' | 'texto' | 'checkbox' | 'range' | undefined;
    resposta: string | string[] | number | boolean | undefined;
    opcoes?: string[];
    validacao?: {
      min?: number;
      max?: number;
      required?: boolean;
    };
}