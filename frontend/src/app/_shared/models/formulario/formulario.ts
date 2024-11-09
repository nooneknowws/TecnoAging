import { Avaliacao } from "../avaliacao/avaliacao";

export class Formulario {
    constructor(
        public id: number,
        public pontuacaoTotal: number,
        public avaliacao: Avaliacao
    ) {}
}