import { Component, OnInit, ViewChildren, QueryList, ElementRef, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { TipoFormulario } from '../../_shared/models/tipos.formulario.enum';
import { AuthService } from '../../_shared/services/auth.service';
import { PacienteService } from '../../_shared/services/paciente.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-tecnico-dashboard',
  templateUrl: './tecnico-dashboard.component.html',
  styleUrls: ['./tecnico-dashboard.component.css']
})
export class TecnicoDashboardComponent implements OnInit, AfterViewInit {
  @ViewChildren('sectionRef') sectionRefs!: QueryList<ElementRef>;
  
  currentTecnico: Tecnico | null = null;
  pacientes: Paciente[] = [];
  selectedPaciente: Paciente | null = null;
  activeSection: string = 'pacientes';
  viewMode: 'grid' | 'table' = 'grid';
  historicoTestes$: Observable<any[]> | null = null;

  readonly sections = [
    { id: 'pacientes', label: 'Pacientes' },
    { id: 'formularios', label: 'Formulários' },
    { id: 'resultados', label: 'Resultados' }
  ];

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
    this.setupScrollObserver();
  }

  ngAfterViewInit() {
    this.setupScrollObserver();
  }

  private setupScrollObserver() {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            this.activeSection = entry.target.id;
          }
        });
      },
      {
        threshold: 0.5
      }
    );

    this.sectionRefs.forEach(ref => observer.observe(ref.nativeElement));
  }

  scrollToSection(sectionId: string) {
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  toggleView(mode: 'grid' | 'table'): void {
    this.viewMode = mode;
  }

  selectPaciente(paciente: Paciente): void {
    this.selectedPaciente = paciente;
    this.historicoTestes$ = this.pacienteService.getHistoricoTestes(paciente.id!);
    setTimeout(() => {
      this.scrollToSection('formularios');
    }, 100);
  }

  getStatusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'concluído':
        return 'bg-success';
      case 'em andamento':
        return 'bg-warning';
      case 'pendente':
        return 'bg-secondary';
      default:
        return 'bg-primary';
    }
  }

  clearSelectedPaciente(): void {
    this.selectedPaciente = null;
    this.historicoTestes$ = null;
    this.scrollToSection('pacientes');
  }

  loadPacientes(): void {
    this.pacienteService.getPacientes().subscribe(pacientes => {
      this.pacientes = pacientes;
    });
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

  consultarPerfil(paciente: Paciente) {
    this.router.navigate(['/tecnico/paciente/ver-perfil'], {
      queryParams: { id: paciente.id }
    });
  }

  editarPerfil(paciente: Paciente) {
    this.router.navigate(['/tecnico/paciente/editar-perfil'], {
      queryParams: { id: paciente.id }
    });
  }

  compareResults(): void {
    if (!this.selectedPaciente) return;
    
    this.router.navigate(['/comparar-resultados'], {
      queryParams: { pacienteId: this.selectedPaciente.cpf }
    });
  }

  logout() {
    this.authService.logout();
  }
}