import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompararResultadosComponent } from './comparar-resultados/comparar-resultados.component';
import { EditarPerfilComponent } from './editar-perfil/editar-perfil.component';
import { EditarPerfilPacienteComponent } from './paciente/editar-perfil/editar-perfil.component';
import { HistoricoPacienteComponent } from './paciente/historico-paciente/historico-paciente.component';
import { VerPerfilComponent } from './paciente/ver-perfil/ver-perfil.component';
import { RouterModule } from '@angular/router';
import { TecnicoDashboardComponent } from './tecnico-dashboard/tecnico-dashboard.component';
import { FormularioService } from '../_shared/services/formulario.service';
import { TecnicoLayoutComponent } from './tecnico-layout/tecnico-layout.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    CompararResultadosComponent,
    EditarPerfilComponent,
    EditarPerfilPacienteComponent,
    HistoricoPacienteComponent,
    VerPerfilComponent,
    TecnicoDashboardComponent,
    TecnicoLayoutComponent
  ],  
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    FormularioService
  ]
})
export class TecnicoModule { }
