import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoricoTestesComponent } from './historico-testes/historico-testes.component';
import { VisualizarPerfilComponent } from './visualizar-perfil/visualizar-perfil.component';
import { PacienteDashboardComponent } from './paciente-dashboard/paciente-dashboard.component';
import { PacienteLayoutComponent } from './paciente-layout/paciente-layout.component';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../_shared/shared.module';
import { EditarPerfilComponentPaciente } from './editar-perfil/editar-perfil.component';
import { FormsModule } from '@angular/forms';
import { NgxMaskDirective, NgxMaskPipe } from 'ngx-mask';



@NgModule({
  declarations: [
    HistoricoTestesComponent,
    VisualizarPerfilComponent,
    PacienteDashboardComponent,
    PacienteLayoutComponent,
    EditarPerfilComponentPaciente
  ],
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    SharedModule,
    NgxMaskDirective,
    NgxMaskPipe
  ]
})
export class PacienteModule { }
