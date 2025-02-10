package br.ufpr.tcc.MSForms.auth;

import br.ufpr.tcc.MSForms.models.Paciente;
import br.ufpr.tcc.MSForms.models.Tecnico;
import br.ufpr.tcc.MSForms.repositories.PacienteRepository;
import br.ufpr.tcc.MSForms.repositories.TecnicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    private static final int ITERACOES = 10000;
    private static final int TAMANHO_CHAVE = 256;
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";

    public LoginResponse loginTecnico(LoginRequest loginRequest) {
        Optional<Tecnico> tecnico = tecnicoRepository.findByCpf(loginRequest.cpf());
        
        if (tecnico.isPresent() && checkPassword(loginRequest.senha(), tecnico.get().getSalt(), tecnico.get().getPasswordHash())) {
            return createSuccessResponse(tecnico.get(), "tecnico");
        }
        return createFailureResponse("Credenciais inválidas");
    }

    public LoginResponse loginPaciente(LoginRequest loginRequest) {
        Optional<Paciente> paciente = pacienteRepository.findByCpf(loginRequest.cpf());
        
        if (paciente.isPresent() && checkPassword(loginRequest.senha(), paciente.get().getSalt(), paciente.get().getPasswordHash())) {
            return createSuccessResponse(paciente.get(), "paciente");
        }
        return createFailureResponse("Credenciais inválidas");
    }

    public LoginResponse registrarTecnico(@RequestBody Tecnico tecnico) {
        String password = tecnico.getSenha();
        String[] saltAndHash = hashPassword(password);
        tecnico.setSalt(saltAndHash[0]);
        tecnico.setPasswordHash(saltAndHash[1]);
        Tecnico savedTecnico = tecnicoRepository.save(tecnico);
        return createSuccessResponse(savedTecnico, "tecnico");
    }

    public LoginResponse registrarPaciente(Paciente paciente) {
        String password = paciente.getSenha();
        String[] saltAndHash = hashPassword(password);
        paciente.setSalt(saltAndHash[0]);
        paciente.setPasswordHash(saltAndHash[1]);
        Paciente savedPaciente = pacienteRepository.save(paciente);
        return createSuccessResponse(savedPaciente, "paciente");
    }
    
    private boolean checkPassword(String senha, String salt, String hash) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            KeySpec spec = new PBEKeySpec(senha.toCharArray(), saltBytes, ITERACOES, TAMANHO_CHAVE);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITMO);
            byte[] hashCalculado = f.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hashCalculado).equals(hash);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar senha", e);
        }
    }

    private String[] hashPassword(String senha) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            KeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, ITERACOES, TAMANHO_CHAVE);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITMO);
            byte[] hash = f.generateSecret(spec).getEncoded();
            
            return new String[]{
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(hash)
            };
        } catch (Exception e) {
            throw new RuntimeException("Erro ao hashear senha", e);
        }
    }

    private LoginResponse createSuccessResponse(Object user, String tipo) {
        String token = generateToken(user, tipo);
        return new LoginResponse(true, user, tipo, token, "Login realizado com sucesso");
    }

    private LoginResponse createFailureResponse(String message) {
        return new LoginResponse(false, null, null, null, message);
    }

    private String generateToken(Object user, String tipo) {
        // Implementação real deve usar um método seguro de geração de token
        return "token-jogo-aqui";
    }

    private boolean matchesPassword(Object user, String providedPassword) {
        if (user instanceof Tecnico) {
            Tecnico tecnico = (Tecnico) user;
            return checkPassword(providedPassword, tecnico.getSalt(), tecnico.getPasswordHash());
        } else if (user instanceof Paciente) {
            Paciente paciente = (Paciente) user;
            return checkPassword(providedPassword, paciente.getSalt(), paciente.getPasswordHash());
        }
        return false;
    }
}