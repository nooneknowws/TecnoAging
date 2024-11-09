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

  getPacienteById(paciente: Paciente): Observable<Paciente> {
    return this.http.get<Paciente>(`${this.API_URL}/pacientes/${paciente.id}`);
  }
}
