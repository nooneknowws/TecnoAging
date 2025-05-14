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

    if (state.url === '/login') {
      if (currentUser && userProfile) {
        this.redirectToProfilePage(userProfile);
        return false;
      }
      return true;
    }

    if (!currentUser || !userProfile) {
      this.router.navigate(['/login']);
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
    const isValidType = (
      (profile === 'paciente' && user instanceof Paciente) ||
      (profile === 'tecnico' && user instanceof Tecnico)
    );

    if (!isValidType) {
      console.error('User type mismatch, forcing logout');
      this.authService.logout();
      this.router.navigate(['/login']);
      return false;
    }

    return true;
  }
}