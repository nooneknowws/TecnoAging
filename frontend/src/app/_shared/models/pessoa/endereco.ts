export class Endereco {
    constructor(
        public CEP?: number,
        public logradouro?: string,
        public numero?: number,
        public complemento?: string,
        public bairro?: string,
        public municipio?: string,
        public UF?: string
    ) {}
}