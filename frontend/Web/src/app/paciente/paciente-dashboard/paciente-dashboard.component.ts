import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../_shared/services/auth.service';
import { PacienteService } from '../../_shared/services/paciente.service';
import { AvaliacaoService } from '../../_shared/services/avaliacao.service';
import { FormularioService } from '../../_shared/services/formulario.service';
import { ImageService } from '../../_shared/services/image.service';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';
import { Formulario } from '../../_shared/models/formulario/formulario';

interface Estatisticas {
  testesRealizados: number;
  testesPendentes: number;
  ultimaAvaliacao: string;
  diasUltimoTeste: number;
}

interface AtividadeRecente {
  formulario: string;
  data: Date;
  tecnico: any;
  status: string;
}

interface HistoricoTeste {
  id: number | undefined;
  formulario: string;
  dataCriacao: Date;
  tecnico: any;
  pontuacaoTotal: number;
}

interface Metricas {
  desempenhoCognitivo: number;
  nivelFadiga: number;
  vulnerabilidadeClinica: number;
  qualidadeVida: number;
}

interface ResultadoDetalhado {
  id: number | undefined;
  formulario: string;
  data: Date;
  pontuacao: number;
  observacoes: string;
}

@Component({
  selector: 'app-paciente-dashboard',
  templateUrl: './paciente-dashboard.component.html',
  styleUrl: './paciente-dashboard.component.css'
})
export class PacienteDashboardComponent implements OnInit {
  activeSection: string = 'dashboard';
  currentPaciente: Paciente | null = null;
  currentPhotoUrl: string | null = null;
  estatisticas: Estatisticas | null = null;
  atividadesRecentes: AtividadeRecente[] = [];
  historicoTestes: HistoricoTeste[] = [];
  formulariosDisponiveis: Formulario[] = [];
  metricas: Metricas | null = null;
  resultadosDetalhados: ResultadoDetalhado[] = [];

  constructor(
    private router: Router,
    private authService: AuthService,
    private pacienteService: PacienteService,
    private avaliacaoService: AvaliacaoService,
    private formularioService: FormularioService,
    private imageService: ImageService
  ) {}

  ngOnInit(): void {
    this.loadCurrentPaciente();
    this.loadFormulariosDisponiveis();
  }

  loadCurrentPaciente(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.cpf) {
      this.pacienteService.getPacienteById(currentUser.id!).subscribe({
        next: (paciente) => {
          this.currentPaciente = paciente;
          this.loadCurrentPhoto(paciente.id!);
          this.loadEstatisticas();
          this.loadHistoricoTestes();
          this.loadAtividadesRecentes();
          this.loadMetricas();
          this.loadResultadosDetalhados();
        },
        error: (error) => {
          console.error('Erro ao carregar dados do paciente:', error);
        }
      });
    }
  }

  loadFormulariosDisponiveis(): void {
    this.formularioService.listarFormularios().subscribe({
      next: (formularios) => {
        this.formulariosDisponiveis = formularios;
      },
      error: (error) => {
        console.error('Erro ao carregar formulários:', error);
      }
    });
  }

  loadEstatisticas(): void {
    if (this.currentPaciente) {
      this.avaliacaoService.buscarPorPaciente(this.currentPaciente.id!).subscribe({
        next: (avaliacoes) => {
          const testesRealizados = avaliacoes.length;
          const ultimaAvaliacao = avaliacoes.length > 0 ? 
            Math.round(avaliacoes[avaliacoes.length - 1].pontuacaoTotal || 0) : 0;
          
          let diasUltimoTeste = 0;
          if (avaliacoes.length > 0) {
            const ultimaData = new Date(avaliacoes[avaliacoes.length - 1].dataCriacao);
            const hoje = new Date();
            diasUltimoTeste = Math.floor((hoje.getTime() - ultimaData.getTime()) / (1000 * 3600 * 24));
          }

          this.estatisticas = {
            testesRealizados,
            testesPendentes: 0, // Implementar lógica para testes pendentes
            ultimaAvaliacao: ultimaAvaliacao.toString(),
            diasUltimoTeste
          };
        },
        error: (error) => {
          console.error('Erro ao carregar estatísticas:', error);
          this.estatisticas = {
            testesRealizados: 0,
            testesPendentes: 0,
            ultimaAvaliacao: 'N/A',
            diasUltimoTeste: 0
          };
        }
      });
    }
  }

  loadHistoricoTestes(): void {
    if (this.currentPaciente) {
      this.avaliacaoService.buscarPorPaciente(this.currentPaciente.id!).subscribe({
        next: (avaliacoes) => {
          this.historicoTestes = avaliacoes.map(avaliacao => ({
            id: avaliacao.id,
            formulario: avaliacao.formulario?.titulo || 'Formulário',
            dataCriacao: new Date(avaliacao.dataCriacao),
            tecnico: avaliacao.tecnico?.nome || 'Técnico',
            pontuacaoTotal: Math.round(avaliacao.pontuacaoTotal || 0)
          }));
        },
        error: (error) => {
          console.error('Erro ao carregar histórico de testes:', error);
        }
      });
    }
  }

  loadAtividadesRecentes(): void {
    if (this.currentPaciente) {
      this.avaliacaoService.buscarPorPaciente(this.currentPaciente.id!).subscribe({
        next: (avaliacoes) => {
          this.atividadesRecentes = avaliacoes
            .slice(-3) // Últimas 3 atividades
            .map(avaliacao => ({
              formulario: avaliacao.formulario?.titulo || 'Formulário',
              data: new Date(avaliacao.dataCriacao),
              tecnico: avaliacao.tecnico?.nome || 'Técnico',
              status: 'Concluído'
            }));
        },
        error: (error) => {
          console.error('Erro ao carregar atividades recentes:', error);
        }
      });
    }
  }

  loadMetricas(): void {
    if (this.currentPaciente) {
      this.metricas = {
        desempenhoCognitivo: 85,
        nivelFadiga: 3,
        vulnerabilidadeClinica: 2,
        qualidadeVida: 78
      };
    }
  }

  loadResultadosDetalhados(): void {
    if (this.currentPaciente) {
      this.avaliacaoService.buscarPorPaciente(this.currentPaciente.id!).subscribe({
        next: (avaliacoes) => {
          this.resultadosDetalhados = avaliacoes.map(avaliacao => ({
            id: avaliacao.id,
            formulario: avaliacao.formulario?.titulo || 'Formulário',
            data: new Date(avaliacao.dataCriacao),
            pontuacao: Math.round(avaliacao.pontuacaoTotal || 0),
            observacoes: 'Resultado dentro dos parâmetros esperados.'
          }));
        },
        error: (error) => {
          console.error('Erro ao carregar resultados detalhados:', error);
        }
      });
    }
  }

  loadCurrentPhoto(pacienteId: number): void {
    this.imageService.getPacientePhoto(pacienteId).subscribe({
      next: (response) => {
        this.currentPhotoUrl = response.image;
      },
      error: () => {
        this.currentPhotoUrl = null;
      }
    });
  }

  switchSection(section: string): void {
    this.activeSection = section;
  }

  logout(): void {    
    const token = localStorage.getItem('token');
    this.authService.logout(token);
    this.router.navigate(['/login']);
  }

  visualizarTeste(testeId: number): void {
    console.log('Visualizar teste:', testeId);
  }

  iniciarFormulario(tipoFormulario: string): void {
    this.router.navigate(['/formularios/preencher'], { 
      queryParams: { tipo: tipoFormulario } 
    });
  }

  getBadgeClass(pontuacao: number): string {
    if (pontuacao >= 80) return 'bg-success';
    if (pontuacao >= 60) return 'bg-warning';
    return 'bg-danger';
  }

  getMetricStatusClass(valor: number): string {
    if (valor >= 80) return 'good';
    if (valor >= 60) return 'warning';
    return 'danger';
  }

  getMetricStatus(valor: number): string {
    if (valor >= 80) return 'Bom';
    if (valor >= 60) return 'Moderado';
    return 'Atenção';
  }

  verDetalhes(resultadoId: number): void {
    // Implementar navegação para detalhes do resultado
    console.log('Ver detalhes do resultado:', resultadoId);
  }
}

