import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, Observable, of } from 'rxjs';
import { map, catchError, switchMap } from 'rxjs/operators';
import { Tecnico } from '../models/pessoa/tecnico/tecnico';
import { Paciente } from '../models/pessoa/paciente/paciente';
import { LoginRequest } from '../models/_auth/login.request';
import { LoginResponse } from '../models/_auth/login.response';
import { AuthUser } from '../models/_auth/auth.user';
import { AppComponent } from '../../app.component';

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
      this.checkCredentials(loginRequest.cpf, loginRequest.senha, 'tecnico'),
      this.checkCredentials(loginRequest.cpf, loginRequest.senha, 'paciente')
    ]).pipe(
      switchMap(([tecnicoAuth, pacienteAuth]) => {
        if (tecnicoAuth) {
          return this.fetchUserData(loginRequest.cpf, 'tecnico');
        }
        if (pacienteAuth) {
          return this.fetchUserData(loginRequest.cpf, 'paciente');
        }
        return of({
          success: false,
          user: null,
          message: 'Credenciais inválidas'
        });
      }),
      catchError(error => of({
        success: false,
        user: null,
        message: 'Erro ao realizar login: ' + error.message
      }))
    );
  }

  private checkCredentials(cpf: string, senha: string, tipo: 'tecnico' | 'paciente'): Observable<AuthUser | null> {
    return this.http.get<AuthUser[]>(`${this.API_URL}/auth`).pipe(
      map(authUsers => authUsers.find(u =>
        u.cpf === cpf &&
        u.senha === senha &&
        u.tipo === tipo
      ) || null)
    );
  }

  private fetchUserData(cpf: string, tipo: 'tecnico' | 'paciente'): Observable<LoginResponse> {
    const endpoint = tipo === 'tecnico' ? 'tecnicos' : 'pacientes';
    
    return this.http.get<(Tecnico | Paciente)[]>(`${this.API_URL}/${endpoint}`).pipe(
      map(users => {
        const user = users.find(u => u.cpf === cpf);
        
        if (!user) {
          return {
            success: false,
            user: null,
            message: 'Usuário não encontrado'
          };
        }

        this.currentUser = tipo === 'tecnico'
          ? Object.assign(new Tecnico(), user)
          : Object.assign(new Paciente(), user);

        const token = btoa(JSON.stringify({ cpf, tipo }));
        
        sessionStorage.setItem(this.SESSION_TOKEN_KEY, token);
        sessionStorage.setItem(this.SESSION_USER_TYPE_KEY, tipo);
        sessionStorage.setItem(this.SESSION_USER_DATA_KEY, JSON.stringify(user));

        return {
          success: true,
          user: this.currentUser,
          tipo: tipo,
          token
        };
      })
    );
  }

  logout(): void {
    this.currentUser = null;
    sessionStorage.removeItem(this.SESSION_TOKEN_KEY);
    sessionStorage.removeItem(this.SESSION_USER_TYPE_KEY);
    sessionStorage.removeItem(this.SESSION_USER_DATA_KEY);
  }

  getCurrentUser(): Tecnico | Paciente | null {
    return this.currentUser;
  }

  isLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  getUserType(): 'tecnico' | 'paciente' | null {
    const userType = sessionStorage.getItem(this.SESSION_USER_TYPE_KEY);
    return userType as 'tecnico' | 'paciente' | null;
  }
}