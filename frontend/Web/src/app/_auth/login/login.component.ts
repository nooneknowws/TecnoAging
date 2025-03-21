import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginRequest } from '../../_shared/models/_auth/login.request';
import { AuthService } from '../../_shared/services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    loginRequest: LoginRequest = {
        cpf: "",
        senha: '',
        tipo: ''
    };
    errorMessage: string | null = null;

    constructor(
        private authService: AuthService,
        private router: Router
    ) {}

    onSubmit() {
        this.errorMessage = null;
        
        if (!this.loginRequest.cpf || !this.loginRequest.senha) {
            this.errorMessage = 'Por favor, preencha todos os campos';
            return;
        }

        this.authService.login(this.loginRequest).subscribe({
            next: (response) => {
                if (response.success) {
                    const route = response.tipo === 'tecnico' ? '/tecnico' : '/paciente';
                    this.router.navigate([route]);
                } else {
                    this.errorMessage = response.message || 'Erro ao realizar login';
                }
            },
            error: (error) => {
                this.errorMessage = 'Erro ao conectar ao servidor';
                console.error('Login error:', error);
            }
        });
    }
}