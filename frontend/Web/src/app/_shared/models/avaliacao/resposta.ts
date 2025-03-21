import { Pergunta } from '../formulario/pergunta';

export class Resposta {
  constructor(
    public pergunta?: Pergunta,
    public valor?: string | string[] | number | boolean | undefined
) {}
}
