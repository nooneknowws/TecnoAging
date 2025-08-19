package br.ufpr.tcc.MSTecnicos.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufpr.tcc.MSTecnicos.exceptions.ResourceAlreadyExistsException;
import br.ufpr.tcc.MSTecnicos.exceptions.ResourceNotFoundException;

import br.ufpr.tcc.MSTecnicos.models.Tecnico;
import br.ufpr.tcc.MSTecnicos.repository.TecnicoRepository;
import br.ufpr.tcc.MSTecnicos.service.ImageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TecnicoController {

	@Autowired
	private TecnicoRepository tecnicoRepository;

	@Autowired
	private ImageService imageService;

	@PostMapping("/tecnicos")
	public ResponseEntity<Tecnico> createTecnico(@RequestBody Tecnico tecnico) {
		Optional<Tecnico> tecnicoExistente = tecnicoRepository.findByCpf(tecnico.getCpf());
		if (tecnicoExistente.isPresent()) {
			throw new ResourceAlreadyExistsException("Já existe um técnico cadastrado com o CPF: " + tecnico.getCpf());
		}

		Tecnico novoTecnico = tecnicoRepository.save(tecnico);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoTecnico);
	}

	@GetMapping("/tecnicos")
	public List<Tecnico> getAllTecnicos() {
		return tecnicoRepository.findAll();
	}

	@GetMapping("/tecnicos/{id}")
	public Tecnico getTecnicosByID(@PathVariable("id") Long tecnicoId) {
		return tecnicoRepository.findById(tecnicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Tecnico não encontrado com o ID: " + tecnicoId));
	}

	@PutMapping("/tecnicos/{id}")
	public ResponseEntity<Tecnico> updateTecnico(@PathVariable("id") Long tecnicoId,
			@RequestBody Tecnico tecnicoAtualizado) {

		Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
				.orElseThrow(() -> new RuntimeException("Tecnico não encontrado com o ID: " + tecnicoId));

		tecnico.setNome(tecnicoAtualizado.getNome());
		tecnico.setSexo(tecnicoAtualizado.getSexo());
		tecnico.setIdade(tecnicoAtualizado.getIdade());
		tecnico.setEndereco(tecnicoAtualizado.getEndereco());
		tecnico.setDataNasc(tecnicoAtualizado.getDataNasc());
		tecnico.setCpf(tecnicoAtualizado.getCpf());
		tecnico.setTelefone(tecnicoAtualizado.getTelefone());
		tecnico.setMatricula(tecnicoAtualizado.getMatricula());
		tecnico.setAtivo(tecnicoAtualizado.isAtivo());

		Tecnico updated = tecnicoRepository.save(tecnico);
		return ResponseEntity.ok(updated);
	}

	@PatchMapping("/tecnicos/{id}/desativar")
	public ResponseEntity<Void> deactivateTecnico(@PathVariable("id") Long tecnicoId) {
		Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Tecnico não encontrado com o ID: " + tecnicoId));
		tecnico.setAtivo(false);
		tecnicoRepository.save(tecnico);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/tecnicos/{id}/ativar")
	public ResponseEntity<Void> activateTecnico(@PathVariable("id") Long tecnicoId) {
		Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Tecnico não encontrado com o ID: " + tecnicoId));
		tecnico.setAtivo(true);
		tecnicoRepository.save(tecnico);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/tecnicos/{id}/foto")
	public ResponseEntity<?> uploadFotoTecnico(@PathVariable("id") Long tecnicoId, @RequestBody Map<String, String> request) {
		try {
			Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado"));

			String base64Image = request.get("image");
			byte[] compressedImage = imageService.compressImage(base64Image);
			
			tecnico.setFotoPerfil(compressedImage);
			tecnicoRepository.save(tecnico);

			return ResponseEntity.ok(Map.of("message", "Foto atualizada com sucesso"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

	@GetMapping("/tecnicos/{id}/foto")
	public ResponseEntity<?> getFotoTecnico(@PathVariable("id") Long tecnicoId) {
		try {
			Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
				.orElseThrow(() -> new ResourceNotFoundException("Técnico não encontrado"));

			byte[] fotoPerfil = tecnico.getFotoPerfil();
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