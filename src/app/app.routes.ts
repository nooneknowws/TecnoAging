import { Routes } from '@angular/router';
import { CadastroTecnicoComponent } from './auth/cadastro-tecnico/cadastro-tecnico.component';
import { LoginComponent } from './auth/login/login.component';
import { EditarPerfilComponent } from './Cliente/perfil/editar-perfil/editar-perfil.component';

export const routes: Routes = [
    { path: 'cadastro-tecnico', component: CadastroTecnicoComponent },
    { path: 'login', component: LoginComponent },
    { path: 'cliente/editar-perfil', component: EditarPerfilComponent}
]
