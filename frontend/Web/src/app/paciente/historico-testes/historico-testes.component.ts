import { Component, OnInit } from '@angular/core';
import { PacienteService } from '../../_shared/services/paciente.service';
import { Avaliacao } from '../../_shared/models/avaliacao/avaliacao';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';

@Component({
  selector: 'app-historico-testes',
  templateUrl: './historico-testes.component.html',
  styleUrls: ['./historico-testes.component.css'] // Corrigido plural
})
export class HistoricoTestesComponent implements OnInit {
  testes: Avaliacao[] = [];
  pacienteId = 1;
  paciente: Paciente | undefined; // Paciente pode ser indefinido enquanto não é carregado
  
  isLoading: boolean = true; // Declaração e inicialização da propriedade

  constructor(private pacienteService: PacienteService) {}

  ngOnInit(): void {
    // Carregando os dados do paciente
    this.pacienteService.getPacienteById(this.pacienteId).subscribe(
      (data) => {
        this.paciente = data;
      },
      (error) => {
        console.error('Erro ao carregar dados do paciente:', error);
      }
    );

    // Carregando o histórico de testes
    this.pacienteService.getHistoricoTestes(this.pacienteId).subscribe(
      (data) => {
        this.testes = data || [];
        this.isLoading = false; // Atualiza o estado de carregamento
      },
      (error) => {
        console.error('Erro ao carregar histórico de testes:', error);
        this.isLoading = false; // Atualiza mesmo em caso de erro
      }
    );
  }

  visualizarTeste(testeId: any): void {
    console.log('Teste selecionado:', testeId);
    // Adicione lógica aqui (como redirecionar ou abrir modal)
  }
}
