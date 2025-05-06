package br.ufpr.tcc.MSTecnicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.tcc.MSTecnicos.models.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

	Optional<Tecnico> findByCpf(String cpf);
}
