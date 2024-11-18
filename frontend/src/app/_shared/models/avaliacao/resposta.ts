import { Pergunta } from '../formulario/pergunta';

export class Resposta {
  constructor(
    public pergunta?: Pergunta,
    public resposta = pergunta?.resposta
) {}
}
