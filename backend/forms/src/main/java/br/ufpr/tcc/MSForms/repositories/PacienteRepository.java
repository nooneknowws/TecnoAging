package br.ufpr.tcc.MSForms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.tcc.MSForms.models.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
