import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
    const userData = localStorage.getItem(this.SESSION_USER_DATA_KEY);
    const userType = localStorage.getItem(this.SESSION_USER_TYPE_KEY);
    
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
          ? `${this.API_URL}/pacientes/${loginResponse.ID}`
          : `${this.API_URL}/tecnicos/${loginResponse.ID}`;
  
        return this.http.get<any>(endpoint, {
          headers: { Authorization: `Bearer ${loginResponse.token}` }
        }).pipe(
          // Aumentar timeout para 30 segundos para usuários com foto
          timeout(30000),
          map(userData => {
            this.storeUserData(userData, loginResponse.Perfil!);
            return loginResponse;
          }),
          catchError(error => {
            console.error('Erro ao buscar dados do usuário:', error);
            
            // Se falhar ao buscar dados completos, ainda permite login
            // mas sem dados detalhados do usuário
            if (error.name === 'TimeoutError') {
              console.warn('Timeout ao buscar dados do usuário - continuando login sem dados completos');
            }
            
            return of(loginResponse);
          })
        );
      })
    );
  }

  registrarTecnico(tecnico: Tecnico): Observable<Tecnico> {
    tecnico.ativo = true;
    return this.http.post<Tecnico>(`${this.API_URL}/tecnicos`, tecnico);
  }

  registrarPaciente(paciente: Paciente): Observable<Paciente> {
    return this.http.post<Paciente>(`${this.API_URL}/pacientes`, paciente)
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
  
  logout(token: string | null): boolean {
    const headers = new HttpHeaders({ 'Authorization': `Bearer ${token}` })
    
    // Clear all local data immediately
    this.clearAllUserData();
    
    // Call backend logout (async)
    this.http.post(`${this.API_URL}/auth/logout`, null, {headers}).subscribe({
      next: () => {
        console.log('Backend logout successful');
      },
      error: (err) => {
        console.error('Backend logout failed:', err);
      }
    });
    
    return true;
  }

  private clearAllUserData(): void {
    // Clear localStorage
    localStorage.removeItem(this.SESSION_TOKEN_KEY);
    localStorage.removeItem(this.SESSION_USER_TYPE_KEY);
    localStorage.removeItem(this.SESSION_USER_DATA_KEY);
    localStorage.removeItem(this.SESSION_USER_ID_KEY);
    localStorage.removeItem('userData'); // Legacy key that might exist
    
    // Clear sessionStorage as well
    sessionStorage.clear();
    
    // Reset current user object
    this.currentUser = null;
    
    console.log('All user data cleared from frontend');
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
    return !!localStorage.getItem(this.SESSION_TOKEN_KEY);
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