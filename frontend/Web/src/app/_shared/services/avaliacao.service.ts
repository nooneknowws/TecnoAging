import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map, tap, switchMap } from 'rxjs';
import { Avaliacao } from '../models/avaliacao/avaliacao';
import { AuthService } from './auth.service';
import { Tecnico } from '../models/pessoa/tecnico/tecnico';
import { PacienteService } from './paciente.service';
import { Resposta } from '../models/avaliacao/resposta';
import { AppComponent } from '../../app.component';

@Injectable({
  providedIn: 'root'
})
export class AvaliacaoService {
  private readonly API_URL = AppComponent.API_URL + '/avaliacoes';
 
  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private pacienteService: PacienteService
  ) {}

  getAvaliacoes(): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(`${this.API_URL}/forms`).pipe(
      map(avaliacoes => avaliacoes.map(avaliacao => this.deserializeAvaliacao(avaliacao)))
    );
  }

  getAvaliacaoById(id: number): Observable<Avaliacao> {
    return this.http.get<Avaliacao>(`${this.API_URL}/avaliacao/${id}`).pipe(
      map(avaliacao => this.deserializeAvaliacao(avaliacao))
    );
  }

  getAvaliacaoByIdTecnico(tecnicoId: number): Observable<Avaliacao> {
    return this.http.get<Avaliacao>(`${this.API_URL}/respostas/tecnico/${tecnicoId}`).pipe(
      map(avaliacao => this.deserializeAvaliacao(avaliacao))
    );
  }

  getAvaliacoesByPaciente(pacienteId: number): Observable<Avaliacao[]> {
    const params = new HttpParams().set('paciente.id', pacienteId.toString());
    return this.http.get<Avaliacao[]>(`${this.API_URL}/respostas/paciente/${pacienteId}`, { params }).pipe(
      map(avaliacoes => avaliacoes.map(avaliacao => this.deserializeAvaliacao(avaliacao)))
    );
  }

  buscarPorPaciente(idPaciente: number): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(`${this.API_URL}/respostas/paciente/${idPaciente}`).pipe(
      map(avaliacoes => avaliacoes.map(avaliacao => this.deserializeAvaliacao(avaliacao)))
    );
  }

  createAvaliacao(avaliacao: Avaliacao, pacienteId?: number): Observable<Avaliacao> {
    const tecnico = this.authService.getCurrentUser() as Tecnico;
    avaliacao.tecnico = tecnico;
    
    if (pacienteId) {
      return this.pacienteService.getPacienteById(pacienteId).pipe(
        switchMap(paciente => {
          avaliacao.paciente = paciente;
          avaliacao.dataCriacao = new Date();
          
          const serialized = this.serializeAvaliacao(avaliacao);
          
          return this.http.post<Avaliacao>(`${this.API_URL}/forms`, serialized);
        })
      );
    }
    
    avaliacao.dataCriacao = new Date();
    const serialized = this.serializeAvaliacao(avaliacao);
    
    return this.http.post<Avaliacao>(`${this.API_URL}/forms`, serialized);
  }

  updateAvaliacao(avaliacao: Avaliacao): Observable<Avaliacao> {
    const tecnico = this.authService.getCurrentUser() as Tecnico;
    avaliacao.tecnico = tecnico;
    avaliacao.dataAtualizacao = new Date();
    
    const serialized = this.serializeAvaliacao(avaliacao);
    
    return this.http.put<Avaliacao>(`${this.API_URL}/forms/update/${avaliacao.id}`, serialized);
  }

  private serializeAvaliacao(avaliacao: Avaliacao): any {
    return {
      avaliacaoId: avaliacao.id,
      pacienteId: avaliacao.paciente?.id,
      pacienteNome: avaliacao.paciente?.nome,
      pacienteIdade: avaliacao.paciente?.idade,
      pacienteIMC: avaliacao.paciente?.imc,
      tecnicoId: avaliacao.tecnico?.id,
      tecnicoNome: avaliacao.tecnico?.nome,
      formularioId: avaliacao.formulario?.id,
      respostas: avaliacao.respostas?.map(resposta => {
        let serializedValue: string;
        
        if (Array.isArray(resposta.valor)) {
          serializedValue = JSON.stringify(resposta.valor);
        } else {
          serializedValue = JSON.stringify(resposta.valor);
        }
        
        return {
          perguntaId: resposta.pergunta?.id,
          valor: serializedValue
        };
      }),
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
      id: avaliacaoData.formularioId,
      tipo: undefined,
      titulo: avaliacaoData.formulario,
      descricao: avaliacaoData.formularioDesc,
      etapas: undefined
    };

    const respostas = avaliacaoData.perguntasValores?.map((item: any) => {
      const pergunta = {
        id: item.perguntaId,
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
      new Date(avaliacaoData.dataCriacao),
      new Date(avaliacaoData.dataAtualizacao)
    );
  }
}