import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Paciente } from '../models/pessoa/paciente/paciente';

@Injectable({
  providedIn: 'root'
})
export class PacienteService {
  private readonly API_URL = AppComponent.API_URL;

  constructor(private http: HttpClient) {}

  getPacientes(): Observable<Paciente[]> {
    return this.http.get<Paciente[]>(`${this.API_URL}/pacientes`);
  }

  getPacienteById(id: number): Observable<Paciente> {
    return this.http.get<Paciente>(`${this.API_URL}/pacientes/${id}`);
  }

  createPaciente(paciente: Paciente): Observable<Paciente> {
    return this.http.post<Paciente>(`${this.API_URL}/pacientes`, paciente);
  }

  updatePaciente(paciente: Paciente): Observable<Paciente> {
    return this.http.put<Paciente>(`${this.API_URL}/pacientes/${paciente.id}`, paciente);
  }

  deletePaciente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/pacientes/${id}`);
  }

  // TODO: Fitros no backend
  getPacientesByEscolaridade(escolaridade: string): Observable<Paciente[]> {
    return this.http.get<Paciente[]>(`${this.API_URL}/pacientes/escolaridade/${escolaridade}`);
  }

  getPacientesBySocioeconomico(socioeconomico: string): Observable<Paciente[]> {
    return this.http.get<Paciente[]>(`${this.API_URL}/pacientes/socioeconomico/${socioeconomico}`);
  }

  calculateIMC(peso: number, altura: number): number {
    return peso / (altura * altura);
  }

  updatePacienteIMC(paciente: Paciente): Observable<Paciente> {
    paciente.imc = this.calculateIMC(paciente.peso!, paciente.altura!);
    return this.updatePaciente(paciente);
  }

  getHistoricoTestes(pacienteId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/pacientes/${pacienteId}/historico-testes`);
  }
}
