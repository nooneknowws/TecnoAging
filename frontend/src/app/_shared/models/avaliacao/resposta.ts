import { Pergunta } from '../formulario/pergunta';

export class Resposta {
  constructor(
    public pergunta?: Pergunta,
    public resposta?: string | string[] | number | boolean | undefined
) {}
}
