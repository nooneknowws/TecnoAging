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
  getFormularioConfig(tipo: TipoFormulario): Observable<Formulario> {
    return of(FORMULARIOS_CONFIG[tipo]);
  }
}