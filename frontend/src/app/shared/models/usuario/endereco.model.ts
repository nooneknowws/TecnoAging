export class Endereco {
    constructor(
        public cep: number,
        public logradouro: string,
        public numero: string,
        public complemento: string,
        public bairro: string,
        public cidade: string,
        public estado: string
    ){}
}
