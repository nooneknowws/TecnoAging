import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AvaliacaoService } from '../../../_shared/services/avaliacao.service';
import { Avaliacao } from '../../../_shared/models/avaliacao/avaliacao';

@Component({
  selector: 'app-consultar-avaliacao',
  templateUrl: './consultar-avaliacao.component.html',
  styleUrls: ['./consultar-avaliacao.component.css']
})

export class ConsultarAvaliacaoComponent implements OnInit {
  avaliacoes: Avaliacao[] = [];
  expanded = new Map<number, boolean>();
  
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
            console.log(JSON.stringify(avaliacoes));
          },
          error: (error) => {
            console.error('Erro ao carregar avaliações:', error);
          }
        });
    }
  }

  formatarValorResposta(valor: any): string {
    if (typeof valor === 'string' && valor.startsWith('"') && valor.endsWith('"')) {
      return valor.slice(1, -1);
    }
    return String(valor || '');
  }

  editarAvaliacao(avaliacaoId: number | undefined) {
    if (avaliacaoId) {
      this.router.navigate(['/tecnico/avaliacoes/editar', avaliacaoId]);
    }
  }

  togglePerguntas(index: number) {
    const currentValue = this.expanded.get(index);
    this.expanded.set(index, !currentValue);
  }

  voltarParaLista() {
    this.router.navigate(['/tecnico']);
  }
}
