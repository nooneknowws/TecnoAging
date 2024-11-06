import { Endereco } from "./endereco";

export class Pessoa {
    constructor(
        public nome?: string,
        public sexo?: string,
        public idade?: number,
        public endereco?: Endereco,
        public dataNasc?: Date,
        public cpf?: number,
        public telefone?: string
    ) {}
}