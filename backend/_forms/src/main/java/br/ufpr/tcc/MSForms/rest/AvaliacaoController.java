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
import br.ufpr.tcc.MSForms.repositories.PerguntaRepository;
import br.ufpr.tcc.MSForms.service.AvaliacaoService;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {


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
        System.out.println("AvaliacaoDTO:");
        System.out.println("pacienteId: " + avaliacaoDTO.getPacienteId());
        System.out.println("tecnicoId: " + avaliacaoDTO.getTecnicoId());
        System.out.println("formularioId: " + avaliacaoDTO.getFormularioId());
        System.out.println("pontuacaoTotal: " + avaliacaoDTO.getPontuacaoTotal());
        
        // Verificar e logar melhor as respostas
        if (avaliacaoDTO.getRespostas() != null) {
            System.out.println("Número de respostas: " + avaliacaoDTO.getRespostas().size());
            for (int i = 0; i < avaliacaoDTO.getRespostas().size(); i++) {
                RespostaDTO resp = avaliacaoDTO.getRespostas().get(i);
                System.out.println("Resposta #" + i + ": perguntaId=" + resp.getPerguntaId() + ", valor=" + resp.getValor());
                
                // Verificar se o ID da pergunta é nulo
                if (resp.getPerguntaId() == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "ID da pergunta não pode ser nulo na resposta #" + i));
                }
            }
        }
        // chamada via RABBITMQ para encontrar o paciente trazer NOME, ID, IDADE E IMC
        PacienteDTO paciente = pacienteRepository.findById(avaliacaoDTO.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));
        System.out.println("Paciente encontrado com id: " + paciente.getId());
        // chamada via RABBITMQ para encontrar o tecnico trazer NOME e ID apenas
        TecnicoDTO tecnico = tecnicoRepository.findById(avaliacaoDTO.getTecnicoId())
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
        System.out.println("Técnico encontrado com id: " + tecnico.getId());

        Formulario formulario = formularioRepository.findById(avaliacaoDTO.getFormularioId())
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado"));
        System.out.println("Formulário encontrado com id: " + formulario.getId());

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
                    .orElseThrow(() -> new RuntimeException("Pergunta não encontrada com ID: " + respostaDTO.getPerguntaId()));

            Resposta resposta = new Resposta(pergunta, avaliacao, respostaDTO.getValor());
            avaliacao.getRespostas().add(resposta);
        }

        System.out.println("Avaliacao:");
        System.out.println("id: " + avaliacao.getId());
        System.out.println("paciente: " + avaliacao.getPaciente().getId());
        System.out.println("tecnico: " + avaliacao.getTecnico().getId());
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
        
        List<Avaliacao> avaliacoes = avaliacaoService.findAllByPacienteId(pacienteId);
        
        List<RespostaAvaliacaoPaciente> avaliacaoResponses = new ArrayList<>();

        for (Avaliacao avaliacao : avaliacoes) {
            TecnicoDTO tecnico = avaliacao.getTecnico(); // ID tecnico
            PacienteDTO paciente = avaliacao.getPaciente(); // ID paciente
            Formulario formulario = avaliacao.getFormulario();
            List<Resposta> respostas = avaliacao.getRespostas(); 
            List<PerguntaValorDTO> perguntaValorList = respostas.stream()
                .map(resposta -> new PerguntaValorDTO(resposta.getPergunta().getTexto(), resposta.getValor()))
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

            avaliacaoResponses.add(response);
        }

        return ResponseEntity.ok(avaliacaoResponses);
    }    
    @GetMapping("/respostas/tecnico/{id}")
    public ResponseEntity<List<RespostaAvaliacaoTecnico>> getRespostasByTecnico(@PathVariable("id") Long tecnicoId) {
        
        List<Avaliacao> avaliacoes = avaliacaoService.findAllByTecnicoId(tecnicoId);
        
        List<RespostaAvaliacaoTecnico> avaliacaoResponses = new ArrayList<>();

        for (Avaliacao avaliacao : avaliacoes) {
            PacienteDTO paciente = avaliacao.getPaciente();
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

    @GetMapping("/avaliacao/{id}")
    public ResponseEntity<RespostaAvaliacaoPaciente> getAvaliacaoById(@PathVariable("id") Long avaliacaoId) {
        
        Avaliacao avaliacao = avaliacaoRepository.findById(avaliacaoId)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        TecnicoDTO tecnico = avaliacao.getTecnico();
        PacienteDTO paciente = avaliacao.getPaciente();
        Formulario formulario = avaliacao.getFormulario();
        List<Resposta> respostas = avaliacao.getRespostas(); 
        List<PerguntaValorDTO> perguntaValorList = respostas.stream()
            .map(resposta -> new PerguntaValorDTO(resposta.getPergunta().getTexto(), resposta.getValor()))
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
