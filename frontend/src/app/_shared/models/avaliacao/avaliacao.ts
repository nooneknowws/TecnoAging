import { Paciente } from "../pessoa/paciente/paciente";
import { Tecnico } from "../pessoa/tecnico/tecnico";

export class Avaliacao {
    constructor(
        public id: number,
        public preenchidoPor: Tecnico,
        public referenteA: Paciente,
        public dataPreenchimento: Date
    ){}
}
