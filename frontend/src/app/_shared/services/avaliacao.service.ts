import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, tap, switchMap } from 'rxjs';
import { Avaliacao } from '../models/avaliacao/avaliacao';
import { AuthService } from './auth.service';
import { Tecnico } from '../models/pessoa/tecnico/tecnico';
import { PacienteService } from './paciente.service';
import { Resposta } from '../models/avaliacao/resposta';

@Injectable({
  providedIn: 'root'
})
export class AvaliacaoService {
  private apiUrl = 'http://localhost:5000/avaliacoes'; 
 
  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private pacienteService: PacienteService
  ) {}

  getAvaliacoes(): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(`${this.apiUrl}/forms`).pipe(
      map(avaliacoes => avaliacoes.map(avaliacao => this.deserializeAvaliacao(avaliacao)))
    );
  }

  getAvaliacaoById(id: number): Observable<Avaliacao> {
    return this.http.get<Avaliacao>(`${this.apiUrl}/avaliacao/${id}`).pipe(
      map(avaliacao => this.deserializeAvaliacao(avaliacao))
    );
  }

  getAvaliacaoByIdTecnico(tecnicoId: number): Observable<Avaliacao> {
    return this.http.get<Avaliacao>(`${this.apiUrl}/respostas/tecnico/${tecnicoId}`).pipe(
      map(avaliacao => this.deserializeAvaliacao(avaliacao))
    );
  }

  getAvaliacoesByPaciente(pacienteId: number): Observable<Avaliacao[]> {
    const params = new HttpParams().set('paciente.id', pacienteId.toString());
    return this.http.get<Avaliacao[]>(`${this.apiUrl}/respostas/paciente/${pacienteId}`, { params }).pipe(
      map(avaliacoes => avaliacoes.map(avaliacao => this.deserializeAvaliacao(avaliacao)))
    );
  }

  salvarAvaliacao(avaliacao: Avaliacao, pacienteId?: number): Observable<Avaliacao> {
    const tecnico = this.authService.getCurrentUser() as Tecnico;
    avaliacao.tecnico = tecnico;
    
  
    if (pacienteId) {
      return this.pacienteService.getPacienteById(pacienteId).pipe(
        switchMap(paciente => {
          avaliacao.paciente = paciente;
          avaliacao.dataAtualizacao = new Date();
          console.log(typeof avaliacao.paciente.id)
  
          const avaliacaoSerializada = this.serializeAvaliacao(avaliacao);
  
          console.log('JSON enviado para a API (com pacienteId):', avaliacaoSerializada);
  
          if (avaliacao.id) {
            return this.http.put<Avaliacao>(
              `${this.apiUrl}/forms/update/${avaliacao.id}`,
              avaliacaoSerializada
            );
          }
  
          avaliacao.dataCriacao = new Date();
          return this.http.post<Avaliacao>(
            `${this.apiUrl}/forms`,
            avaliacaoSerializada
          );
        })
      );
    }
  
    avaliacao.dataAtualizacao = new Date();
    const avaliacaoSerializada = this.serializeAvaliacao(avaliacao);
  
    console.log('JSON enviado para a API:', avaliacaoSerializada);
  
    if (avaliacao.id) {
      return this.http.put<Avaliacao>(
        `${this.apiUrl}/forms/update/${avaliacao.id}`,
        avaliacaoSerializada
      );
    }
  
    avaliacao.dataCriacao = new Date();
    return this.http.post<Avaliacao>(
      `${this.apiUrl}/forms`,
      avaliacaoSerializada
    );
  }
  
  
  private serializeAvaliacao(avaliacao: Avaliacao): any {
    console.log(typeof avaliacao?.paciente?.id);
    console.log(typeof avaliacao.paciente?.id);
  
    return {
      pacienteId: avaliacao.paciente?.id,
      tecnicoId: avaliacao.tecnico?.id,
      formularioId: avaliacao.formulario?.id,
      respostas: avaliacao.respostas?.map(resposta => ({
        perguntaId: resposta.pergunta?.id,
        valor: Array.isArray(resposta.resposta) 
                 ? resposta.resposta.join(', ')
                 : resposta.resposta 
      })) || [],
      pontuacaoTotal: avaliacao.pontuacaoTotal,
      pontuacaoMaxima: avaliacao.pontuacaoMaxima,
      dataCriacao: avaliacao.dataCriacao,
      dataAtualizacao: avaliacao.dataAtualizacao
    };
  }
  
  
  private deserializeAvaliacao(avaliacaoData: any): Avaliacao {
    const tecnico = {
      id: avaliacaoData.tecnicoId,
      nome: avaliacaoData.tecnico
    };

    const paciente = {
      id: avaliacaoData.pacienteId,
      nome: avaliacaoData.paciente
    };

    const formulario = {
      id: avaliacaoData.avaliacaoId,
      tipo: undefined,
      titulo: avaliacaoData.formulario,
      descricao: avaliacaoData.formularioDesc,
      etapas: undefined
    };

    const respostas = avaliacaoData.perguntasValores?.map((item: any) => {
      const pergunta = {
        id: undefined,
        texto: item.pergunta,
        tipo: undefined, 
        resposta: item.valor
      };

      return new Resposta(
        pergunta,
        item.valor
      );
    }) || [];

    return new Avaliacao(
      avaliacaoData.avaliacaoId,
      tecnico,
      paciente,
      formulario,
      respostas,
      avaliacaoData.pontuacaoTotal,
      avaliacaoData.pontuacaoMaxima,
      new Date(), // Since dataCriacao is not in the new JSON, using current date
      new Date()  // Since dataAtualizacao is not in the new JSON, using current date
    );
  }
}