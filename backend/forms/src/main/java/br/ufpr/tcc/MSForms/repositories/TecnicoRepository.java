package br.ufpr.tcc.MSForms.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.tcc.MSForms.models.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

	Optional<Tecnico> findByCpf(String cpf);
}
