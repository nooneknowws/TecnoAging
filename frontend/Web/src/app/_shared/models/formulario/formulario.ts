import { Etapa } from "./etapa";

export interface RegraCalculo {
  tipoCalculo: string;
  formulaCustom?: string;
  pesos?: { [key: number]: number };
}

export class Formulario {
  constructor(
    public id: number,
    public tipo: string | undefined,
    public titulo: string,
    public descricao: string,
    public etapas: Etapa[] | undefined,
    public calculaPontuacao: boolean = false,
    public regraCalculoFinal?: RegraCalculo
  ) {}
}