import { Endereco } from "../endereco";
import { Pessoa } from "../pessoa";
import { Contato } from "./contato";

export class Paciente extends Pessoa {
    constructor(
        public peso?: number,
        public imc?: number,
        public altura?: number,
        public socioeconomico?: string,
        public escolaridade?: string,
        public contatos?: Contato[],
        id?: number,
        nome?: string,
        sexo?: string,
        idade?: number,
        endereco?: Endereco,
        dataNasc?: Date,
        cpf?: string,
        telefone?: string
    ) {
        super(id, nome, sexo, idade, endereco, dataNasc, cpf, telefone);
    }
}