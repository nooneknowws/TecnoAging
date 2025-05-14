import { EnumEstadoCivil } from "../../estadocivil.enum";
import { EnumEstadosBrasil } from "../../estadosbrasil.enum";
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
        public estadoCivil?: EnumEstadoCivil,
        public nacionalidade?: string,
        public municipioNasc?: string,
        public ufNasc?: EnumEstadosBrasil,
        public corRaca?: string,
        public fotoUrl?: string,
        public rg?: string,
        public dataExpedicao?: Date,
        public orgaoEmissor?: string,
        public ufEmissor?: EnumEstadosBrasil,
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
    static fromJSON(json: any): Paciente {
        return Object.assign(new Paciente(), json);
      }
}