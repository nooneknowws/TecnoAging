import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from '../../_shared/services/auth.service';

@Component({
  selector: 'app-tecnico-layout',
  templateUrl: './tecnico-layout.component.html',
  styleUrls: ['./tecnico-layout.component.css']
})
export class TecnicoLayoutComponent {
  linkVoltarVisivel: boolean = false;
  isNavbarOpen: boolean = true;
  tecnicoId: string | null = null;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.linkVoltarVisivel = !this.isRotaInicial();
    });

    const user = this.authService.getCurrentUser();
    if (user && (user as any).id) {
      this.tecnicoId = String((user as any).id);
    } else {
      this.tecnicoId = localStorage.getItem('userID');
    }
  }

  toggleNavbar(): void {
    this.isNavbarOpen = !this.isNavbarOpen;
  }

  voltar() {
    history.back();
  }

  isRotaInicial(): boolean {
    return this.router.url === '/tecnico';
  }

  logout(): void {
    const token = localStorage.getItem('token')
    this.authService.logout(token);
    this.router.navigate(['/login']);
  }
}