package br.ufpr.tcc.MSForms.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpr.tcc.MSForms.models.Formulario;
import br.ufpr.tcc.MSForms.repositories.FormularioRepository;

@RestController
@RequestMapping("/api/formularios")
public class FormularioController {

    private final FormularioRepository formularioRepository;

    public FormularioController(FormularioRepository formularioRepository) {
        this.formularioRepository = formularioRepository;
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


}
