import { Routes } from '@angular/router';
import { CadastroTecnicoComponent } from './auth/cadastro-tecnico/cadastro-tecnico.component';
import { LoginComponent } from './auth/login/login.component';
import { EditarPerfilComponent } from './Cliente/perfil/editar-perfil/editar-perfil.component';
import { SedentarismoComponent } from './formularios/sedentarismo/sedentarismo.component';
import { Ivcf20Component } from './formularios/ivcf-20/ivcf-20.component';
import { MinimentalComponent } from './formularios/minimental/minimental.component';

export const routes: Routes = [
    { path: 'cadastro-tecnico', component: CadastroTecnicoComponent },
    { path: 'login', component: LoginComponent },
    { path: 'cliente/editar-perfil', component: EditarPerfilComponent},
    { path: 'formularios/sedentarismo', component: SedentarismoComponent},
    { path: 'formularios/ivcf-20', component: Ivcf20Component},
    { path: 'formularios/minimental', component: MinimentalComponent}
]
