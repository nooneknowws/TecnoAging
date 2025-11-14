import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { LoginRequest } from '../../_shared/models/_auth/login.request';
import { AuthService } from '../../_shared/services/auth.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    loginRequest: LoginRequest = {
        cpf: "",
        senha: '',
    };
    errorMessage: string | null = null;
    sessionExpiredMessage: string | null = null;

    constructor(
        private authService: AuthService,
        private router: Router,
        private route: ActivatedRoute
    ) {}

    ngOnInit(): void {
        // Verificar se foi redirecionado por expiração de sessão
        this.route.queryParams.subscribe(params => {
            if (params['expired'] === 'true') {
                this.sessionExpiredMessage = 'Sua sessão expirou. Por favor, faça login novamente.';

                // Limpar mensagem após 5 segundos
                setTimeout(() => {
                    this.sessionExpiredMessage = null;
                }, 5000);
            }
        });
    }

    onSubmit() {
        this.errorMessage = null;
        
        if (!this.loginRequest.cpf || !this.loginRequest.senha) {
            this.errorMessage = 'Por favor, preencha todos os campos';
            return;
        }

        this.authService.login(this.loginRequest).subscribe({
            next: (response) => {
                if (response.success) {
                    this.router.navigate(['/']);
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