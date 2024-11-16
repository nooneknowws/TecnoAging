import { Component, OnInit } from '@angular/core';
import { PacienteService } from '../../_shared/services/paciente.service';

@Component({
  selector: 'app-historico-testes',
  templateUrl: './historico-testes.component.html',
  styleUrls: ['./historico-testes.component.scss']
})
export class HistoricoTestesComponent implements OnInit {
  testes: any[] = [];
  isLoading: boolean = true; // Declaração e inicialização da propriedade

  constructor(private pacienteService: PacienteService) {}

  ngOnInit(): void {
    const pacienteId = 1; // Substituir pelo ID real do paciente autenticado
    this.pacienteService.getHistoricoTestes(pacienteId).subscribe(
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

  visualizarTeste(testeId: string): void {
    console.log('Teste selecionado:', testeId);
    // Adicione lógica aqui (como redirecionar ou abrir modal)
  }
}
