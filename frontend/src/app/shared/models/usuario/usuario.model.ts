import { Endereco } from "./endereco.model";

export class Usuario {
    constructor(
        public id?: number,
        public cpf?: string,
        public nome?: string,
        public dataDeNasc?: Date,
        public email?: string,
        public sexo?: string,
        public endereco?: Endereco,
        public telefone?: string,
        public role?: string
    ) {}
}
