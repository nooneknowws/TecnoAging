package br.ufpr.tcc.MSForms.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpr.tcc.MSForms.models.*;
import br.ufpr.tcc.MSForms.models.dto.*;
import br.ufpr.tcc.MSForms.repositories.FormularioRepository;

@RestController
@RequestMapping("/api/formularios")
public class FormularioController {

    private final FormularioRepository formularioRepository;
    private final br.ufpr.tcc.MSForms.repositories.AvaliacaoRepository avaliacaoRepository;

    public FormularioController(FormularioRepository formularioRepository,
                               br.ufpr.tcc.MSForms.repositories.AvaliacaoRepository avaliacaoRepository) {
        this.formularioRepository = formularioRepository;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @PostMapping
    public ResponseEntity<Formulario> criarFormulario(@RequestBody Formulario formulario) {
        if (formulario.getEtapas() != null) {
            formulario.getEtapas().forEach(etapa -> {
                etapa.setFormulario(formulario);
                if (etapa.getPerguntas() != null) {
                    etapa.getPerguntas().forEach(pergunta -> pergunta.setEtapa(etapa));
                }
            });
        }

        Formulario salvo = formularioRepository.save(formulario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
    
    @GetMapping("/")
    public List<Formulario> getTodosFormularios() {
        return formularioRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Formulario> buscarFormularioPorId(@PathVariable("id") Long id) {
        Optional<Formulario> formulario = formularioRepository.findById(id);

        if (formulario.isPresent()) {
            return ResponseEntity.ok(formulario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Formulario> criarFormularioCadastro(@RequestBody FormularioCadastroDTO dto) {
        Formulario formulario = new Formulario();
        formulario.setTipo(dto.getTipo());
        formulario.setTitulo(dto.getTitulo());
        formulario.setDescricao(dto.getDescricao());
        formulario.setCalculaPontuacao(dto.getCalculaPontuacao());

        if (dto.getRegraCalculoFinal() != null) {
            formulario.setRegraCalculoFinal(mapRegraCalculoDTO(dto.getRegraCalculoFinal()));
        }

        if (dto.getEtapas() != null) {
            List<Etapa> etapas = dto.getEtapas().stream().map(etapaDTO -> {
                Etapa etapa = new Etapa();
                etapa.setTitulo(etapaDTO.getTitulo());
                etapa.setDescricao(etapaDTO.getDescricao());
                etapa.setFormulario(formulario);

                if (etapaDTO.getRegraCalculoEtapa() != null) {
                    etapa.setRegraCalculoEtapa(mapRegraCalculoDTO(etapaDTO.getRegraCalculoEtapa()));
                }

                if (etapaDTO.getPerguntas() != null) {
                    List<Pergunta> perguntas = etapaDTO.getPerguntas().stream().map(perguntaDTO -> {
                        Pergunta pergunta = new Pergunta();
                        pergunta.setTexto(perguntaDTO.getTexto());
                        pergunta.setTipo(perguntaDTO.getTipo());
                        pergunta.setOpcoes(perguntaDTO.getOpcoes());
                        pergunta.setValidacao(perguntaDTO.getValidacao());
                        pergunta.setEtapa(etapa);

                        if (perguntaDTO.getConfiguracaoPontuacao() != null) {
                            pergunta.setConfiguracaoPontuacao(mapConfiguracaoPontuacaoDTO(perguntaDTO.getConfiguracaoPontuacao()));
                        }

                        if (perguntaDTO.getMetadadosCampo() != null) {
                            pergunta.setMetadadosCampo(mapMetadadosCampoDTO(perguntaDTO.getMetadadosCampo()));
                        }

                        return pergunta;
                    }).toList();
                    etapa.setPerguntas(perguntas);
                }

                return etapa;
            }).toList();
            formulario.setEtapas(etapas);
        }

        Formulario salvo = formularioRepository.save(formulario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarFormulario(@PathVariable Long id, @RequestBody FormularioCadastroDTO dto) {
        Optional<Formulario> formularioExistente = formularioRepository.findById(id);

        if (!formularioExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Verificar se formulário possui avaliações
        boolean possuiAvaliacoes = avaliacaoRepository.existsByFormularioId(id);

        if (possuiAvaliacoes) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"error\": \"Não é possível alterar a estrutura de pontuação de um formulário que já possui avaliações cadastradas.\"}");
        }

        // Se não houver avaliações, permitir alteração completa
        Formulario formulario = formularioExistente.get();
        formulario.setTipo(dto.getTipo());
        formulario.setTitulo(dto.getTitulo());
        formulario.setDescricao(dto.getDescricao());
        formulario.setCalculaPontuacao(dto.getCalculaPontuacao());

        if (dto.getRegraCalculoFinal() != null) {
            formulario.setRegraCalculoFinal(mapRegraCalculoDTO(dto.getRegraCalculoFinal()));
        }

        // Limpar etapas antigas e adicionar novas
        formulario.getEtapas().clear();

        if (dto.getEtapas() != null) {
            List<Etapa> etapas = dto.getEtapas().stream().map(etapaDTO -> {
                Etapa etapa = new Etapa();
                etapa.setTitulo(etapaDTO.getTitulo());
                etapa.setDescricao(etapaDTO.getDescricao());
                etapa.setFormulario(formulario);

                if (etapaDTO.getRegraCalculoEtapa() != null) {
                    etapa.setRegraCalculoEtapa(mapRegraCalculoDTO(etapaDTO.getRegraCalculoEtapa()));
                }

                if (etapaDTO.getPerguntas() != null) {
                    List<Pergunta> perguntas = etapaDTO.getPerguntas().stream().map(perguntaDTO -> {
                        Pergunta pergunta = new Pergunta();
                        pergunta.setTexto(perguntaDTO.getTexto());
                        pergunta.setTipo(perguntaDTO.getTipo());
                        pergunta.setOpcoes(perguntaDTO.getOpcoes());
                        pergunta.setValidacao(perguntaDTO.getValidacao());
                        pergunta.setEtapa(etapa);

                        if (perguntaDTO.getConfiguracaoPontuacao() != null) {
                            pergunta.setConfiguracaoPontuacao(mapConfiguracaoPontuacaoDTO(perguntaDTO.getConfiguracaoPontuacao()));
                        }

                        if (perguntaDTO.getMetadadosCampo() != null) {
                            pergunta.setMetadadosCampo(mapMetadadosCampoDTO(perguntaDTO.getMetadadosCampo()));
                        }

                        return pergunta;
                    }).toList();
                    etapa.setPerguntas(perguntas);
                }

                return etapa;
            }).toList();
            formulario.getEtapas().addAll(etapas);
        }

        Formulario atualizado = formularioRepository.save(formulario);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFormulario(@PathVariable Long id) {
        if (!formularioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        formularioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private RegraCalculo mapRegraCalculoDTO(RegraCalculoDTO dto) {
        RegraCalculo regra = new RegraCalculo();
        regra.setTipoCalculo(dto.getTipoCalculo());
        regra.setFormulaCustom(dto.getFormulaCustom());
        regra.setPesos(dto.getPesos());
        return regra;
    }

    private ConfiguracaoPontuacao mapConfiguracaoPontuacaoDTO(ConfiguracaoPontuacaoDTO dto) {
        ConfiguracaoPontuacao config = new ConfiguracaoPontuacao();
        config.setTipoPontuacao(dto.getTipoPontuacao());
        config.setMapeamentoPontos(dto.getMapeamentoPontos());
        config.setFormula(dto.getFormula());
        config.setPontosMinimos(dto.getPontosMinimos());
        config.setPontosMaximos(dto.getPontosMaximos());
        return config;
    }

    private MetadadosCampo mapMetadadosCampoDTO(MetadadosCampoDTO dto) {
        MetadadosCampo meta = new MetadadosCampo();
        meta.setSubTipo(dto.getSubTipo());
        meta.setPlaceholder(dto.getPlaceholder());
        meta.setUnidade(dto.getUnidade());
        meta.setMascara(dto.getMascara());
        meta.setMultiplaEscolha(dto.getMultiplaEscolha());
        meta.setMinOpcoes(dto.getMinOpcoes());
        meta.setMaxOpcoes(dto.getMaxOpcoes());
        return meta;
    }

}
