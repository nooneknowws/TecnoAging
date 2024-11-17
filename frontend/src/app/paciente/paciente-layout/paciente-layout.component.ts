import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';

@Component({
  selector: 'app-paciente-layout',
  templateUrl: './paciente-layout.component.html',
  styleUrl: './paciente-layout.component.css'
})
export class PacienteLayoutComponent {
  linkVoltarVisivel: boolean = false;
  
  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.linkVoltarVisivel = !this.isRotaInicial();
    });
  }

  voltar() {
    history.back();
  }

  isRotaInicial(): boolean {
    return this.router.url === '/paciente';
  }
}
