import { Pergunta } from "./pergunta";
import { RegraCalculo } from "./formulario";

export interface Etapa {
    id?: number;
    titulo: string;
    descricao: string;
    perguntas: Pergunta[];
    regraCalculoEtapa?: RegraCalculo;
}