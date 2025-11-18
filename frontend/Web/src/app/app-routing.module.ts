import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './_auth/login/login.component';
import { CadastroComponent } from './_auth/cadastro/cadastro.component';
import { HistoricoTestesComponent } from './paciente/historico-testes/historico-testes.component';
import { VisualizarPerfilComponent } from './paciente/visualizar-perfil/visualizar-perfil.component';
import { EditarPerfilComponentPaciente } from './paciente/editar-perfil/editar-perfil.component';
import { EditarPerfilComponent } from './tecnico/editar-perfil/editar-perfil.component';
import { HistoricoPacienteComponent } from './tecnico/paciente/historico-paciente/historico-paciente.component';
import { VerPerfilComponent } from './tecnico/paciente/ver-perfil/ver-perfil.component';
import { EditarPerfilPacienteComponent } from './tecnico/paciente/editar-perfil/editar-perfil.component';
import { AuthGuard } from './auth.guard';
import { PacienteDashboardComponent } from './paciente/paciente-dashboard/paciente-dashboard.component';
import { TecnicoDashboardComponent } from './tecnico/tecnico-dashboard/tecnico-dashboard.component';
import { TecnicoLayoutComponent } from './tecnico/tecnico-layout/tecnico-layout.component';
import { FormularioComponent } from './formulario/formulario.component';
import { PacienteLayoutComponent } from './paciente/paciente-layout/paciente-layout.component';
import { ConsultarAvaliacaoComponent } from './tecnico/paciente/consultar-avaliacao/consultar-avaliacao.component';
import { EditarAvaliacaoComponent } from './tecnico/paciente/editar-avaliacao/editar-avaliacao.component';
import { HomeComponent } from './_auth/home.component';
import { CadastroTecnicoComponent } from './_auth/cadastro/cadastro-tecnico/cadastro-tecnico.component';
import { CadastroPacienteComponent } from './_auth/cadastro/cadastro-paciente/cadastro-paciente.component';
import { SolicitarCodigoComponent } from './_auth/recuperar-senha/solicitar-codigo.component';
import { RedefinirSenhaComponent } from './_auth/recuperar-senha/redefinir-senha.component';
import { FormularioCadastroComponent } from './_tecnico/formulario-cadastro/formulario-cadastro.component';
import { FormularioEdicaoComponent } from './_tecnico/formulario-edicao/formulario-edicao.component';
import { GerenciarTecnicosComponent } from './tecnico/gerenciar-tecnicos/gerenciar-tecnicos.component';
import { CadastrarTecnicoComponent } from './tecnico/cadastrar-tecnico/cadastrar-tecnico.component';
import { EditarTecnicoComponent } from './tecnico/editar-tecnico/editar-tecnico.component';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'login', component: LoginComponent },
  {
    path: 'cadastro',
    children: [
      { path: 'tecnico', component: CadastroTecnicoComponent },
      { path: 'paciente', component: CadastroPacienteComponent },
    ],
  },
  { path: 'recuperar-senha', component: SolicitarCodigoComponent },
  { path: 'recuperar-senha/codigo', component: RedefinirSenhaComponent },
  {
    path: 'paciente',
    component: PacienteLayoutComponent,
    canActivate: [AuthGuard],
    data: { tipo: 'paciente' },
    children: [
      { path: '', component: PacienteDashboardComponent },
      { path: 'editar-perfil', component: EditarPerfilComponentPaciente },
      { path: 'formularios', component: PacienteDashboardComponent },
      { path: 'historico-testes', component: HistoricoTestesComponent },
      { path: 'visualizar-perfil', component: VisualizarPerfilComponent },
      { path: 'editar-perfil', component: EditarPerfilPacienteComponent },
    ],
  },
  {
    path: 'tecnico',
    component: TecnicoLayoutComponent,
    canActivate: [AuthGuard],
    data: { tipo: 'tecnico' },
    children: [
      { path: '', component: TecnicoDashboardComponent },
      { path: 'editar-perfil', component: EditarPerfilComponent },
      { path: 'gerenciar-tecnicos', component: GerenciarTecnicosComponent },
      { path: 'cadastrar-tecnico', component: CadastrarTecnicoComponent },
      { path: 'editar-tecnico/:id', component: EditarTecnicoComponent },
      {
        path: 'paciente/editar-perfil',
        component: EditarPerfilPacienteComponent,
      },
      {
        path: 'paciente/historico-paciente',
        component: HistoricoPacienteComponent,
      },
      { path: 'paciente/ver-perfil', component: VerPerfilComponent },
      {
        path: 'avaliacoes/consultar/:pacienteId',
        component: ConsultarAvaliacaoComponent,
      },
      { path: 'avaliacoes/editar/:id', component: EditarAvaliacaoComponent },
      { path: 'formularios/criar', component: FormularioCadastroComponent },
      { path: 'formularios/editar/:id', component: FormularioEdicaoComponent },
    ],
  },
  {
    path: 'formularios',
    canActivate: [AuthGuard],
    children: [
      {
        path: ':id',
        component: FormularioComponent,
      },
    ],
  },
  { path: '**', redirectTo: '/' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
