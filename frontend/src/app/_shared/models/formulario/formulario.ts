import { TipoFormulario } from "../tipos.formulario.enum";
import { Etapa } from "./etapa";

export class Formulario {
  constructor(
    public id: number,
    public tipo: TipoFormulario,
    public titulo: string,
    public descricao: string,
    public etapas: Etapa[],
    public pontuacaoMaxima?: number
  ) {}
}