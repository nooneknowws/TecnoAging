import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, tap, switchMap } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Avaliacao } from '../models/avaliacao/avaliacao';
import { AuthService } from './auth.service';
import { Tecnico } from '../models/pessoa/tecnico/tecnico';
import { Paciente } from '../models/pessoa/paciente/paciente';
import { PacienteService } from './paciente.service';

@Injectable({
  providedIn: 'root'
})
export class AvaliacaoService {
  private readonly API_URL = AppComponent.API_URL;
 
  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private pacienteService: PacienteService
  ) {}

  getAvaliacoes(): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(`${this.API_URL}/avaliacoes`).pipe(
      map(avaliacoes => avaliacoes.map(avaliacao => this.deserializeAvaliacao(avaliacao)))
    );
  }

  getAvaliacaoById(id: number): Observable<Avaliacao> {
    return this.http.get<Avaliacao>(`${this.API_URL}/avaliacoes/${id}`).pipe(
      map(avaliacao => this.deserializeAvaliacao(avaliacao))
    );
  }

  getAvaliacoesByPaciente(pacienteId: number): Observable<Avaliacao[]> {
    const params = new HttpParams().set('referenteA.id', pacienteId.toString());
    return this.http.get<Avaliacao[]>(`${this.API_URL}/avaliacoes`, { params }).pipe(
      map(avaliacoes => avaliacoes.map(avaliacao => this.deserializeAvaliacao(avaliacao)))
    );
  }

  salvarAvaliacao(avaliacao: Avaliacao, pacienteId?: number): Observable<Avaliacao> {
    const tecnico = this.authService.getCurrentUser() as Tecnico;
    avaliacao.preenchidoPor = tecnico;
    if (pacienteId) {
      return this.pacienteService.getPacienteById(pacienteId).pipe(
        switchMap(paciente => {
          avaliacao.referenteA = paciente;
          avaliacao.dataAtualizacao = new Date();
          
          if (avaliacao.id) {
            return this.http.put<Avaliacao>(
              `${this.API_URL}/avaliacoes/${avaliacao.id}`, 
              avaliacao
            );
          }
          
          avaliacao.dataCriacao = new Date();
          return this.http.post<Avaliacao>(
            `${this.API_URL}/avaliacoes`, 
            avaliacao
          );
        })
      );
    }

    avaliacao.dataAtualizacao = new Date();
    
    if (avaliacao.id) {
      return this.http.put<Avaliacao>(
        `${this.API_URL}/avaliacoes/${avaliacao.id}`, 
        avaliacao
      );
    }
    
    avaliacao.dataCriacao = new Date();
    return this.http.post<Avaliacao>(
      `${this.API_URL}/avaliacoes`, 
      avaliacao
    );
  }

  private deserializeAvaliacao(avaliacaoData: any): Avaliacao {
    return new Avaliacao(
      avaliacaoData.id,
      avaliacaoData.preenchidoPor,
      avaliacaoData.referenteA,
      avaliacaoData.formulario,
      avaliacaoData.pontuacaoTotal,
      avaliacaoData.pontuacaoMaxima,
      new Date(avaliacaoData.dataCriacao),
      new Date(avaliacaoData.dataAtualizacao)
    );
  }
}