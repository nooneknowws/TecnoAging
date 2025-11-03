import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AvaliacaoService } from '../../_shared/services/avaliacao.service';
import { Avaliacao } from '../../_shared/models/avaliacao/avaliacao';

@Component({
  selector: 'app-comparar-resultados',
  templateUrl: './comparar-resultados.component.html',
  styleUrls: ['./comparar-resultados.component.css'],
})
export class CompararResultadosComponent implements OnInit {
  testes: any[] = [];
  resultadoSelecionado: any = null;

  constructor(
    private route: ActivatedRoute,
    private avaliacaoService: AvaliacaoService
  ) {}

  ngOnInit(): void {
    const tecnicoId = this.route.snapshot.params['tecnicoId'];
    if (tecnicoId) {
      this.avaliacaoService.getAvaliacaoByIdTecnico(tecnicoId).subscribe({
        next: (avaliacoes: Avaliacao[]) => {
          this.testes = avaliacoes.map(a => ({
            tipo: a.formulario?.titulo ?? 'Sem título',
            dataFinalizacao: a.dataAtualizacao ? new Date(a.dataAtualizacao).toLocaleString('pt-BR') : '-',
            pontuacaoTotal: a.pontuacaoTotal ?? 0,
            detalhes: (a.respostas ?? []).map(r => ({
              texto: r.pergunta?.texto ?? '-',
              resposta: this.formatarValorResposta(r.valor),
              media: '-'
            }))
          }));
        },
        error: (err) => {
          console.error('Erro ao carregar avaliações para comparar:', err);
        }
      });
    } else {
      this.testes = [];
    }
    console.log(tecnicoId, 'Testes carregados para comparação:', this.testes);
  }

  exibirDetalhes(teste: any): void {
    this.resultadoSelecionado = teste;
  }

  formatarValorResposta(valor: any): string {
    if (typeof valor === 'string' && valor.startsWith('"') && valor.endsWith('"')) {
      return valor.slice(1, -1);
    }
    return String(valor ?? '');
  }
}
