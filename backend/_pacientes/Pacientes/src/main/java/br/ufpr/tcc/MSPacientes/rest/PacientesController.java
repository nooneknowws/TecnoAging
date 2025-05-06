package br.ufpr.tcc.MSPacientes.rest;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import br.ufpr.tcc.MSPacientes.models.Paciente;
import br.ufpr.tcc.MSPacientes.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PacientesController {


    @Autowired
    private PacienteRepository pacienteRepository;
	
    @PostMapping("/pacientes")
    @Transactional
    public ResponseEntity<?> createPaciente(@RequestBody Paciente paciente) {
        try {
            if(paciente.getId() != null) {
                Paciente existing = pacienteRepository.findById(paciente.getId())
                    .orElseThrow(() -> new EntityNotFoundException());
                BeanUtils.copyProperties(paciente, existing, "id", "version");
                return ResponseEntity.ok(pacienteRepository.save(existing));
            }
            return ResponseEntity.ok(pacienteRepository.save(paciente));
        } catch (ObjectOptimisticLockingFailureException e) {
            return ResponseEntity.status(409)
                .body("Conflict: Record was modified by another user");
        }
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
        
        Paciente updated = pacienteRepository.save(paciente);
        return ResponseEntity.ok(updated);
    }
    

}
