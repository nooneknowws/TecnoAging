package br.ufpr.tcc.MSForms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.ufpr.tcc.MSForms.models.Pergunta;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long>  {

    @Query("SELECT p FROM Pergunta p JOIN p.etapa e WHERE e.formulario.id = :formularioId ORDER BY e.id ASC, p.id ASC")
    List<Pergunta> findByFormularioIdOrderByEtapaIdAscIdAsc(@Param("formularioId") Long formularioId);

}
