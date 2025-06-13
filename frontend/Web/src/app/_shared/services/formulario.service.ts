import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Formulario } from '../models/formulario/formulario';  
import { TipoFormulario } from '../models/tipos.formulario.enum';

@Injectable({
  providedIn: 'root',
})
export class FormularioService {
  private apiUrl = 'http://localhost:3000/api/formularios'; 

  constructor(private http: HttpClient) {}

  getFormularioConfig(tipo: TipoFormulario): Observable<Formulario> {
    console.log(localStorage.getItem('token'))
    return this.http.get<Formulario>(`${this.apiUrl}/${this.mapTipoToId(tipo)}`);
  }

  listarFormularios(): Observable<Formulario[]> {
    return this.http.get<Formulario[]>(`${this.apiUrl}`);
  }

  private mapTipoToId(tipo: TipoFormulario): number {
    switch (tipo) {
      case TipoFormulario.PFS:
        return 1;
      case TipoFormulario.MINIMENTAL:
        return 2;
      case TipoFormulario.IVCF20:
        return 3;
      case TipoFormulario.SEDENTARISMO:
        return 4;
      case TipoFormulario.FACTF:
        return 5;
      default:
        throw new Error('Tipo de formulário inválido');
    }
  }
}
