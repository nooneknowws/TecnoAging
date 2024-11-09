import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Tecnico } from '../models/pessoa/tecnico/tecnico';

@Injectable({
  providedIn: 'root'
})
export class TecnicoService {
  private readonly API_URL = AppComponent.API_URL;

  constructor(private http: HttpClient) {}

  getTecnicos(): Observable<Tecnico[]> {
    return this.http.get<Tecnico[]>(`${this.API_URL}/tecnicos`);
  }

  getTecnicoById(id: number): Observable<Tecnico> {
    return this.http.get<Tecnico>(`${this.API_URL}/tecnicos/${id}`);
  }

  createTecnico(tecnico: Tecnico): Observable<Tecnico> {
    return this.http.post<Tecnico>(`${this.API_URL}/tecnicos`, tecnico);
  }

  updateTecnico(tecnico: Tecnico): Observable<Tecnico> {
    return this.http.put<Tecnico>(`${this.API_URL}/tecnicos/${tecnico.id}`, tecnico);
  }

  deleteTecnico(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/tecnicos/${id}`);
  }

  // Métodos adicionais específicos para Tecnico
  getTecnicosAtivos(): Observable<Tecnico[]> {
    return this.http.get<Tecnico[]>(`${this.API_URL}/tecnicos/ativos`);
  }

  toggleTecnicoStatus(id: number): Observable<Tecnico> {
    return this.http.patch<Tecnico>(`${this.API_URL}/tecnicos/${id}/toggle-status`, {});
  }

  getTecnicoByMatricula(matricula: number): Observable<Tecnico> {
    return this.http.get<Tecnico>(`${this.API_URL}/tecnicos/matricula/${matricula}`);
  }
}