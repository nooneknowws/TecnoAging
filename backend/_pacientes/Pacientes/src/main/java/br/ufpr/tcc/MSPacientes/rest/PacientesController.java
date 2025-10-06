package br.ufpr.tcc.MSPacientes.rest;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import br.ufpr.tcc.MSPacientes.models.Paciente;
import br.ufpr.tcc.MSPacientes.repository.PacienteRepository;
import br.ufpr.tcc.MSPacientes.service.ImageService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PacientesController {


    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ImageService imageService;
	
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
        
        // Manter foto de perfil existente se não for fornecida nova foto
        if (pacienteAtualizado.getFotoPerfil() != null) {
            paciente.setFotoPerfil(pacienteAtualizado.getFotoPerfil());
        }
        
        Paciente updated = pacienteRepository.save(paciente);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/pacientes/{id}/foto")
    public ResponseEntity<?> uploadFotoPaciente(@PathVariable(name = "id") Long pacienteId, @RequestBody Map<String, String> request) {
        try {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

            String base64Image = request.get("image");
            byte[] compressedImage = imageService.compressImage(base64Image);
            
            paciente.setFotoPerfil(compressedImage);
            pacienteRepository.save(paciente);

            return ResponseEntity.ok(Map.of("message", "Foto atualizada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/pacientes/{id}/foto")
    public ResponseEntity<?> getFotoPaciente(@PathVariable(name = "id") Long pacienteId) {
        try {
            Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

            byte[] fotoPerfil = paciente.getFotoPerfil();
            if (fotoPerfil == null) {
                return ResponseEntity.notFound().build();
            }

            String base64Image = imageService.convertToBase64(fotoPerfil);
            return ResponseEntity.ok(Map.of("image", base64Image));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
