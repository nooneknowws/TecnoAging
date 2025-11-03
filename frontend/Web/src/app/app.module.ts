import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TecnicoModule } from './tecnico/tecnico.module';
import { PacienteModule } from './paciente/paciente.module';
import { LoginComponent } from './_auth/login/login.component';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from './_shared/services/auth.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { CadastroComponent } from './_auth/cadastro/cadastro.component';
import { FormularioComponent } from './formulario/formulario.component';
import { AuthInterceptor } from './_auth/auth.interceptor';
import { CadastroTecnicoComponent } from './_auth/cadastro/cadastro-tecnico/cadastro-tecnico.component';
import { CadastroPacienteComponent } from './_auth/cadastro/cadastro-paciente/cadastro-paciente.component';
import { SolicitarCodigoComponent } from './_auth/recuperar-senha/solicitar-codigo.component';
import { RedefinirSenhaComponent } from './_auth/recuperar-senha/redefinir-senha.component';
import { FormularioCadastroComponent } from './_tecnico/formulario-cadastro/formulario-cadastro.component';
import { FormularioEdicaoComponent } from './_tecnico/formulario-edicao/formulario-edicao.component';
import { SharedModule } from './_shared/shared.module';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CadastroComponent,
    CadastroTecnicoComponent,
    CadastroPacienteComponent,
    SolicitarCodigoComponent,
    RedefinirSenhaComponent,
    FormularioComponent,
    FormularioCadastroComponent,
    FormularioEdicaoComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    TecnicoModule,
    PacienteModule,
    HttpClientModule,
    FormsModule,
    NgxMaskDirective, 
    NgxMaskPipe,
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    SharedModule
  ],
  providers: [
    AuthService,
    provideNgxMask(),
    { 
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }