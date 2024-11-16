package br.ufpr.tcc.MSForms.rest;

import br.ufpr.tcc.MSForms.models.Paciente;
import br.ufpr.tcc.MSForms.models.Tecnico;
import br.ufpr.tcc.MSForms.repositories.PacienteRepository;
import br.ufpr.tcc.MSForms.repositories.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping("/tecnicos")
    public Tecnico createTecnico(@RequestBody Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }

    @PostMapping("/pacientes")
    public Paciente createPaciente(@RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @GetMapping("/tecnicos")
    public List<Tecnico> getAllTecnicos() {
        return tecnicoRepository.findAll();
    }

    @GetMapping("/pacientes")
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }
}
