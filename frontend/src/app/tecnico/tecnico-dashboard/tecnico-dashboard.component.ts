import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Formulario } from '../../_shared/models/formulario/formulario';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { AuthService } from '../../_shared/services/auth.service';
import { FormularioService } from '../../_shared/services/formulario.service';
import { PacienteService } from '../../_shared/services/paciente.service';

@Component({
  selector: 'app-tecnico-dashboard',
  templateUrl: './tecnico-dashboard.component.html',
  styleUrls: ['./tecnico-dashboard.component.css']
})
export class TecnicoDashboardComponent implements OnInit {
  currentTecnico: Tecnico | null = null;
  pacientes: Paciente[] = [];
  formularios: Formulario[] = [];
  selectedPaciente: Paciente | null = null;
  activeTab: 'pacientes' | 'formularios' | 'resultados' = 'pacientes';
  
  constructor(
    private formularioService: FormularioService,
    private pacienteService: PacienteService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (user instanceof Tecnico) {
      this.currentTecnico = user;
    } else {
      this.router.navigate(['/login']);
    }
    
    this.loadPacientes();
    this.loadFormularios();
  }

  loadPacientes(): void {
    this.pacienteService.getPacientes().subscribe(pacientes => {
      this.pacientes = pacientes
    });
  }

  loadFormularios(): void {
    this.formularioService.getFormularios().subscribe(formularios => {
      this.formularios = formularios
    });
  }

  selectPaciente(paciente: Paciente): void {
    this.selectedPaciente = paciente;
  }

  switchTab(tab: 'pacientes' | 'formularios' | 'resultados'): void {
    this.activeTab = tab;
  }

  navigateToFormulario(tipo: string): void {
    if (!this.selectedPaciente) {
      // Show error notification
      return;
    }
    this.router.navigate(['/formularios/' + tipo], {
      queryParams: { pacienteId: this.selectedPaciente.cpf }
    });
  }

  viewPatientHistory(paciente: Paciente): void {
    this.router.navigate(['/historico'], {
      queryParams: { pacienteId: paciente.cpf }
    });
  }

  compareResults(): void {
    if (!this.selectedPaciente) {
      // Show error notification
      return;
    }
    this.router.navigate(['/comparar-resultados'], {
      queryParams: { pacienteId: this.selectedPaciente.cpf }
    });
  }

  logout() {
    this.authService.logout();
  }
}