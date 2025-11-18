import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Paciente } from './_shared/models/pessoa/paciente/paciente';
import { Tecnico } from './_shared/models/pessoa/tecnico/tecnico';
import { AuthService } from './_shared/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const expectedRole = route.data['tipo'];
    const currentUser = this.authService.getCurrentUser();
    const rawProfile = this.authService.getUserProfile();
    const userProfile = rawProfile?.toLowerCase();
    const token = localStorage.getItem('token');

    if (state.url === '/login') {
      if (currentUser && userProfile && token) {
        this.redirectToProfilePage(userProfile);
        return false;
      }
      return true;
    }

    // Verificar se usuário está logado e token existe
    if (!currentUser || !userProfile || !token) {
      console.warn('AuthGuard: Usuário não autenticado ou token ausente');
      this.clearAuthAndRedirect();
      return false;
    }

    // Verificar se token está expirado (decodificando JWT)
    if (this.isTokenExpired(token)) {
      console.warn('AuthGuard: Token expirado');
      this.clearAuthAndRedirect();
      return false;
    }

    const normalizedExpected = expectedRole?.toLowerCase();
    const normalizedUser = userProfile.toLowerCase();

    if (normalizedExpected && normalizedExpected !== normalizedUser) {
      this.redirectToProfilePage(normalizedUser);
      return false;
    }

    return this.validateUserType(normalizedUser, currentUser);
  }

  private redirectToProfilePage(profile: string): void {
    const route = profile === 'tecnico' ? '/tecnico' : '/paciente';
    this.router.navigate([route]);
  }

  private validateUserType(profile: string, user: any): boolean {
    const token = localStorage.getItem('token')
    const isValidType = (
      (profile === 'paciente' && user instanceof Paciente) ||
      (profile === 'tecnico' && user instanceof Tecnico)
    );

    if (!isValidType) {
      console.error('User type mismatch, forcing logout');
      this.authService.logout(token);
      this.router.navigate(['/login']);
      return false;
    }

    // Verificar se é técnico e se está inativo
    if (profile === 'tecnico' && user instanceof Tecnico && user.ativo === false) {
      console.warn('AuthGuard: Técnico inativo tentando acessar o sistema');
      this.authService.logout(token);
      this.router.navigate(['/login'], {
        queryParams: { inativo: 'true' }
      });
      return false;
    }

    return true;
  }

  /**
   * Verifica se o token JWT está expirado
   */
  private isTokenExpired(token: string): boolean {
    try {
      const payload = this.decodeToken(token);
      if (!payload || !payload.exp) {
        return true;
      }

      const expirationDate = new Date(payload.exp * 1000);
      const now = new Date();

      return now >= expirationDate;
    } catch (error) {
      console.error('Erro ao verificar expiração do token:', error);
      return true;
    }
  }

  /**
   * Decodifica o payload do JWT
   */
  private decodeToken(token: string): any {
    try {
      const parts = token.split('.');
      if (parts.length !== 3) {
        return null;
      }

      const payload = parts[1];
      const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(decoded);
    } catch (error) {
      console.error('Erro ao decodificar token:', error);
      return null;
    }
  }

  /**
   * Limpa dados de autenticação e redireciona para login
   */
  private clearAuthAndRedirect(): void {
    const token = localStorage.getItem('token');
    this.authService.logout(token);
    this.router.navigate(['/login'], {
      queryParams: { expired: 'true' }
    });
  }
}