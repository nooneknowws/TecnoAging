import { Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { Formulario } from '../../_shared/models/formulario/formulario';
import { Avaliacao } from '../../_shared/models/avaliacao/avaliacao';
import { AuthService } from '../../_shared/services/auth.service';
import { PacienteService } from '../../_shared/services/paciente.service';
import { ImageService } from '../../_shared/services/image.service';
import { FormularioService } from '../../_shared/services/formulario.service';
import { AvaliacaoService } from '../../_shared/services/avaliacao.service';

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
  avaliacoes: Avaliacao[] = [];
  loadingAvaliacoes = false;
  loading = false;
  loadingFormularios = false;

  readonly sections: DashboardSection[] = [
    { id: 'pacientes', label: 'Pacientes', requiresPaciente: false, order: 1 },
    { id: 'formularios', label: 'Formulários', requiresPaciente: true, order: 2 },
    { id: 'resultados', label: 'Resultados', requiresPaciente: true, order: 3 }
  ];

  formulariosDisponiveis: Formulario[] = [];

  constructor(
    private pacienteService: PacienteService,
    private authService: AuthService,
    private router: Router,
    private imageService: ImageService,
    private formularioService: FormularioService,
    private avaliacaoService: AvaliacaoService
  ) {}

  ngOnInit(): void {
    this.initializeUser();
    this.loadFormularios();
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
        this.loadPacientesPhotos();
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

  private loadPacientesPhotos(): void {
    this.pacientes.forEach(paciente => {
      if (paciente.id) {
        this.imageService.getPacientePhoto(paciente.id).subscribe({
          next: (response) => {
            if (response && response.image) {
              paciente.fotoUrl = response.image;
            }
          },
          error: (error) => {
            console.log(`Foto não encontrada para paciente ${paciente.id}`);
          }
        });
      }
    });
  }

  selectPaciente(paciente: Paciente): void {
    if (this.selectedPaciente?.id === paciente.id) return;

    this.selectedPaciente = paciente;
    this.loadAvaliacoes(paciente.id!);
    this.switchSection('formularios');
  }

  clearSelectedPaciente(): void {
    this.selectedPaciente = null;
    this.avaliacoes = [];
    this.switchSection('pacientes');
  }

  private loadFormularios(): void {
    this.loadingFormularios = true;
    this.formularioService.listarFormularios().subscribe({
      next: (formularios) => {
        this.formulariosDisponiveis = formularios;
        this.loadingFormularios = false;
      },
      error: (error) => {
        console.error('Erro ao carregar formulários:', error);
        this.loadingFormularios = false;
      }
    });
  }

  private loadAvaliacoes(pacienteId: number): void {
    this.loadingAvaliacoes = true;
    this.avaliacaoService.buscarPorPaciente(pacienteId).subscribe({
      next: (avaliacoes) => {
        this.avaliacoes = avaliacoes.sort((a, b) =>
          new Date(b.dataCriacao).getTime() - new Date(a.dataCriacao).getTime()
        );
        this.loadingAvaliacoes = false;
      },
      error: (error) => {
        console.error('Erro ao carregar avaliações:', error);
        this.avaliacoes = [];
        this.loadingAvaliacoes = false;
      }
    });
  }

  navigateToFormulario(formularioId: number, event?: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    if (!this.selectedPaciente) {
      return;
    }

    this.router.navigate(['/formularios', formularioId], {
      queryParams: {
        pacienteId: this.selectedPaciente.id,
        returnUrl: this.router.url
      }
    });
  }

  editarFormulario(formularioId: number, event?: Event): void {
    if (event) {
      event.preventDefault();
      event.stopPropagation();
    }

    this.router.navigate(['/tecnico/formularios/editar', formularioId]);
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


  consultarAvaliacao(avaliacaoId: number): void {
    if (!this.selectedPaciente) return;

    this.router.navigate(['/tecnico/avaliacoes/consultar', this.selectedPaciente.id], {
      queryParams: {
        avaliacaoId: avaliacaoId,
        returnUrl: this.router.url
      }
    });
  }

  editarAvaliacao(avaliacaoId: number): void {
    this.router.navigate(['/tecnico/avaliacoes/editar', avaliacaoId], {
      queryParams: {
        returnUrl: this.router.url
      }
    });
  }

  getBadgeClass(pontuacaoTotal: number, pontuacaoMaxima: number): string {
    if (pontuacaoMaxima === 0) return 'bg-secondary';
    const percentual = (pontuacaoTotal / pontuacaoMaxima) * 100;
    if (percentual >= 80) return 'bg-success';
    if (percentual >= 60) return 'bg-warning';
    return 'bg-danger';
  }

  getPercentual(pontuacaoTotal: number, pontuacaoMaxima: number): number {
    if (pontuacaoMaxima === 0) return 0;
    return Math.round((pontuacaoTotal / pontuacaoMaxima) * 100);
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

  onImageError(event: any) {
    if (event.target) {
      event.target.src = 'https://place-hold.it/192x256';
    }
  }

  logout(): void {
    const token = localStorage.getItem('token');
    if (this.authService.logout(token)) {
      console.log(`token: ${token} apagado com sucesso`);
      this.router.navigate(['/login']);
    }
    else {
      console.log('erro ao realizar o logout')
    }

  }
}