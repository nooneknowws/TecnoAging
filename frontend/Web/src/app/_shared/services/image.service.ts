import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private baseUrl = 'http://localhost:3000';

  constructor(private http: HttpClient) {}

  uploadPacientePhoto(pacienteId: number, imageBase64: string): Observable<any> {
    const url = `${this.baseUrl}/api/pacientes/${pacienteId}/foto`;
    const body = { image: imageBase64 };
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post(url, body, { headers });
  }

  getPacientePhoto(pacienteId: number): Observable<any> {
    const url = `${this.baseUrl}/api/pacientes/${pacienteId}/foto`;
    return this.http.get(url);
  }

  uploadTecnicoPhoto(tecnicoId: number, imageBase64: string): Observable<any> {
    const url = `${this.baseUrl}/api/tecnicos/${tecnicoId}/foto`;
    const body = { image: imageBase64 };
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post(url, body, { headers });
  }

  getTecnicoPhoto(tecnicoId: number): Observable<any> {
    const url = `${this.baseUrl}/api/tecnicos/${tecnicoId}/foto`;
    return this.http.get(url);
  }
}