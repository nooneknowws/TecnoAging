import { Component, OnInit, ViewChildren, QueryList, ElementRef, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { TipoFormulario } from '../../_shared/models/tipos.formulario.enum';
import { AuthService } from '../../_shared/services/auth.service';
import { PacienteService } from '../../_shared/services/paciente.service';
import { Observable } from 'rxjs';

type ViewMode = 'grid' | 'table';
type Section = 'pacientes' | 'formularios' | 'resultados';

interface DashboardSection {
  id: Section;
  label: string;
  requiresPaciente: boolean;
  order: number;
}

@Component({
  selector: 'app-tecnico-dashboard',
  templateUrl: './tecnico-dashboard.component.html',
  styleUrls: ['./tecnico-dashboard.component.css']
})
export class TecnicoDashboardComponent implements OnInit {
  @ViewChildren('sectionRef', { read: ElementRef }) 
  private sectionRefs!: QueryList<ElementRef<HTMLElement>>;
  private sectionElements: Map<Section, ElementRef> = new Map();
  
  currentTecnico: Tecnico | null = null;
  pacientes: Paciente[] = [];
  selectedPaciente: Paciente | null = null;
  private pacienteSubject = new BehaviorSubject<Paciente[]>([]);
  pacientes$ = this.pacienteSubject.asObservable();
  
  activeSection: Section = 'pacientes';
  viewMode: ViewMode = 'grid';
  historicoTestes$: Observable<any[]> | null = null;
  loading = false;

  readonly sections: DashboardSection[] = [
    { id: 'pacientes', label: 'Pacientes', requiresPaciente: false, order: 1 },
    { id: 'formularios', label: 'Formulários', requiresPaciente: true, order: 2 },
    { id: 'resultados', label: 'Resultados', requiresPaciente: true, order: 3 }
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
    this.initializeUser();
  }

  ngAfterViewInit() {
    this.sectionRefs.forEach(ref => {
      const sectionId = ref.nativeElement.id as Section;
      this.sectionElements.set(sectionId, ref);
    });
  }

  private initializeUser(): void {
    const user = this.authService.getCurrentUser();
    if (user instanceof Tecnico) {
      this.currentTecnico = user;
      this.loadPacientes();
    } else {
      this.router.navigate(['/login']);
    }
  }

  switchSection(sectionId: Section): void {
    const section = this.sections.find(s => s.id === sectionId);
    if (!section) return;

    if (section.requiresPaciente && !this.selectedPaciente) {
      return;
    }

    this.activeSection = sectionId;
    this.scrollToSection(sectionId);
  }

  private scrollToSection(sectionId: Section): void {
    const sectionRef = this.sectionElements.get(sectionId);
    if (sectionRef) {
      sectionRef.nativeElement.scrollIntoView({ 
        behavior: 'smooth',
        block: 'start' 
      });
    }
  }

  toggleView(mode: ViewMode): void {
    this.viewMode = mode;
  }

  private loadPacientes(): void {
    this.loading = true;
    this.pacienteService.getPacientes().subscribe({
      next: (pacientes) => {
        this.pacientes = pacientes;
        this.pacienteSubject.next(pacientes);
      },
      error: (error) => {
        console.error('Erro ao carregar pacientes:', error);
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  selectPaciente(paciente: Paciente): void {
    if (this.selectedPaciente?.id === paciente.id) return;
    
    this.selectedPaciente = paciente;
    this.loadHistoricoTestes(paciente.id!);
    this.switchSection('formularios');
  }

  clearSelectedPaciente(): void {
    this.selectedPaciente = null;
    this.historicoTestes$ = null;
    this.switchSection('pacientes');
  }

  private loadHistoricoTestes(pacienteId: number): void {
    this.historicoTestes$ = this.pacienteService.getHistoricoTestes(pacienteId);
  }

  navigateToFormulario(tipo: TipoFormulario, event?: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    if (!this.selectedPaciente) {
      return;
    }

    this.router.navigate(['/formularios', tipo], {
      queryParams: { 
        pacienteId: this.selectedPaciente.id,
        returnUrl: this.router.url
      }
    });
  }

  consultarPerfil(paciente: Paciente, event?: Event): void {
    if (event) {
      event.stopPropagation();
    }
    
    this.router.navigate(['/tecnico/paciente/ver-perfil'], {
      queryParams: { 
        id: paciente.id,
        returnUrl: this.router.url
      }
    });
  }

  editarPerfil(paciente: Paciente, event?: Event): void {
    if (event) {
      event.stopPropagation();
    }
    
    this.router.navigate(['/tecnico/paciente/editar-perfil'], {
      queryParams: { 
        id: paciente.id,
        returnUrl: this.router.url
      }
    });
  }

  compareResults(): void {
    if (!this.selectedPaciente) return;
    
    this.router.navigate(['/comparar-resultados'], {
      queryParams: { 
        pacienteId: this.selectedPaciente.id,
        returnUrl: this.router.url
      }
    });
  }

  getStatusClass(status: string): string {
    const statusMap: Record<string, string> = {
      'concluído': 'bg-success',
      'em andamento': 'bg-warning',
      'pendente': 'bg-secondary',
      'cancelado': 'bg-danger'
    };

    return statusMap[status.toLowerCase()] || 'bg-primary';
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Erro ao fazer logout:', error);
      }
    });
  }
}