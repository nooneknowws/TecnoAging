import { EnumEstadosBrasil } from "../estadosbrasil.enum";

export class Endereco {
    constructor(
        public cep?: number | string,
        public logradouro?: string,
        public numero?: number,
        public complemento?: string,
        public bairro?: string,
        public municipio?: string,
        public uf?: EnumEstadosBrasil
    ) {}
}