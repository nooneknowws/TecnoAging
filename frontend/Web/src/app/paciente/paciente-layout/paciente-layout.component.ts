import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from '../../_shared/services/auth.service';

@Component({
  selector: 'app-paciente-layout',
  templateUrl: './paciente-layout.component.html',
  styleUrls: ['./paciente-layout.component.css'],
})
export class PacienteLayoutComponent {
  linkVoltarVisivel: boolean = false;
  isNavbarOpen: boolean = true;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe(() => {
        this.linkVoltarVisivel = !this.isRotaInicial();
      });
  }

  toggleNavbar(): void {
    this.isNavbarOpen = !this.isNavbarOpen;
  }

  voltar() {
    history.back();
  }

  isRotaInicial(): boolean {
    return this.router.url === '/paciente' || this.router.url === '/paciente/';
  }

  logout(): void {
    const token = localStorage.getItem('token');
    this.authService.logout(token);
    this.router.navigate(['/login']);
  }
}
