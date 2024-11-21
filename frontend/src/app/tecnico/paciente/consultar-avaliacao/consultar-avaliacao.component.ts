//TODO: arrancar esse html estático que o Claude gerou
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AvaliacaoService } from '../../../_shared/services/avaliacao.service';
import { Avaliacao } from '../../../_shared/models/avaliacao/avaliacao';

@Component({
  selector: 'app-consultar-avaliacao',
  template: `
    <div class="container mt-4">
      <h2>Consultar Avaliações</h2>
      
      <div class="card mt-3" *ngFor="let avaliacao of avaliacoes">
        <div class="card-header d-flex justify-content-between align-items-center">
          <h5 class="mb-0">{{avaliacao.formulario!.titulo}}</h5>
          <span class="badge bg-primary">{{avaliacao.dataAtualizacao | date:'dd/MM/yyyy'}}</span>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <p><strong>Paciente:</strong> {{avaliacao.paciente?.nome}}</p>
              <p><strong>Técnico:</strong> {{avaliacao.tecnico?.nome}}</p>
            </div>
            <div class="col-md-6">
              <p><strong>Pontuação:</strong> {{avaliacao.pontuacaoTotal}} / {{avaliacao.pontuacaoMaxima}}</p>
            </div>
          </div>
          
          <div class="mt-3">
            <table class="table table-bordered">
              <thead>
                <tr>
                  <th>Pergunta</th>
                  <th>Resposta</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let resposta of avaliacao.respostas">
                  <td>{{resposta.pergunta?.texto}}</td>
                  <td>{{resposta.resposta}}</td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div class="mt-3">
            <button class="btn btn-primary me-2" (click)="editarAvaliacao(avaliacao.id)">
              Editar
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class ConsultarAvaliacaoComponent implements OnInit {
  avaliacoes: Avaliacao[] = [];
  
  constructor(
    private avaliacaoService: AvaliacaoService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const pacienteId = this.route.snapshot.params['pacienteId'];
    if (pacienteId) {
      this.avaliacaoService.getAvaliacoesByPaciente(pacienteId)
        .subscribe({
          next: (avaliacoes) => {
            this.avaliacoes = avaliacoes;
          },
          error: (error) => {
            console.error('Erro ao carregar avaliações:', error);
          }
        });
    }
  }

  editarAvaliacao(avaliacaoId: number | undefined) {
    if (avaliacaoId) {
      this.router.navigate(['/avaliacoes/editar', avaliacaoId]);
    }
  }
}
