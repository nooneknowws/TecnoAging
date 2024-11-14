import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-form-sedentarismo',
  templateUrl: './form-sedentarismo.component.html',
  styleUrl: './form-sedentarismo.component.css'
})
export class FormSedentarismoComponent implements OnInit {
  etapas = [
    {
      titulo: 'Atividade Moderada',
      descricao: 'Você consegue realizá-la conversando com dificuldade enquanto se movimenta e não vai conseguir cantar.',
      perguntas: [
        {
          texto: 'Quanto tempo por dia você realiza atividades moderadas?',
          tipo: 'tempo',
          resposta: ''
        },
        {
          texto: 'Quantos dias por semana?',
          tipo: 'dias',
          resposta: ''
        }
      ]
    },
    {
      titulo: 'Atividade Vigorosa',
      descricao: 'Você não vai conseguir nem conversar. A sua respiração vai ser muito mais rápida que o normal e os batimentos do seu coração vão aumentar muito.',
      perguntas: [
        {
          texto: 'Quanto tempo por dia você realiza atividades vigorosas?',
          tipo: 'tempo',
          resposta: ''
        },
        {
          texto: 'Quantos dias por semana?',
          tipo: 'dias',
          resposta: ''
        }
      ]
    },
    {
      titulo: 'Comportamento Sedentário',
      descricao: 'Quanto tempo do seu dia, enquanto você está acordado, você gasta sentado, reclinado ou deitado, assistindo televisão, no celular, em frente ao computador, realizando trabalhos manuais, dirigindo ou lendo?',
      perguntas: [
        {
          texto: 'Quanto tempo por dia?',
          tipo: 'tempo',
          resposta: ''
        },
        {
          texto: 'Quantos dias por semana?',
          tipo: 'dias',
          resposta: ''
        }
      ]
    }
  ];

  etapaAtual = 0;

  constructor() { }

  ngOnInit(): void {
  }

  proximaEtapa(): void {
    if (this.etapaAtual < this.etapas.length - 1) {
      this.etapaAtual++;
    }
  }

  etapaAnterior(): void {
    if (this.etapaAtual > 0) {
      this.etapaAtual--;
    }
  }

  salvarRespostas(): void {
    console.log('Respostas salvas:', this.etapas);
  }
}