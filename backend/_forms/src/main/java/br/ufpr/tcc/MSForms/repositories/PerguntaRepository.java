package br.ufpr.tcc.MSForms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.tcc.MSForms.models.Pergunta;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long>  {

}
