import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Tecnico } from '../models/pessoa/tecnico/tecnico';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class TecnicoService {
  private readonly API_URL = AppComponent.API_URL;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

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

  ativarTecnico(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API_URL}/tecnicos/${id}/ativar`, {});
  }

  desativarTecnico(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API_URL}/tecnicos/${id}/desativar`, {});
  }

  getTecnicoByMatricula(matricula: number): Observable<Tecnico> {
    return this.http.get<Tecnico>(`${this.API_URL}/tecnicos/matricula/${matricula}`);
  }

  updateTecnicoAndRefreshCache(tecnico: Tecnico): Observable<Tecnico> {
    return this.http.put<Tecnico>(`${this.API_URL}/tecnicos/${tecnico.id}`, tecnico).pipe(
      tap(updatedTecnico => {
        // Se o técnico atualizado é o usuário logado, atualiza o cache
        const currentUser = this.authService.getCurrentUser();
        if (currentUser && currentUser.id === updatedTecnico.id) {
          this.authService.updateCurrentUserCache(updatedTecnico);
        }
      })
    );
  }
}