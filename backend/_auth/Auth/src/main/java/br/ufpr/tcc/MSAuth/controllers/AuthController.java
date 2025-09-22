package br.ufpr.tcc.MSAuth.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.ufpr.tcc.MSAuth.dto.AuthDTO;
import br.ufpr.tcc.MSAuth.dto.ResetPasswordDTO;
import br.ufpr.tcc.MSAuth.models.AuthenticatedUser;
import br.ufpr.tcc.MSAuth.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            AuthenticatedUser user = authService.login(authDTO);
            response.put("token", user.getToken());
            response.put("ID", user.getUserId());
            response.put("Nome", user.getName());
            response.put("Perfil", user.getPerfil());
            response.put("message", "Authentication successful");
            response.put("success", user.isSuccess());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("error", "Authentication failed");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            
        } catch (Exception e) {
            response.put("error", "Internal server error");
            response.put("details", "Please try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String token = extractToken(request);
            if (token == null) {
                response.put("error", "Missing authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            boolean loggedOut = authService.logout(token);
            
            if (loggedOut) {
                response.put("message", "Logout successful");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            response.put("error", "Logout failed");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/verify-jwt")
    public ResponseEntity<Map<String, Object>> verifyJwt(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
        	
            String token = extractToken(request);
            logger.info("Verification request received for token: {}", token);
            
            if (token == null) {
                response.put("valid", false);
                response.put("message", "Authorization header missing");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            Map<String, Object> verificationResult = authService.verifyJwtAndExtractData(token);
            
            if ((Boolean) verificationResult.get("valid")) {
                response.put("valid", true);
                response.put("message", "Token is valid");
                response.put("userId", verificationResult.get("userId"));
                response.put("username", verificationResult.get("username"));
                response.put("microservice", verificationResult.get("microservice"));
                return ResponseEntity.ok(response);
            } else {
                response.put("valid", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Token verification failed");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/enviar-codigo")
    public ResponseEntity<Map<String, Object>> enviarCodigo(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String cpf = request.get("cpf");

            if (cpf == null || cpf.trim().isEmpty()) {
                response.put("error", "CPF é obrigatório");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Always return 111111 without CPF validation for development
            response.put("message", "Código enviado com sucesso");
            response.put("codigo", "111111");
            response.put("telefone", "***.***.***-**"); // Mock masked phone

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("error", "Erro interno do servidor");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ResetPasswordDTO resetDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (resetDTO.getCpf() == null || resetDTO.getCpf().trim().isEmpty()) {
                response.put("error", "CPF é obrigatório");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (resetDTO.getCodigo() == null || !resetDTO.getCodigo().equals("111111")) {
                response.put("error", "Código inválido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (resetDTO.getNovaSenha() == null || resetDTO.getNovaSenha().trim().isEmpty()) {
                response.put("error", "Nova senha é obrigatória");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (!resetDTO.getNovaSenha().equals(resetDTO.getConfirmarSenha())) {
                response.put("error", "Senhas não coincidem");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            boolean passwordUpdated = authService.resetPassword(resetDTO.getCpf(), resetDTO.getNovaSenha());

            if (passwordUpdated) {
                response.put("message", "Senha alterada com sucesso");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Não foi possível alterar a senha. Verifique se o CPF está correto.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            response.put("error", "Erro interno do servidor");
            response.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}