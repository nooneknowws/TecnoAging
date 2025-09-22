import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../_shared/services/auth.service';

@Component({
  selector: 'app-solicitar-codigo',
  templateUrl: './solicitar-codigo.component.html',
  styleUrls: ['./solicitar-codigo.component.css']
})
export class SolicitarCodigoComponent {
  cpf: string = '';
  errorMessage: string | null = null;
  successMessage: string | null = null;
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  onSubmit() {
    this.errorMessage = null;
    this.successMessage = null;

    if (!this.cpf) {
      this.errorMessage = 'Por favor, informe seu CPF';
      return;
    }

    if (!this.isValidCPF(this.cpf)) {
      this.errorMessage = 'Por favor, informe um CPF válido';
      return;
    }

    this.isLoading = true;

    this.authService.enviarCodigo(this.cpf).subscribe({
      next: (response) => {
        this.isLoading = false;
        console.log('Resposta do servidor:', response);
        
        let telefoneInfo = '';
        if (response.telefone) {
          telefoneInfo = ` para o telefone ${response.telefone}`;
        }
        
        this.successMessage = `Código de verificação enviado via SMS${telefoneInfo} vinculado ao CPF ${this.formatCPF(this.cpf)}`;
        
        setTimeout(() => {
          this.router.navigate(['/recuperar-senha/codigo'], { 
            queryParams: { cpf: this.cpf } 
          });
        }, 3000);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Erro ao enviar código:', error);
        
        if (error.status === 404) {
          this.errorMessage = 'CPF não encontrado no sistema';
        } else if (error.error && error.error.error) {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = 'Erro interno do servidor. Tente novamente.';
        }
      }
    });
  }

  private isValidCPF(cpf: string): boolean {
    const cleanCPF = cpf.replace(/\D/g, '');
    return cleanCPF.length === 11;
  }

  private formatCPF(cpf: string): string {
    const cleanCPF = cpf.replace(/\D/g, '');
    return cleanCPF.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  voltarLogin() {
    this.router.navigate(['/login']);
  }
}