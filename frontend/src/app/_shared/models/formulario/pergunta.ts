export interface Pergunta {
    id: number | undefined,
    texto: string;
    tipo: 'numero' | 'radio' | 'tempo' | 'dias' | 'texto' | 'checkbox' | 'range' | undefined;
    opcoes?: string[];
    validacao?: {
      min?: number;
      max?: number;
      required?: boolean;
    };
}