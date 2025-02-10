package br.ufpr.tcc.MSForms.auth;

import br.ufpr.tcc.MSForms.models.Paciente;
import br.ufpr.tcc.MSForms.models.Tecnico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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
        LoginResponse response = authService.registrarTecnico(tecnico);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pacientes")
    public ResponseEntity<LoginResponse> registrarPaciente(@RequestBody Paciente paciente) {
        LoginResponse response = authService.registrarPaciente(paciente);
        return ResponseEntity.ok(response);
    }
}