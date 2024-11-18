package br.ufpr.tcc.MSForms.rest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpr.tcc.MSForms.models.*;
import br.ufpr.tcc.MSForms.models.dto.*;
import br.ufpr.tcc.MSForms.repositories.AvaliacaoRepository;
import br.ufpr.tcc.MSForms.repositories.FormularioRepository;
import br.ufpr.tcc.MSForms.repositories.PacienteRepository;
import br.ufpr.tcc.MSForms.repositories.PerguntaRepository;
import br.ufpr.tcc.MSForms.repositories.TecnicoRepository;
import br.ufpr.tcc.MSForms.service.AvaliacaoService;

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

    @Autowired
    private PerguntaRepository perguntaRepository;
    
    @Autowired
    private AvaliacaoService avaliacaoService;

   

    @PostMapping("/forms")
    public ResponseEntity<Map<String, String>> salvarAvaliacao(@RequestBody AvaliacaoDTO avaliacaoDTO) {
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

        if (avaliacao.getRespostas() == null) {
            avaliacao.setRespostas(new ArrayList<>());
        }

        for (RespostaDTO respostaDTO : avaliacaoDTO.getRespostas()) {
            Pergunta pergunta = perguntaRepository.findById(respostaDTO.getPerguntaId())
                    .orElseThrow(() -> new RuntimeException("Pergunta não encontrada"));

            Resposta resposta = new Resposta(pergunta, avaliacao, respostaDTO.getValor());
            avaliacao.getRespostas().add(resposta);
        }

        avaliacaoRepository.save(avaliacao);

        return ResponseEntity.ok(Map.of("message", "Avaliação salva com sucesso"));
    }
    @GetMapping("/respostas/paciente/{id}")
    public ResponseEntity<List<RespostaAvaliacaoPaciente>> getRespostasByPaciente(@PathVariable("id") Long pacienteId) {
        
        List<Avaliacao> avaliacoes = avaliacaoService.findAllByPacienteId(pacienteId);
        
        List<RespostaAvaliacaoPaciente> avaliacaoResponses = new ArrayList<>();

        for (Avaliacao avaliacao : avaliacoes) {
            Tecnico tecnico = avaliacao.getTecnico();
            Formulario formulario = avaliacao.getFormulario();
            List<Resposta> respostas = avaliacao.getRespostas(); 
            List<PerguntaValorDTO> perguntaValorList = respostas.stream()
                .map(resposta -> new PerguntaValorDTO(resposta.getPergunta().getTexto(), resposta.getValor()))
                .collect(Collectors.toList());

            RespostaAvaliacaoPaciente response = new RespostaAvaliacaoPaciente(
                tecnico.getId(), 
                tecnico.getNome(), 
                formulario.getTitulo(),
                formulario.getDescricao(),
                perguntaValorList
            );

            avaliacaoResponses.add(response);
        }

        return ResponseEntity.ok(avaliacaoResponses);
    }
    @GetMapping("/respostas/tecnico/{id}")
    public ResponseEntity<List<RespostaAvaliacaoTecnico>> getRespostasByTecnico(@PathVariable("id") Long tecnicoId) {
        
        List<Avaliacao> avaliacoes = avaliacaoService.findAllByTecnicoId(tecnicoId);
        
        List<RespostaAvaliacaoTecnico> avaliacaoResponses = new ArrayList<>();

        for (Avaliacao avaliacao : avaliacoes) {
            Paciente paciente = avaliacao.getPaciente();
            List<Resposta> respostas = avaliacao.getRespostas(); 
            List<PerguntaValorDTO> perguntaValorList = respostas.stream()
                .map(resposta -> new PerguntaValorDTO(resposta.getPergunta().getTexto(), resposta.getValor()))
                .collect(Collectors.toList());

            RespostaAvaliacaoTecnico response = new RespostaAvaliacaoTecnico(
                paciente.getId(), 
                paciente.getNome(), 
                perguntaValorList
            );

            avaliacaoResponses.add(response);
        }

        // Return the response
        return ResponseEntity.ok(avaliacaoResponses);
    }


}
