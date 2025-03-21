import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable, of } from 'rxjs';
import { map, catchError, switchMap, timeout, delay, tap } from 'rxjs/operators';
import { Tecnico } from '../models/pessoa/tecnico/tecnico';
import { Paciente } from '../models/pessoa/paciente/paciente';
import { LoginRequest } from '../models/_auth/login.request';
import { LoginResponse } from '../models/_auth/login.response';
import { AuthUser } from '../models/_auth/auth.user';
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
  
  private currentUser: Tecnico | Paciente | null = null;

  constructor(private http: HttpClient) {
    this.initializeFromSession();
  }

  private initializeFromSession(): void {
    const userData = sessionStorage.getItem(this.SESSION_USER_DATA_KEY);
    const userType = sessionStorage.getItem(this.SESSION_USER_TYPE_KEY);
    
    if (userData && userType) {
      const parsedUser = JSON.parse(userData);
      this.currentUser = userType === 'tecnico' 
        ? Object.assign(new Tecnico(), parsedUser)
        : Object.assign(new Paciente(), parsedUser);
    }
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return forkJoin([
      this.http.post<LoginResponse>(`${this.API_URL}/auth/tecnicos/login`, loginRequest),
      this.http.post<LoginResponse>(`${this.API_URL}/auth/pacientes/login`, loginRequest)
    ]).pipe(
      map(([tecnicoResponse, pacienteResponse]) => {
        if (tecnicoResponse.success) {
          return this.handleSuccessLogin(tecnicoResponse, 'tecnico');
        }
        if (pacienteResponse.success) {
          return this.handleSuccessLogin(pacienteResponse, 'paciente');
        }
        return {
          success: false,
          user: null,
          message: 'Credenciais inválidas'
        };
      }),
      catchError(error => of({
        success: false,
        user: null,
        message: 'Erro ao realizar login: ' + error.message
      }))
    );
  }

  registrarTecnico(tecnico: Tecnico): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/auth/tecnicos`, tecnico).pipe(
      map((response) => {
        if (response.success) {
          this.storeSessionData(response.user, 'tecnico', response.token!);
          return response;
        }
        return {
          success: false,
          user: null,
          message: 'Registro falhou'
        };
      }),
      catchError((error) => {
        return of({
          success: false,
          user: null,
          message: 'Erro ao registrar técnico: ' + error.message
        });
      })
    );
  }

  registrarPaciente(paciente: Paciente): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/auth/pacientes`, paciente).pipe(
      map((response) => {
        if (response.success) {
          this.storeSessionData(response.user, 'paciente', response.token!);
          return response;
        }
        return {
          success: false,
          user: null,
          message: 'Registro falhou'
        };
      }),
      catchError((error) => {
        return of({
          success: false,
          user: null,
          message: 'Erro ao registrar paciente: ' + error.message
        });
      })
    );
  }

  private handleSuccessLogin(response: LoginResponse, type: 'tecnico' | 'paciente'): LoginResponse {
    this.storeSessionData(response.user, type, response.token!);
    return response;
  }

  private storeSessionData(user: Tecnico | Paciente | null, type: string, token: string): void {
    if (user) {
      sessionStorage.setItem(this.SESSION_TOKEN_KEY, token);
      sessionStorage.setItem(this.SESSION_USER_TYPE_KEY, type);
      sessionStorage.setItem(this.SESSION_USER_DATA_KEY, JSON.stringify(user));
      this.currentUser = type === 'tecnico' ? new Tecnico() : new Paciente();
      Object.assign(this.currentUser, user);
    }
  }

  logout(): Observable<void> {
    return of(void 0).pipe(
      delay(1),
      tap(() => {
        this.currentUser = null;
        sessionStorage.removeItem(this.SESSION_TOKEN_KEY);
        sessionStorage.removeItem(this.SESSION_USER_TYPE_KEY);
        sessionStorage.removeItem(this.SESSION_USER_DATA_KEY);
      })
    );
  }

  getCurrentUser(): Tecnico | Paciente | null {
    return this.currentUser;
  }

  updateCurrentUser(user: any): void {
    sessionStorage.setItem('userData', JSON.stringify(user));
  }

  isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  getUserType(): 'tecnico' | 'paciente' | null {
    const userType = sessionStorage.getItem(this.SESSION_USER_TYPE_KEY);
    return userType as 'tecnico' | 'paciente' | null;
  }

  buscarCep(cep: string): Observable<Endereco | null> {
    const sanitizedCep = cep.replace(/\D/g, '');
    if (sanitizedCep.length !== 8) {
      return of(null);
    }
  
    return this.http.get<any>(`https://viacep.com.br/ws/${sanitizedCep}/json/`).pipe(
      timeout(5000),
      map(data => {
        if (data.erro) {
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