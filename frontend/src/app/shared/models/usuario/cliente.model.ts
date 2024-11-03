import { Usuario } from "./usuario.model";
import { Contatos } from "./contatos.model";

export class Cliente extends Usuario {
    constructor(
        public contatos?: [Contatos],
        public escolaridade?: string,
        public socioeconomico?: string,
        public peso?: number,
        public altura?: number,
        public IMC?: number
    ){ super() }
}
