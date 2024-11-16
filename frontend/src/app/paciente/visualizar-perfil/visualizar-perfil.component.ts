import { Component, OnInit } from '@angular/core';
import { PacienteService } from '../../_shared/services/paciente.service';
import { Paciente } from '../../_shared/models/pessoa/paciente/paciente';

@Component({
  selector: 'app-visualizar-perfil',
  templateUrl: './visualizar-perfil.component.html',
  styleUrl: './visualizar-perfil.component.css'
})
export class VisualizarPerfilComponent implements OnInit {
  paciente: Paciente | null = null; // Armazena o paciente carregado
  showDadosPessoais: boolean = false;

  constructor(private pacienteService: PacienteService) {}

  ngOnInit(): void {
    // Simulando ID do paciente para teste
    const pacienteId = 1;
    this.pacienteService.getPacienteById(pacienteId).subscribe((data) => {
      this.paciente = data;
    });
  }

  toggleDadosPessoais(): void {
    this.showDadosPessoais = !this.showDadosPessoais;
  }
}
