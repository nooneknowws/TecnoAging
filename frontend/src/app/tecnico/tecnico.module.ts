import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CompararResultadosComponent } from './comparar-resultados/comparar-resultados.component';
import { EditarPerfilComponent } from './editar-perfil/editar-perfil.component';
import { EditarPerfilPacienteComponent } from './paciente/editar-perfil/editar-perfil.component';
import { HistoricoPacienteComponent } from './paciente/historico-paciente/historico-paciente.component';
import { VerPerfilComponent } from './paciente/ver-perfil/ver-perfil.component';
import { RouterModule } from '@angular/router';
import { TecnicoDashboardComponent } from './tecnico-dashboard/tecnico-dashboard.component';



@NgModule({
  declarations: [
    CompararResultadosComponent,
    EditarPerfilComponent,
    EditarPerfilPacienteComponent,
    HistoricoPacienteComponent,
    VerPerfilComponent,
    TecnicoDashboardComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ]
})
export class TecnicoModule { }
