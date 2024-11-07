export interface AuthUser {
    cpf: string;
    senha: string;
    tipo: 'tecnico' | 'paciente';
    userId: number;
}