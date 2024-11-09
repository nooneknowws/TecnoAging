import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Formulario } from '../models/formulario/formulario';

@Injectable({
  providedIn: 'root'
})
export class FormularioService {
  private readonly API_URL = AppComponent.API_URL;

  constructor(private http: HttpClient) {}

  getFormularios(): Observable<Formulario[]> {
    return this.http.get<Formulario[]>(`${this.API_URL}/formularios`);
  }

  getFormularioById(formulario: Formulario): Observable<Formulario> {
    return this.http.get<Formulario>(`${this.API_URL}/formularios/${formulario.id}`);
  }
}
