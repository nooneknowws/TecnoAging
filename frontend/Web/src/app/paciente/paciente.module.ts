import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoricoTestesComponent } from './historico-testes/historico-testes.component';
import { VisualizarPerfilComponent } from './visualizar-perfil/visualizar-perfil.component';
import { PacienteDashboardComponent } from './paciente-dashboard/paciente-dashboard.component';
import { PacienteLayoutComponent } from './paciente-layout/paciente-layout.component';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    HistoricoTestesComponent,
    VisualizarPerfilComponent,
    PacienteDashboardComponent,
    PacienteLayoutComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ]
})
export class PacienteModule { }
