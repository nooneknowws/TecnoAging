import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoricoTestesComponent } from './historico-testes/historico-testes.component';
import { VisualizarPerfilComponent } from './visualizar-perfil/visualizar-perfil.component';



@NgModule({
  declarations: [
    HistoricoTestesComponent,
    VisualizarPerfilComponent
  ],
  imports: [
    CommonModule
  ]
})
export class IdosoModule { }
