import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../_shared/services/auth.service';

@Component({
  selector: 'app-redefinir-senha',
  templateUrl: './redefinir-senha.component.html',
  styleUrls: ['./redefinir-senha.component.css']
})
export class RedefinirSenhaComponent implements OnInit {
  cpf: string = '';
  codigoVerificacao: string = '';
  novaSenha: string = '';
  confirmarSenha: string = '';
  errorMessage: string | null = null;
  successMessage: string | null = null;
  isLoading: boolean = false;
  
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.cpf = params['cpf'] || '';
    });
  }

  onSubmit() {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.codigoVerificacao) {
      this.errorMessage = 'Por favor, informe o código de verificação';
      return;
    }

    if (!this.novaSenha) {
      this.errorMessage = 'Por favor, informe a nova senha';
      return;
    }

    if (this.novaSenha !== this.confirmarSenha) {
      this.errorMessage = 'As senhas não coincidem';
      return;
    }

    if (this.novaSenha.length < 6) {
      this.errorMessage = 'A senha deve ter pelo menos 6 caracteres';
      return;
    }

    this.isLoading = true;

    this.authService.resetPassword(this.cpf, this.codigoVerificacao, this.novaSenha, this.confirmarSenha).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Resposta do servidor:', response);
        this.successMessage = 'Senha alterada com sucesso! Redirecionando para o login...';
        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Erro ao resetar senha:', error);
        
        if (error.status === 400 && error.error && error.error.error) {
          this.errorMessage = error.error.error;
        } else if (error.status === 404) {
          this.errorMessage = 'CPF não encontrado no sistema';
        } else {
          this.errorMessage = 'Erro interno do servidor. Tente novamente.';
        }
      }
    });
  }

  voltarCodigo() {
    this.router.navigate(['/recuperar-senha'], { 
      queryParams: { cpf: this.cpf } 
    });
  }

  formatCPF(cpf: string): string {
    const cleanCPF = cpf.replace(/\D/g, '');
    return cleanCPF.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  voltarLogin() {
    this.router.navigate(['/login']);
  }
}