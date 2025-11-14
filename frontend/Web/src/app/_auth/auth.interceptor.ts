import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../_shared/services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.warn('HTTP Interceptor: Recebeu erro 401 - Token inválido ou expirado');
          this.handleUnauthorized();
        }
        return throwError(() => error);
      })
    );
  }

  private handleUnauthorized(): void {
    const token = localStorage.getItem('token');

    // Fazer logout e limpar dados
    this.authService.logout(token);

    // Redirecionar para login com mensagem
    this.router.navigate(['/login'], {
      queryParams: { expired: 'true' }
    });
  }
}