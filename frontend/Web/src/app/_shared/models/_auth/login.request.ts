export interface LoginRequest {
    cpf: string;
    senha: string;
    tipo: 'tecnico' | 'paciente' | '';
}