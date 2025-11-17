import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Tecnico } from '../../_shared/models/pessoa/tecnico/tecnico';
import { TecnicoService } from '../../_shared/services/tecnico.service';
import { AuthService } from '../../_shared/services/auth.service';
import { ImageService } from '../../_shared/services/image.service';

type ViewMode = 'grid' | 'table';

@Component({
  selector: 'app-gerenciar-tecnicos',
  templateUrl: './gerenciar-tecnicos.component.html',
  styleUrls: ['./gerenciar-tecnicos.component.css']
})
export class GerenciarTecnicosComponent implements OnInit {
  tecnicos: Tecnico[] = [];
  tecnicosFiltrados: Tecnico[] = [];
  currentTecnicoId: number | null = null;
  viewMode: ViewMode = 'grid';
  loading = false;
  searchTerm = '';
  statusFilter: 'todos' | 'ativos' | 'inativos' = 'todos';

  constructor(
    private tecnicoService: TecnicoService,
    private authService: AuthService,
    private imageService: ImageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.currentTecnicoId = currentUser.id;
    }
    this.loadTecnicos();
  }

  loadTecnicos(): void {
    this.loading = true;
    this.tecnicoService.getTecnicos().subscribe({
      next: (tecnicos) => {
        this.tecnicos = tecnicos;
        this.loadTecnicosPhotos();
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        console.error('Erro ao carregar técnicos:', error);
        this.loading = false;
      }
    });
  }

  private loadTecnicosPhotos(): void {
    this.tecnicos.forEach(tecnico => {
      if (tecnico.id) {
        this.imageService.getTecnicoPhoto(tecnico.id).subscribe({
          next: (response) => {
            if (response && response.image) {
              tecnico.fotoUrl = response.image;
            }
          },
          error: (error) => {
            console.log(`Foto não encontrada para técnico ${tecnico.id}`);
          }
        });
      }
    });
  }

  applyFilters(): void {
    this.tecnicosFiltrados = this.tecnicos.filter(tecnico => {
      const matchesSearch = !this.searchTerm ||
        tecnico.nome?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        tecnico.cpf?.includes(this.searchTerm) ||
        tecnico.matricula?.toString().includes(this.searchTerm);

      const matchesStatus =
        this.statusFilter === 'todos' ||
        (this.statusFilter === 'ativos' && tecnico.ativo) ||
        (this.statusFilter === 'inativos' && !tecnico.ativo);

      return matchesSearch && matchesStatus;
    });
  }

  onSearchChange(term: string): void {
    this.searchTerm = term;
    this.applyFilters();
  }

  onStatusFilterChange(status: 'todos' | 'ativos' | 'inativos'): void {
    this.statusFilter = status;
    this.applyFilters();
  }

  toggleView(mode: ViewMode): void {
    this.viewMode = mode;
  }

  cadastrarTecnico(): void {
    this.router.navigate(['/tecnico/cadastrar-tecnico']);
  }

  editarTecnico(tecnico: Tecnico, event?: Event): void {
    if (event) {
      event.stopPropagation();
    }
    this.router.navigate(['/tecnico/editar-tecnico', tecnico.id]);
  }

  toggleStatus(tecnico: Tecnico, event?: Event): void {
    if (event) {
      event.stopPropagation();
    }

    if (tecnico.id === this.currentTecnicoId) {
      alert('Você não pode desativar sua própria conta!');
      return;
    }

    const acao = tecnico.ativo ? 'desativar' : 'ativar';
    const confirmacao = confirm(`Deseja realmente ${acao} o técnico ${tecnico.nome}?`);

    if (!confirmacao) return;

    const serviceCall = tecnico.ativo
      ? this.tecnicoService.desativarTecnico(tecnico.id!)
      : this.tecnicoService.ativarTecnico(tecnico.id!);

    serviceCall.subscribe({
      next: () => {
        tecnico.ativo = !tecnico.ativo;
        this.applyFilters();
      },
      error: (error) => {
        console.error(`Erro ao ${acao} técnico:`, error);
        alert(`Erro ao ${acao} técnico. Tente novamente.`);
      }
    });
  }

  getStatusBadgeClass(ativo: boolean | undefined): string {
    return ativo ? 'badge bg-success' : 'badge bg-secondary';
  }

  getStatusText(ativo: boolean | undefined): string {
    return ativo ? 'Ativo' : 'Inativo';
  }

  onImageError(event: any): void {
    if (event.target) {
      event.target.src = 'https://place-hold.it/192x256';
    }
  }

  isCurrentUser(tecnico: Tecnico): boolean {
    return tecnico.id === this.currentTecnicoId;
  }
}
