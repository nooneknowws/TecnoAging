import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, of } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Formulario } from '../models/formulario/formulario';
import { Avaliacao } from '../models/avaliacao/avaliacao';
import { TipoFormulario } from '../models/tipos.formulario.enum';
import { FORMULARIOS_CONFIG } from '../models/formulario/formularios.config';

@Injectable({
  providedIn: 'root'
})
export class FormularioService {
  private readonly API_URL = AppComponent.API_URL;
  
  constructor(private http: HttpClient) {}

  getFormularioConfig(tipo: TipoFormulario): Observable<Formulario> {
    return of(FORMULARIOS_CONFIG[tipo]);
  }

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

  salvarAvaliacao(avaliacao: Avaliacao): Observable<Avaliacao> {
    avaliacao.dataAtualizacao = new Date();
    if (avaliacao.id) {
      return this.http.put<Avaliacao>(`${this.API_URL}/avaliacoes/${avaliacao.id}`, avaliacao);
    }
    avaliacao.dataCriacao = new Date();
    return this.http.post<Avaliacao>(`${this.API_URL}/avaliacoes`, avaliacao);
  }

  private deserializeAvaliacao(avaliacaoData: any): Avaliacao {
    return new Avaliacao(
      avaliacaoData.id,
      avaliacaoData.preenchidoPor,
      avaliacaoData.referenteA,
      avaliacaoData.formulario,
      avaliacaoData.pontuacaoTotal,
      new Date(avaliacaoData.dataCriacao),
      new Date(avaliacaoData.dataAtualizacao)
    );
  }
}