import { Endereco } from "./endereco";

export class Pessoa {
    constructor(
        public id?: number,
        public nome?: string,
        public sexo?: string,
        public idade?: number,
        public endereco?: Endereco,
        public dataNasc?: Date,
        public cpf?: string,
        public telefone?: string,
        public senha?: string
    ) {}
}