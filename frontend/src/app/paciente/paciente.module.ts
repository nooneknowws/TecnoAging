import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoricoTestesComponent } from './historico-testes/historico-testes.component';
import { VisualizarPerfilComponent } from './visualizar-perfil/visualizar-perfil.component';
import { PacienteDashboardComponent } from './paciente-dashboard/paciente-dashboard.component';



@NgModule({
  declarations: [
    HistoricoTestesComponent,
    VisualizarPerfilComponent,
    PacienteDashboardComponent
  ],
  imports: [
    CommonModule
  ]
})
export class PacienteModule { }
