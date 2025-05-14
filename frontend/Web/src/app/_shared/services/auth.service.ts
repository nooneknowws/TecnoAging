import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError, switchMap, timeout, delay, tap } from 'rxjs/operators';
import { Tecnico } from '../models/pessoa/tecnico/tecnico';
import { Paciente } from '../models/pessoa/paciente/paciente';
import { LoginRequest } from '../models/_auth/login.request';
import { LoginResponse } from '../models/_auth/login.response';
import { AppComponent } from '../../app.component';
import { Endereco } from '../models/pessoa/endereco';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = AppComponent.API_URL;
  private readonly SESSION_TOKEN_KEY = 'token';
  private readonly SESSION_USER_TYPE_KEY = 'userType';
  private readonly SESSION_USER_DATA_KEY = 'userData';
  private readonly SESSION_USER_ID_KEY = 'userID';
  
  private currentUser: Tecnico | Paciente | null = null;

  constructor(private http: HttpClient) {
    this.initializeFromSession();
  }

  private initializeFromSession(): void {
    const userData =  localStorage.getItem(this.SESSION_USER_DATA_KEY);
    const userType =  localStorage.getItem(this.SESSION_USER_TYPE_KEY);
    
    if (userData && userType) {
      const parsedUser = JSON.parse(userData);
      this.currentUser = userType === 'tecnico' 
        ? Tecnico.fromJSON(parsedUser)
        : Paciente.fromJSON(parsedUser);
    }
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/auth/login`, loginRequest).pipe(
      switchMap(loginResponse => {
        this.storeSessionData(loginResponse.ID!, loginResponse.Perfil!, loginResponse.token!);
        
        const endpoint = loginResponse.Perfil === 'PACIENTE' 
          ? `http://localhost:3000/api/pacientes/${loginResponse.ID}`
          : `http://localhost:5002/api/tecnicos/${loginResponse.ID}`;
  
        return this.http.get<any>(endpoint, {
          headers: { Authorization: `Bearer ${loginResponse.token}` }
        }).pipe(
          map(userData => {
            this.storeUserData(userData, loginResponse.Perfil!);
            return loginResponse;
          }),
          catchError(error => {
            console.error('Erro ao buscar dados do usuário:', error);
            return of(loginResponse);
          })
        );
      })
    );
  }

  registrarTecnico(tecnico: Tecnico): Observable<Tecnico> {
    tecnico.ativo = true;
    console.log(tecnico);
    return this.http.post<Tecnico>(`${this.API_URL}/tecnicos`, tecnico);
  }

  registrarPaciente(paciente: Paciente): Observable<Paciente> {
    return this.http.post<Paciente>(`${this.API_URL}/auth/pacientes`, paciente)
  }

  private storeUserData(userData: any, perfil: string): void {
    localStorage.setItem(this.SESSION_USER_DATA_KEY, JSON.stringify(userData));
    this.currentUser = perfil.toLowerCase() === 'tecnico' 
      ? Object.assign(new Tecnico(), userData)
      : Object.assign(new Paciente(), userData);
  }

  private storeSessionData(ID: string, perfil: string, token: string): void {
    localStorage.setItem(this.SESSION_TOKEN_KEY, token);
    localStorage.setItem(this.SESSION_USER_TYPE_KEY, perfil.toLowerCase());
    localStorage.setItem(this.SESSION_USER_ID_KEY, ID);
  }
  

  logout(): void {
     localStorage.clear();
    this.currentUser = null;
    this.http.post(`${this.API_URL}/auth/logout`, null).subscribe({
      next: () => console.log('Logged out successfully'),
      error: (err) => console.error('Logout error:', err)
    });
  }

  getCurrentUser(): Tecnico | Paciente | null {
    return this.currentUser;
  }

  updateCurrentUser(user: any): void {
     localStorage.setItem('userData', JSON.stringify(user));
  }

  getUserProfile(): string | null {
    return localStorage.getItem(this.SESSION_USER_TYPE_KEY);
  }
  
  isLoggedIn(): boolean {
    return !! localStorage.getItem(this.SESSION_TOKEN_KEY);
  }

  buscarCep(cep: string): Observable<Endereco | null> {
    console.log('oi');
    const sanitizedCep = cep.replace(/\D/g, '');
    if (sanitizedCep.length !== 8) {
      return of(null);
    }
  
    return this.http.get<any>(`https://viacep.com.br/ws/${sanitizedCep}/json/`).pipe(
      timeout(10000),
      map(data => {
        if (data.erro) {
          console.log(data.erro)
          return null;
        }
        return {
          cep: parseInt(sanitizedCep, 10),
          logradouro: data.logradouro,
          complemento: data.complemento || '',
          bairro: data.bairro,
          municipio: data.localidade,
          uf: data.uf
        } as Endereco;
      }),
      catchError(error => {
        console.error('Erro ao buscar CEP:', error);
        return of(null);
      })
    );
  }
}