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
  pontuacaoMaxima: number;
}

@Component({
  selector: 'app-paciente-dashboard',
  templateUrl: './paciente-dashboard.component.html',
  styleUrl: './paciente-dashboard.component.css',
})
export class PacienteDashboardComponent implements OnInit {
  activeSection: string = 'dashboard';
  currentPaciente: Paciente | null = null;
  currentPhotoUrl: string | null = null;
  estatisticas: Estatisticas | null = null;
  atividadesRecentes: AtividadeRecente[] = [];
  historicoTestes: HistoricoTeste[] = [];
  formulariosDisponiveis: Formulario[] = [];
  expandedAvaliacaoId: number | null = null;
  avaliacaoDetalhada: any = null;

  constructor(
    private router: Router,
    private authService: AuthService,
    private pacienteService: PacienteService,
    private avaliacaoService: AvaliacaoService,
    private formularioService: FormularioService,
    private imageService: ImageService
  ) {}

  ngOnInit(): void {
    if (this.router.url.includes('/paciente/formularios')) {
      this.activeSection = 'testes';
    } else {
      this.activeSection = 'dashboard';
    }

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
        },
        error: (error) => {
          console.error('Erro ao carregar dados do paciente:', error);
        },
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
      },
    });
  }

  loadEstatisticas(): void {
    if (this.currentPaciente) {
      this.avaliacaoService
        .buscarPorPaciente(this.currentPaciente.id!)
        .subscribe({
          next: (avaliacoes) => {
            const testesRealizados = avaliacoes.length;

            let ultimaAvaliacaoStr = 'N/A';
            let diasUltimoTeste = 0;

            if (avaliacoes.length > 0) {
              // Ordenar por data de criação (mais recente primeiro)
              const avaliacoesOrdenadas = avaliacoes.sort(
                (a, b) =>
                  new Date(b.dataCriacao).getTime() -
                  new Date(a.dataCriacao).getTime()
              );

              const ultimaAvaliacao = avaliacoesOrdenadas[0];
              const pontuacaoTotal = ultimaAvaliacao.pontuacaoTotal || 0;
              const pontuacaoMaxima = ultimaAvaliacao.pontuacaoMaxima || 1;
              const percentual = Math.round(
                (pontuacaoTotal / pontuacaoMaxima) * 100
              );

              ultimaAvaliacaoStr = `${pontuacaoTotal}/${pontuacaoMaxima} (${percentual}%)`;

              const ultimaData = new Date(ultimaAvaliacao.dataCriacao);
              const hoje = new Date();
              diasUltimoTeste = Math.floor(
                (hoje.getTime() - ultimaData.getTime()) / (1000 * 3600 * 24)
              );
            }

            this.estatisticas = {
              testesRealizados,
              testesPendentes: 0,
              ultimaAvaliacao: ultimaAvaliacaoStr,
              diasUltimoTeste,
            };
          },
          error: (error) => {
            console.error('Erro ao carregar estatísticas:', error);
            this.estatisticas = {
              testesRealizados: 0,
              testesPendentes: 0,
              ultimaAvaliacao: 'N/A',
              diasUltimoTeste: 0,
            };
          },
        });
    }
  }

  loadHistoricoTestes(): void {
    if (this.currentPaciente) {
      this.avaliacaoService
        .buscarPorPaciente(this.currentPaciente.id!)
        .subscribe({
          next: (avaliacoes) => {
            this.historicoTestes = avaliacoes.map((avaliacao) => ({
              id: avaliacao.id,
              formulario: avaliacao.formulario?.titulo || 'Formulário',
              dataCriacao: new Date(avaliacao.dataCriacao),
              tecnico: avaliacao.tecnico?.nome || 'Autoavaliação',
              pontuacaoTotal: Math.round(avaliacao.pontuacaoTotal || 0),
              pontuacaoMaxima: Math.round(avaliacao.pontuacaoMaxima || 0),
            }));
          },
          error: (error) => {
            console.error('Erro ao carregar histórico de testes:', error);
          },
        });
    }
  }

  loadAtividadesRecentes(): void {
    if (this.currentPaciente) {
      this.avaliacaoService
        .buscarPorPaciente(this.currentPaciente.id!)
        .subscribe({
          next: (avaliacoes) => {
            this.atividadesRecentes = avaliacoes
              .slice(-3) // Últimas 3 atividades
              .map((avaliacao) => ({
                formulario: avaliacao.formulario?.titulo || 'Formulário',
                data: new Date(avaliacao.dataCriacao),
                tecnico: avaliacao.tecnico?.nome || 'Autoavaliação',
                status: 'Concluído',
              }));
          },
          error: (error) => {
            console.error('Erro ao carregar atividades recentes:', error);
          },
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
      },
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

  // Método removido - funcionalidade substituída por visualizarDetalhesAvaliacao
  // que exibe os detalhes inline no dashboard
  // visualizarTeste(testeId: number): void {
  //   if (this.currentPaciente?.id) {
  //     this.router.navigate(['/paciente/visualizar-avaliacao', this.currentPaciente.id], {
  //       queryParams: { avaliacaoId: testeId }
  //     });
  //   }
  // }

  visualizarDetalhesAvaliacao(avaliacaoId: number): void {
    // Exibir detalhes da avaliação com respostas expandidas
    this.expandedAvaliacaoId =
      this.expandedAvaliacaoId === avaliacaoId ? null : avaliacaoId;

    // Se expandindo, carregar os detalhes completos
    if (this.expandedAvaliacaoId === avaliacaoId) {
      this.avaliacaoService.getAvaliacaoById(avaliacaoId).subscribe({
        next: (avaliacao) => {
          this.avaliacaoDetalhada = avaliacao;
        },
        error: (error) => {
          console.error('Erro ao carregar detalhes da avaliação:', error);
        },
      });
    } else {
      this.avaliacaoDetalhada = null;
    }
  }

  iniciarFormulario(idFormulario: number): void {
    this.router.navigate([`/formularios/${idFormulario}`]);
  }

  getBadgeClass(pontuacaoTotal: number, pontuacaoMaxima: number): string {
    const percentual = (pontuacaoTotal / pontuacaoMaxima) * 100;
    if (percentual >= 80) return 'bg-success';
    if (percentual >= 60) return 'bg-warning';
    return 'bg-danger';
  }

  getPercentual(pontuacaoTotal: number, pontuacaoMaxima: number): number {
    if (pontuacaoMaxima === 0) return 0;
    return Math.round((pontuacaoTotal / pontuacaoMaxima) * 100);
  }

  formatarValorResposta(valor: any): string {
    if (
      typeof valor === 'string' &&
      valor.startsWith('"') &&
      valor.endsWith('"')
    ) {
      return valor.slice(1, -1);
    }
    return String(valor || '');
  }
}
