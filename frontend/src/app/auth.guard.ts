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
    const userType = this.authService.getUserType();

    if (state.url === '/login') {
      if (currentUser && userType) {
        this.router.navigate([userType]);
        return false;
      }
      return true;
    }

    if (!currentUser || !userType) {
      this.router.navigate(['login']);
      return false;
    }

    if (expectedRole && expectedRole !== userType) {
      this.router.navigate([userType]); 
      return false;
    }

    switch (userType) {
      case 'paciente':
        if (!(currentUser instanceof Paciente)) {
          this.router.navigate(['login']);
          return false;
        }
        break;
      case 'tecnico':
        if (!(currentUser instanceof Tecnico)) {
          this.router.navigate(['login']);
          return false;
        }
        break;
      default:
        this.router.navigate(['login']);
        return false;
    }

    return true;
  }
}