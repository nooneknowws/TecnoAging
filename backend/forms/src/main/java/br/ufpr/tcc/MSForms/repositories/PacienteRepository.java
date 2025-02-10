package br.ufpr.tcc.MSForms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.tcc.MSForms.models.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	Optional<Paciente> findByCpf(String cpf);
}
