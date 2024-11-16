package br.ufpr.tcc.MSForms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.tcc.MSForms.models.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
}
