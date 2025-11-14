import { Formulario } from "../formulario/formulario";
import { Paciente } from "../pessoa/paciente/paciente";
import { Tecnico } from "../pessoa/tecnico/tecnico";
import { Resposta } from "./resposta";

export class Avaliacao {
    constructor(
        public id?: number,
        public tecnico?: Tecnico | null,
        public paciente?: Paciente,      
        public formulario?: Formulario,
        public respostas?: Resposta[],
        public pontuacaoTotal?: number,
        public pontuacaoMaxima?: number,
        public dataCriacao: Date = new Date(),
        public dataAtualizacao: Date = new Date()
    ){}
}
