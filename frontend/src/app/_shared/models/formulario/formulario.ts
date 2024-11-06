import { Avaliacao } from "../avaliacao/avaliacao";

export class Formulario {
    constructor(
        public pontuacaoTotal: number,
        public avaliacao: Avaliacao
    ) {}
}