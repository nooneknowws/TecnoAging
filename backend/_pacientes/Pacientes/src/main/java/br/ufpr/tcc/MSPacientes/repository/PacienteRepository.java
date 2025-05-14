package br.ufpr.tcc.MSPacientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.tcc.MSPacientes.models.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	Optional<Paciente> findByCpf(String cpf);
}
