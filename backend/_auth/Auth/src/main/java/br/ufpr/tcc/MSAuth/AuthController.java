package br.ufpr.tcc.MSAuth;

import br.ufpr.tcc.MSForms.models.Paciente;
import br.ufpr.tcc.MSForms.models.Tecnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/tecnicos/login")
    public ResponseEntity<LoginResponse> loginTecnico(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.loginTecnico(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pacientes/login")
    public ResponseEntity<LoginResponse> loginPaciente(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.loginPaciente(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tecnicos")
    public ResponseEntity<LoginResponse> registrarTecnico(@RequestBody Tecnico tecnico) {
        System.out.println("Recebido: " + tecnico.toString());
        // ou
        System.out.println("Recebido: " + tecnico);
        
        // Se você quiser logar os atributos da classe e da superclasse
        System.out.println("Atributos da classe Tecnico:");
        System.out.println("id: " + tecnico.getId());
        System.out.println("nome: " + tecnico.getNome());
        System.out.println("sexo: " + tecnico.getSexo());
        System.out.println("idade: " + tecnico.getIdade());
        System.out.println("endereco: " + tecnico.getEndereco());
        System.out.println("dataNasc: " + tecnico.getDataNasc());
        System.out.println("telefone: " + tecnico.getTelefone());
        System.out.println("matricula: " + tecnico.getMatricula());
        System.out.println("ativo: " + tecnico.isAtivo());
        
        System.out.println("Atributos da superclasse Pessoa:");
        System.out.println("id: " + tecnico.getId());
        System.out.println("cpf: " + tecnico.getCpf());
        System.out.println("nome: " + tecnico.getNome());
        System.out.println("sexo: " + tecnico.getSexo());
        System.out.println("idade: " + tecnico.getIdade());
        System.out.println("endereco: " + tecnico.getEndereco());
        System.out.println("dataNasc: " + tecnico.getDataNasc());
        System.out.println("telefone: " + tecnico.getTelefone());
        System.out.println("senha: " + tecnico.getSenha());
        System.out.println("salt: " + tecnico.getSalt());
        System.out.println("passwordHash: " + tecnico.getPasswordHash());
        
        LoginResponse response = authService.registrarTecnico(tecnico);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/pacientes")
    public ResponseEntity<LoginResponse> registrarPaciente(@RequestBody Paciente paciente) {
        LoginResponse response = authService.registrarPaciente(paciente);
        return ResponseEntity.ok(response);
    }
}