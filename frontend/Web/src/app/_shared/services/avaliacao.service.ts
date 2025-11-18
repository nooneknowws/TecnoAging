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
    return this.http.get<any>(`${this.API_URL}/avaliacao/${id}`).pipe(
      map(avaliacaoData => this.deserializeAvaliacao(avaliacaoData))
    );
  }

  getAvaliacaoByIdTecnico(tecnicoId: number): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(`${this.API_URL}/respostas/tecnico/${tecnicoId}`).pipe(
      map(avaliacoes => avaliacoes.map(avaliacao => this.deserializeAvaliacao(avaliacao)))
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
    const currentUser = this.authService.getCurrentUser();
    const userProfile = this.authService.getUserProfile();

    // Determinar tecnicoId baseado no tipo de usuário
    if (userProfile === 'paciente') {
      avaliacao.tecnico = null;

      // Se paciente está preenchendo, usar seu próprio ID
      if (!pacienteId && currentUser && 'id' in currentUser) {
        pacienteId = currentUser.id;
      }
    } else {
      avaliacao.tecnico = currentUser as Tecnico;
    }

    // Se temos pacienteId (fornecido ou do usuário logado), buscar dados completos
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

    // Fallback: não deveria chegar aqui, mas serializa mesmo assim
    console.warn('Criando avaliação sem pacienteId - isso pode causar erro no backend');
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
      tecnicoId: avaliacao.tecnico?.id ?? null,
      tecnicoNome: avaliacao.tecnico?.nome ?? null,
      formularioId: avaliacao.formulario?.id,
      respostas: avaliacao.respostas?.map(resposta => {
        let serializedValue: string;

        if (Array.isArray(resposta.valor)) {
          // Arrays precisam de JSON.stringify
          serializedValue = JSON.stringify(resposta.valor);
        } else if (typeof resposta.valor === 'string') {
          // Strings já vêm prontas
          serializedValue = resposta.valor;
        } else {
          // Números, booleanos, etc convertidos para string
          serializedValue = String(resposta.valor);
        }

        return {
          perguntaId: resposta.pergunta?.id,
          valor: serializedValue
        };
      }),
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
      nome: avaliacaoData.paciente,
      idade: avaliacaoData.pacienteIDADE,
      imc: avaliacaoData.pacienteIMC
    };

    const formulario = {
      id: avaliacaoData.formularioId,
      tipo: undefined,
      titulo: avaliacaoData.formulario,
      descricao: avaliacaoData.formularioDesc,
      etapas: undefined,
      calculaPontuacao: false
    };

    // Transform the new response format to the expected format
    const respostas = (avaliacaoData.respostas || []).map((resposta: any) => ({
      pergunta: resposta.pergunta,
      valor: resposta.valor
    }));

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