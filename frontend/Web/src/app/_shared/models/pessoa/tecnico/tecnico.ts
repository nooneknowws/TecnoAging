import { Endereco } from "../endereco";
import { Pessoa } from "../pessoa";

export class Tecnico extends Pessoa {
    constructor(
        public matricula?: number,
        public ativo?: boolean,
        public fotoUrl?: string,
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
    static fromJSON(json: any): Tecnico {
        return Object.assign(new Tecnico(), json);
      }
}