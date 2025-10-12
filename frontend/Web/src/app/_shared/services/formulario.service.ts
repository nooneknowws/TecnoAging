import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Formulario } from '../models/formulario/formulario';

export interface FormularioDTO {
  tipo: string;
  titulo: string;
  descricao: string;
  calculaPontuacao: boolean;
  regraCalculoFinal?: any;
  etapas: any[];
}

@Injectable({
  providedIn: 'root',
})
export class FormularioService {
  private apiUrl = 'http://localhost:3000/api/formularios';

  constructor(private http: HttpClient) {}

  private getHttpOptions() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      })
    };
  }

  listarFormularios(): Observable<Formulario[]> {
    return this.http.get<Formulario[]>(`${this.apiUrl}/`, this.getHttpOptions());
  }

  criarFormulario(formulario: FormularioDTO): Observable<Formulario> {
    return this.http.post<Formulario>(`${this.apiUrl}/cadastro`, formulario, this.getHttpOptions());
  }

  atualizarFormulario(id: number, formulario: FormularioDTO): Observable<Formulario> {
    return this.http.put<Formulario>(`${this.apiUrl}/${id}`, formulario, this.getHttpOptions());
  }

  deletarFormulario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.getHttpOptions());
  }

  getFormularioPorId(id: number): Observable<Formulario> {
    return this.http.get<Formulario>(`${this.apiUrl}/${id}`, this.getHttpOptions());
  }

  getTiposSugeridos(): { value: string, label: string }[] {
    return [
      { value: 'SEDENTARISMO', label: 'Nível de Atividade Física' },
      { value: 'IVCF20', label: 'Vulnerabilidade Clínico Funcional' },
      { value: 'PFS', label: 'Fatigabilidade de Pittsburgh' },
      { value: 'MINIMENTAL', label: 'Mini Exame do Estado Mental' },
      { value: 'FACTF', label: 'FACT-F' },
      { value: 'CUSTOM', label: 'Personalizado' }
    ];
  }
}
