import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Formulario } from '../../_shared/models/formulario/formulario';
import { FORMULARIOS_CONFIG } from '../../_shared/models/formulario/formularios.config';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { TipoFormulario } from '../../_shared/models/tipos.formulario.enum';
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
  tiposFormulario = Object.values(TipoFormulario);
  formularios = Object.values(FORMULARIOS_CONFIG);
  selectedPaciente: Paciente | null = null;
  activeTab: 'pacientes' | 'formularios' | 'resultados' = 'pacientes';

  readonly formulariosDisponiveis = [
    { tipo: TipoFormulario.SEDENTARISMO, titulo: 'Nível de Atividade Física' },
    { tipo: TipoFormulario.IVCF20, titulo: 'Vulnerabilidade Clínico Funcional' },
    { tipo: TipoFormulario.PFS, titulo: 'Fatigabilidade de Pittsburgh' },
    { tipo: TipoFormulario.MINIMENTAL, titulo: 'Mini Exame do Estado Mental' },
    { tipo: TipoFormulario.FACTF, titulo: 'FACT-F' }
  ];
 
  constructor(
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
  }

  loadPacientes(): void {
    this.pacienteService.getPacientes().subscribe(pacientes => {
      this.pacientes = pacientes;
    });
  }

  selectPaciente(paciente: Paciente): void {
    this.selectedPaciente = paciente;
  }

  switchTab(tab: 'pacientes' | 'formularios' | 'resultados'): void {
    this.activeTab = tab;
  }

  navigateToFormulario(tipo: TipoFormulario) {
    if (!this.selectedPaciente) {
      alert('Selecione um paciente primeiro');
      return;
    }
    this.router.navigate(['/formularios', tipo], {
      queryParams: { pacienteId: this.selectedPaciente.id }
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