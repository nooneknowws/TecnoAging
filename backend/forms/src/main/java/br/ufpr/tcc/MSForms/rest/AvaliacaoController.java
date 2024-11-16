package br.ufpr.tcc.MSForms.rest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpr.tcc.MSForms.models.*;
import br.ufpr.tcc.MSForms.models.dto.AvaliacaoDTO;
import br.ufpr.tcc.MSForms.repositories.AvaliacaoRepository;
import br.ufpr.tcc.MSForms.repositories.FormularioRepository;
import br.ufpr.tcc.MSForms.repositories.PacienteRepository;
import br.ufpr.tcc.MSForms.repositories.TecnicoRepository;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    @PostMapping("/forms")
    public ResponseEntity<String> salvarAvaliacao(@RequestBody AvaliacaoDTO avaliacaoDTO) {
        Paciente paciente = pacienteRepository.findById(avaliacaoDTO.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        Tecnico tecnico = tecnicoRepository.findById(avaliacaoDTO.getTecnicoId())
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
        Formulario formulario = formularioRepository.findById(avaliacaoDTO.getFormularioId())
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado"));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setPaciente(paciente);
        avaliacao.setTecnico(tecnico);
        avaliacao.setFormulario(formulario);
        avaliacao.setPontuacaoTotal(avaliacaoDTO.getPontuacaoTotal());
        avaliacao.setDataCriacao(LocalDateTime.now());
        avaliacao.setDataAtualizacao(LocalDateTime.now());

        avaliacaoRepository.save(avaliacao);

        return ResponseEntity.ok("Avaliação salva com sucesso");
    }
}
