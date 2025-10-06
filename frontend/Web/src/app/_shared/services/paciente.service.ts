import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Paciente } from '../models/pessoa/paciente/paciente';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PacienteService {
  private readonly API_URL = 'http://localhost:3000/api';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getPacientes(): Observable<Paciente[]> {
    return this.http.get<Paciente[]>(`${this.API_URL}/pacientes`);
  }

  getPacienteById(id: number): Observable<Paciente> {
    return this.http.get<Paciente>(`${this.API_URL}/pacientes/${id}`);
  }

  buscarPorCpf(cpf: string): Observable<Paciente> {
    return this.http.get<Paciente>(`${this.API_URL}/pacientes/cpf/${cpf}`);
  }

  createPaciente(paciente: Paciente): Observable<Paciente> {
    return this.http.post<Paciente>(`${this.API_URL}/pacientes`, paciente);
  }

  updatePaciente(paciente: Paciente): Observable<Paciente> {
    return this.http.put<Paciente>(`${this.API_URL}/pacientes/${paciente.id}`, paciente);
  }

  updatePacienteAndRefreshCache(paciente: Paciente): Observable<Paciente> {
    return this.http.put<Paciente>(`${this.API_URL}/pacientes/${paciente.id}`, paciente).pipe(
      tap(updatedPaciente => {
        // Se o paciente atualizado é o usuário logado, atualiza o cache
        const currentUser = this.authService.getCurrentUser();
        if (currentUser && currentUser.id === updatedPaciente.id) {
          this.authService.updateCurrentUserCache(updatedPaciente);
        }
      })
    );
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
    const params = new HttpParams().set('id', pacienteId.toString());
    return this.http.get<any[]>(`http://localhost:5000/avaliacoes/respostas/paciente/${pacienteId}`);
  }
}
