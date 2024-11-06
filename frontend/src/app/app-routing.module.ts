import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './_auth/login/login.component';
import { CadastroComponent } from './_auth/cadastro/cadastro.component';
import { HistoricoTestesComponent } from './idoso/historico-testes/historico-testes.component';
import { VisualizarPerfilComponent } from './idoso/visualizar-perfil/visualizar-perfil.component';
import { CompararResultadosComponent } from './tecnico/comparar-resultados/comparar-resultados.component';
import { EditarPerfilComponent } from './tecnico/editar-perfil/editar-perfil.component';
import { HistoricoPacienteComponent } from './tecnico/paciente/historico-paciente/historico-paciente.component';
import { VerPerfilComponent } from './tecnico/paciente/ver-perfil/ver-perfil.component';
import { FormFactfComponent } from './formularios/form-factf/form-factf.component';
import { FormIvcf20Component } from './formularios/form-ivcf20/form-ivcf20.component';
import { FormMinimentalComponent } from './formularios/form-minimental/form-minimental.component';
import { FormPfsComponent } from './formularios/form-pfs/form-pfs.component';
import { FormSedentarismoComponent } from './formularios/form-sedentarismo/form-sedentarismo.component';
import { EditarPerfilPacienteComponent } from './tecnico/paciente/editar-perfil/editar-perfil.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'cadastro', component: CadastroComponent },
  { path: 'formularios/fact-f', component: FormFactfComponent },
  { path: 'formularios/ivcf-20', component: FormIvcf20Component },
  { path: 'formularios/mini-mental', component: FormMinimentalComponent },
  { path: 'formularios/pfs', component: FormPfsComponent },
  { path: 'formularios/sedentarismo', component: FormSedentarismoComponent },
  { path: 'idoso/historico-testes', component: HistoricoTestesComponent },
  { path: 'idoso/visualizar-perfil', component: VisualizarPerfilComponent },
  { path: 'tecnico/comparar-resultados', component: CompararResultadosComponent },
  { path: 'tecnico/editar-perfil', component: EditarPerfilComponent },
  { path: 'tecnico/paciente/editar-perfil', component: EditarPerfilPacienteComponent },
  { path: 'tecnico/paciente/historico-paciente', component: HistoricoPacienteComponent },
  { path: 'tecnico/paciente/ver-perfil', component: VerPerfilComponent },
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }