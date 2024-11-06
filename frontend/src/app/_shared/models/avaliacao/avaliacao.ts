import { Paciente } from "../pessoa/paciente/paciente";
import { Tecnico } from "../pessoa/tecnico/tecnico";

export class Avaliacao {
    constructor(
        public preenchidoPor: Tecnico,
        public referenteA: Paciente,
        public dataPreenchimento: Date
    ){}
}
