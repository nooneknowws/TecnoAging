import { NgModule } from '@angular/core';
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

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CadastroComponent,
    FormularioComponent
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
    ReactiveFormsModule
  ],
  providers: [
    AuthService,
    provideNgxMask()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }