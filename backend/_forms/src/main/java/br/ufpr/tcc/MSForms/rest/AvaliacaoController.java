package br.ufpr.tcc.MSForms.rest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpr.tcc.MSForms.models.*;
import br.ufpr.tcc.MSForms.models.dto.*;
import br.ufpr.tcc.MSForms.repositories.AvaliacaoRepository;
import br.ufpr.tcc.MSForms.repositories.FormularioRepository;
import br.ufpr.tcc.MSForms.repositories.PerguntaRepository;
import br.ufpr.tcc.MSForms.service.AvaliacaoService;
import br.ufpr.tcc.MSForms.service.FormsService;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {



    @Autowired
    private FormsService messageSender;
    
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    @Autowired
    private PerguntaRepository perguntaRepository;
    
    @Autowired
    private AvaliacaoService avaliacaoService;

   
    @PostMapping("/forms")
    public ResponseEntity<Map<String, String>> salvarAvaliacao(@RequestBody AvaliacaoDTO avaliacaoDTO) throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("AvaliacaoDTO:");
        System.out.println("pacienteId: " + avaliacaoDTO.getPacienteId());
        System.out.println("tecnicoId: " + avaliacaoDTO.getTecnicoId());
        System.out.println("formularioId: " + avaliacaoDTO.getFormularioId());
        System.out.println("pontuacaoTotal: " + avaliacaoDTO.getPontuacaoTotal());
        
        if (avaliacaoDTO.getRespostas() != null) {
            System.out.println("Número de respostas: " + avaliacaoDTO.getRespostas().size());
            for (int i = 0; i < avaliacaoDTO.getRespostas().size(); i++) {
                RespostaDTO resp = avaliacaoDTO.getRespostas().get(i);
                System.out.println("Resposta #" + i + ": perguntaId=" + resp.getPerguntaId() + ", valor=" + resp.getValor());
                
                if (resp.getPerguntaId() == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "ID da pergunta não pode ser nulo na resposta #" + i));
                }
            }
        }
        CompletableFuture<PacienteDTO> pacienteFuture = messageSender.requestPacienteData(avaliacaoDTO.getPacienteId());
        CompletableFuture<TecnicoDTO> tecnicoFuture = messageSender.requestTecnicoData(avaliacaoDTO.getTecnicoId());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(pacienteFuture, tecnicoFuture);
        allFutures.get(10, TimeUnit.SECONDS); 

        PacienteDTO paciente = pacienteFuture.get();
        TecnicoDTO tecnico = tecnicoFuture.get();

        System.out.println("Paciente encontrado: " + paciente.getNome());
        System.out.println("Técnico encontrado: " + tecnico.getNome());

        Formulario formulario = formularioRepository.findById(avaliacaoDTO.getFormularioId())
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado"));
        System.out.println("Formulário encontrado com id: " + formulario.getId());

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setPaciente(paciente.getId());
        avaliacao.setPaciente_nome(paciente.getNome());
        avaliacao.setPaciente_idade_avaliacao(paciente.getIdade());
        avaliacao.setPaciente_imc_avaliacao(paciente.getImc());
        avaliacao.setTecnico(tecnico.getId());
        avaliacao.setTecnico_nome(tecnico.getNome());
        avaliacao.setFormulario(formulario);
        avaliacao.setPontuacaoTotal(avaliacaoDTO.getPontuacaoTotal());
        avaliacao.setDataCriacao(LocalDateTime.now());
        avaliacao.setDataAtualizacao(LocalDateTime.now());

        if (avaliacao.getRespostas() == null) {
            avaliacao.setRespostas(new ArrayList<>());
        }

        for (RespostaDTO respostaDTO : avaliacaoDTO.getRespostas()) {
            Pergunta pergunta = perguntaRepository.findById(respostaDTO.getPerguntaId())
                    .orElseThrow(() -> new RuntimeException("Pergunta não encontrada com ID: " + respostaDTO.getPerguntaId()));

            Resposta resposta = new Resposta(pergunta, avaliacao, respostaDTO.getValor());
            avaliacao.getRespostas().add(resposta);
        }

        System.out.println("Avaliacao:");
        System.out.println("id: " + avaliacao.getId());
        System.out.println("paciente: " + avaliacao.getPaciente());
        System.out.println("tecnico: " + avaliacao.getTecnico());
        System.out.println("formulario: " + avaliacao.getFormulario().getId());
        System.out.println("pontuacaoTotal: " + avaliacao.getPontuacaoTotal());
        System.out.println("dataCriacao: " + avaliacao.getDataCriacao());
        System.out.println("dataAtualizacao: " + avaliacao.getDataAtualizacao());
        System.out.println("respostas: " + avaliacao.getRespostas());

        for (Resposta resposta : avaliacao.getRespostas()) {
            System.out.println("Resposta:");
            System.out.println("id: " + resposta.getId());
            System.out.println("pergunta: " + resposta.getPergunta().getId());
            System.out.println("valor: " + resposta.getValor());
        }

        avaliacaoRepository.save(avaliacao);

        return ResponseEntity.ok(Map.of("message", "Avaliação salva com sucesso"));
    }
    
    @GetMapping("/respostas/paciente/{id}")
    public ResponseEntity<List<RespostaAvaliacaoPaciente>> getRespostasByPaciente(@PathVariable("id") Long pacienteId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByPaciente(pacienteId);
        
        List<RespostaAvaliacaoPaciente> avaliacaoResponses = avaliacoes.stream()
            .map(avaliacao -> {
                TecnicoDTO tecnico = avaliacao.getTecnicoDTO();
                PacienteDTO paciente = avaliacao.getPacienteDTO();
                Formulario formulario = avaliacao.getFormulario();
                
                List<PerguntaValorDTO> perguntaValorList = avaliacao.getRespostas().stream()
                    .map(resposta -> new PerguntaValorDTO(
                        resposta.getPergunta().getTexto(), 
                        resposta.getValor()
                    ))
                    .collect(Collectors.toList());

                return new RespostaAvaliacaoPaciente(
                    avaliacao.getId(),
                    paciente.getId(),
                    paciente.getNome(),
                    paciente.getIdade(),
                    paciente.getImc(),
                    tecnico.getId(), 
                    tecnico.getNome(), 
                    formulario.getTitulo(),
                    formulario.getDescricao(),
                    avaliacao.getPontuacaoTotal(),
                    avaliacao.getPontuacaoMaxima(),
                    avaliacao.getDataCriacao(),
                    avaliacao.getDataAtualizacao(),
                    perguntaValorList
                );
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(avaliacaoResponses);
    }

    @GetMapping("/respostas/tecnico/{id}")
    public ResponseEntity<List<RespostaAvaliacaoTecnico>> getRespostasByTecnico(@PathVariable("id") Long tecnicoId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByTecnico(tecnicoId);
        
        List<RespostaAvaliacaoTecnico> avaliacaoResponses = avaliacoes.stream()
            .map(avaliacao -> {
                PacienteDTO paciente = avaliacao.getPacienteDTO();
                
                List<PerguntaValorDTO> perguntaValorList = avaliacao.getRespostas().stream()
                    .map(resposta -> new PerguntaValorDTO(
                        resposta.getPergunta().getTexto(), 
                        resposta.getValor()
                    ))
                    .collect(Collectors.toList());

                return new RespostaAvaliacaoTecnico(
                    paciente.getId(), 
                    paciente.getNome(), 
                    perguntaValorList
                );
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(avaliacaoResponses);
    }

    @GetMapping("/avaliacao/{id}")
    public ResponseEntity<RespostaAvaliacaoPaciente> getAvaliacaoById(@PathVariable("id") Long avaliacaoId) {
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        TecnicoDTO tecnico = avaliacao.getTecnicoDTO();
        PacienteDTO paciente = avaliacao.getPacienteDTO();
        Formulario formulario = avaliacao.getFormulario();
        
        List<PerguntaValorDTO> perguntaValorList = avaliacao.getRespostas().stream()
            .map(resposta -> new PerguntaValorDTO(
                resposta.getPergunta().getTexto(), 
                resposta.getValor()
            ))
            .collect(Collectors.toList());

        RespostaAvaliacaoPaciente response = new RespostaAvaliacaoPaciente(
            avaliacao.getId(),
            paciente.getId(), 
            paciente.getNome(),
            paciente.getIdade(),
            paciente.getImc(),
            tecnico.getId(), 
            tecnico.getNome(), 
            formulario.getTitulo(),
            formulario.getDescricao(),
            avaliacao.getPontuacaoTotal(),
            avaliacao.getPontuacaoMaxima(),
            avaliacao.getDataCriacao(),
            avaliacao.getDataAtualizacao(),
            perguntaValorList
        );

        return ResponseEntity.ok(response);
    }
}
