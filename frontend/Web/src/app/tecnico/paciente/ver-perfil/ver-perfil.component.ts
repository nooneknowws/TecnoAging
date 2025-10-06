import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Avaliacao } from '../../../_shared/models/avaliacao/avaliacao';
import { Paciente } from '../../../_shared/models/pessoa/paciente/paciente';
import { AvaliacaoService } from '../../../_shared/services/avaliacao.service';
import { PacienteService } from '../../../_shared/services/paciente.service';
import { ImageService } from '../../../_shared/services/image.service';

@Component({
  selector: 'app-ver-perfil',
  templateUrl: './ver-perfil.component.html',
  styleUrl: './ver-perfil.component.css'
})
export class VerPerfilComponent {
  paciente?: Paciente;
  avaliacoes: Avaliacao[] = [];
  
  constructor(
    private pacienteService: PacienteService,
    private avaliacaoService: AvaliacaoService,
    private route: ActivatedRoute,
    private router: Router,
    private imageService: ImageService
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const pacienteId = params['id'];
      if (pacienteId) {
        this.carregarPaciente(pacienteId);
      }
    });
  }

  private carregarPaciente(id: number) {
    this.pacienteService.getPacienteById(id).subscribe({
      next: (paciente) => {
        this.paciente = paciente;
        this.carregarFotoPaciente(id);
        this.carregarAvaliacoes(id);
      },
      error: (error) => console.error('Erro ao carregar paciente:', error)
    });
  }

  private carregarFotoPaciente(pacienteId: number) {
    this.imageService.getPacientePhoto(pacienteId).subscribe({
      next: (response) => {
        if (response && response.image && this.paciente) {
          this.paciente.fotoUrl = response.image;
        }
      },
      error: (error) => {
        console.log(`Foto não encontrada para paciente ${pacienteId}`);
      }
    });
  }

  private carregarAvaliacoes(pacienteId: number) {
    this.avaliacaoService.getAvaliacoesByPaciente(pacienteId).subscribe({
      next: (avaliacoes) => this.avaliacoes = avaliacoes,
      error: (error) => console.error('Erro ao carregar avaliações:', error)
    });
  }

  editarPaciente() {
    this.router.navigate(['/tecnico/paciente/editar-perfil'], { 
      queryParams: { id: this.paciente?.id } 
    });
  }

  consultarAvaliacao(avaliacaoId: number) {
    this.router.navigate(['/tecnico/avaliacoes/consultar', this.paciente?.id], {
      queryParams: { avaliacaoId: avaliacaoId }
    });
  }
  
  editarAvaliacao(avaliacaoId: number) {
    this.router.navigate(['/tecnico/avaliacoes/editar', avaliacaoId]);
  }

  voltar() {
    this.router.navigate(['/tecnico']);
  }

  onImageError(event: any) {
    if (event.target) {
      event.target.src = 'https://place-hold.it/192x256';
    }
  }
}