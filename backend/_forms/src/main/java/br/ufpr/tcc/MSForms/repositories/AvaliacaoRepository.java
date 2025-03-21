package br.ufpr.tcc.MSForms.repositories;

import br.ufpr.tcc.MSForms.models.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByPacienteId(Long pacienteId);

    List<Avaliacao> findByTecnicoId(Long tecnicoId);

    List<Avaliacao> findByTecnicoIdAndPacienteId(Long tecnicoId, Long pacienteId);
    @Query
    ("SELECT p.texto, r.valor FROM Resposta r JOIN r.pergunta p WHERE r.avaliacao.id = :avaliacaoId")
    List<Object[]> findPerguntasAndValoresByAvaliacaoId(@Param("avaliacaoId") Long avaliacaoId);
    List<Avaliacao> findAllByTecnicoId(Long tecnicoId);
    List<Avaliacao> findAllByPacienteId(Long pacienteId);
}
