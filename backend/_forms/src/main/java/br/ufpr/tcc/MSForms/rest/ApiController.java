package br.ufpr.tcc.MSForms.rest;

import br.ufpr.tcc.MSForms.models.Paciente;
import br.ufpr.tcc.MSForms.models.Tecnico;
import br.ufpr.tcc.MSForms.repositories.PacienteRepository;
import br.ufpr.tcc.MSForms.repositories.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    
    @GetMapping("/pacientes/{id}")
    public Paciente getPacienteById(@PathVariable(name = "id") Long pacienteId) {
        return pacienteRepository.findById(pacienteId).get();
    }
    
    @PutMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable(name = "id") Long pacienteId, @RequestBody Paciente pacienteAtualizado) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
            .orElseThrow(() -> new RuntimeException("Paciente não encontrado com o ID: " + pacienteId));
        
        paciente.setNome(pacienteAtualizado.getNome());
        paciente.setSexo(pacienteAtualizado.getSexo());
        paciente.setIdade(pacienteAtualizado.getIdade());
        paciente.setPeso(pacienteAtualizado.getPeso());
        paciente.setAltura(pacienteAtualizado.getAltura());
        paciente.setImc(pacienteAtualizado.getImc());
        paciente.setSocioeconomico(pacienteAtualizado.getSocioeconomico());
        paciente.setEscolaridade(pacienteAtualizado.getEscolaridade());
        paciente.setEstadoCivil(pacienteAtualizado.getEstadoCivil());
        paciente.setNacionalidade(pacienteAtualizado.getNacionalidade());
        paciente.setMunicipioNasc(pacienteAtualizado.getMunicipioNasc());
        paciente.setUfNasc(pacienteAtualizado.getUfNasc());
        paciente.setCorRaca(pacienteAtualizado.getCorRaca());
        paciente.setRg(pacienteAtualizado.getRg());
        paciente.setDataExpedicao(pacienteAtualizado.getDataExpedicao());
        paciente.setOrgaoEmissor(pacienteAtualizado.getOrgaoEmissor());
        paciente.setUfEmissor(pacienteAtualizado.getUfEmissor());
        paciente.setEndereco(pacienteAtualizado.getEndereco());
        paciente.setDataNasc(pacienteAtualizado.getDataNasc());
        paciente.setCpf(pacienteAtualizado.getCpf());
        paciente.setTelefone(pacienteAtualizado.getTelefone());
        paciente.setContatos(pacienteAtualizado.getContatos());
        
        return ResponseEntity.ok(paciente);
    }
}
