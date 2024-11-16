import { Formulario } from "../formulario/formulario";
import { Paciente } from "../pessoa/paciente/paciente";
import { Tecnico } from "../pessoa/tecnico/tecnico";

export class Avaliacao {
    constructor(
        public id?: number,
        public preenchidoPor?: Tecnico,
        public referenteA?: Paciente,      
        public formulario?: Formulario,
        public pontuacaoTotal?: number,
        public pontuacaoMaxima?: number,
        public dataCriacao: Date = new Date(),
        public dataAtualizacao: Date = new Date()
    ){}
}
