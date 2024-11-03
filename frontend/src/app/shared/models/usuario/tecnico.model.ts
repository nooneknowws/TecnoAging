import { Usuario } from "./usuario.model";

export class Tecnico extends Usuario {
    constructor(
        public matricula?: string,
        public setor?: string
    ) { super(); }
}
