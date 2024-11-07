import { Paciente } from "../pessoa/paciente/paciente";
import { Tecnico } from "../pessoa/tecnico/tecnico";

export interface LoginResponse {
    success: boolean;
    user: Tecnico | Paciente | null;
    tipo?: 'tecnico' | 'paciente';
    token?: string;
    message?: string;
}
