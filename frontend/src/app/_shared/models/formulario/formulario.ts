import { TipoFormulario } from "../tipos.formulario.enum";
import { Etapa } from "./etapa";

export class Formulario {
  constructor(
    public id: number,
    public tipo: TipoFormulario | undefined,
    public titulo: string,
    public descricao: string,
    public etapas: Etapa[] | undefined
  ) {}
}