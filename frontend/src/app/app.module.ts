import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TecnicoModule } from './tecnico/tecnico.module';
import { PacienteModule } from './paciente/paciente.module';
import { LoginComponent } from './_auth/login/login.component';
import { FormFactfComponent } from './formularios/form-factf/form-factf.component';
import { FormIvcf20Component } from './formularios/form-ivcf20/form-ivcf20.component';
import { FormMinimentalComponent } from './formularios/form-minimental/form-minimental.component';
import { FormPfsComponent } from './formularios/form-pfs/form-pfs.component';
import { FormSedentarismoComponent } from './formularios/form-sedentarismo/form-sedentarismo.component';
import { NavbarComponent } from './navbar/navbar.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    FormFactfComponent,
    FormIvcf20Component,
    FormMinimentalComponent,
    FormPfsComponent,
    FormSedentarismoComponent,
    NavbarComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    TecnicoModule,
    PacienteModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }