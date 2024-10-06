import { Routes } from '@angular/router';
import { CadastroTecnicoComponent } from './auth/cadastro-tecnico/cadastro-tecnico.component';
import { LoginComponent } from './auth/login/login.component';

export const routes: Routes = [
    { path: 'cadastro-tecnico', component: CadastroTecnicoComponent },
    { path: 'login', component: LoginComponent }
];
