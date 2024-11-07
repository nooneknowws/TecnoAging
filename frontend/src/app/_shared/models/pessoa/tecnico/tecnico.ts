import { Endereco } from "../endereco";
import { Pessoa } from "../pessoa";

export class Tecnico extends Pessoa {
    constructor(
        public matricula?: number,
        public ativo?: boolean,
        nome?: string,
        sexo?: string,
        idade?: number,
        endereco?: Endereco,
        dataNasc?: Date,
        cpf?: string,
        telefone?: string
    ) {
        super(nome, sexo, idade, endereco, dataNasc, cpf, telefone);
    }
}