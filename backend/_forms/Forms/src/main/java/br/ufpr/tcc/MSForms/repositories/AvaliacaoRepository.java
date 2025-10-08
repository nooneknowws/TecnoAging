package br.ufpr.tcc.MSForms.repositories;

import br.ufpr.tcc.MSForms.models.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByPaciente(Long pacienteId);

    List<Avaliacao> findByTecnico(Long tecnicoId);

    List<Avaliacao> findByTecnicoAndPaciente(Long tecnicoId, Long pacienteId);

    @Query("SELECT p.texto, r.valor FROM Resposta r JOIN r.pergunta p WHERE r.avaliacao.id = :avaliacaoId")
    List<Object[]> findPerguntasAndValoresByAvaliacaoId(@Param("avaliacaoId") Long avaliacaoId);

    @Query("SELECT COUNT(a) > 0 FROM Avaliacao a WHERE a.formulario.id = :formularioId")
    boolean existsByFormularioId(@Param("formularioId") Long formularioId);

}