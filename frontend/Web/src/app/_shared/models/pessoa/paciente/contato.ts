import { EnumParentesco } from "../../parentesco.enum";

export class Contato {
    constructor(
        public nome?: string,
        public telefone?: string,
        public parentesco?: EnumParentesco
    ) {}
}