// home.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../_shared/services/auth.service';

@Component({
  selector: 'app-home',
  template: '<div>Redirecionando...</div>'
})
export class HomeComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const perfil = this.authService.getUserProfile();
    
    if (perfil === 'paciente') {
      this.router.navigate(['/paciente']);
    } else if (perfil === 'tecnico') {
      this.router.navigate(['/tecnico']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}