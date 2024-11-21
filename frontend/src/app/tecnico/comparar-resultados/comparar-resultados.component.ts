import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-comparar-resultados',
  templateUrl: './comparar-resultados.component.html',
  styleUrls: ['./comparar-resultados.component.css'],
})
export class CompararResultadosComponent implements OnInit {
  testes = [
    {
      tipo: 'Nível de Atividade Física',
      tempoTotal: '08:30',
      pontuacaoTotal: 85,
      detalhes: [
        { texto: 'Quanto tempo por dia você realiza atividades vigorosas?', resposta: '01:30', media: '00:45' },
        { texto: 'Quantos dias por semana?', resposta: '4', media: '2' },
      ],
    },
    {
      tipo: 'Índice de Vulnerabilidade',
      tempoTotal: '06:20',
      pontuacaoTotal: 72,
      detalhes: [
        { texto: 'Em geral, comparado com outras pessoas da sua idade, como você diria que está a sua saúde?', resposta: 'Regular ou ruim', media: 'Regular ou ruim' },
        { texto: 'Comparada há um ano atrás, como você se classificaria sua saúde em geral, agora?', resposta: 'Igual', media: 'Pior' },
      ],
    },
    {
      tipo: 'Mini Exame do Estado Mental',
      tempoTotal: '07:15',
      pontuacaoTotal: 90,
      detalhes: [
        { texto: 'Que dia é hoje?', resposta: 'Correto', media: '80% Correto' },
        { texto: 'Em que ano estamos?', resposta: 'Correto', media: '85% Correto' },
        { texto: 'Em que estado estamos?', resposta: 'Correto', media: '90% Correto' },
      ],
    },
  ];

  resultadoSelecionado: any = null;

  constructor() {}

  ngOnInit(): void {}

  exibirDetalhes(teste: any): void {
    this.resultadoSelecionado = teste;
  }
}
