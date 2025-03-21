import { Pergunta } from "./pergunta";

export interface Etapa {
    titulo: string;
    descricao: string;
    perguntas: Pergunta[];
}