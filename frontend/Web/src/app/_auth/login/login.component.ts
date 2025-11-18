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
    isPasswordVisible: boolean = false;

    constructor(
        private authService: AuthService,
        private router: Router,
        private route: ActivatedRoute
    ) {}

    ngOnInit(): void {
        // Verificar se foi redirecionado por expiração de sessão ou conta inativa
        this.route.queryParams.subscribe(params => {
            if (params['expired'] === 'true') {
                this.sessionExpiredMessage = 'Sua sessão expirou. Por favor, faça login novamente.';

                // Limpar mensagem após 5 segundos
                setTimeout(() => {
                    this.sessionExpiredMessage = null;
                }, 5000);
            }

            if (params['inativo'] === 'true') {
                this.errorMessage = 'Acesso negado: Sua conta de técnico está inativa. Entre em contato com o administrador do sistema.';

                // Limpar mensagem após 10 segundos
                setTimeout(() => {
                    this.errorMessage = null;
                }, 10000);
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
                // Verificar se é erro de técnico inativo
                if (error.message === 'TECNICO_INATIVO') {
                    this.errorMessage = 'Acesso negado: Sua conta de técnico está inativa. Entre em contato com o administrador do sistema.';
                } else {
                    this.errorMessage = error.error?.message || 'Erro ao conectar ao servidor';
                }
                console.error('Login error:', error);
            }
        });
    }

    togglePasswordVisibility(): void {
        this.isPasswordVisible = !this.isPasswordVisible;
    }
}