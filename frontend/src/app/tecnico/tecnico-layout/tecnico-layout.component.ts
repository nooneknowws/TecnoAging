import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';

@Component({
  selector: 'app-tecnico-layout',
  templateUrl: './tecnico-layout.component.html',
  styleUrl: './tecnico-layout.component.css'
})
export class TecnicoLayoutComponent {
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
    return this.router.url === '/tecnico';
  }
}
