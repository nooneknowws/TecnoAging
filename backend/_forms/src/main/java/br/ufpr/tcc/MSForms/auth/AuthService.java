package br.ufpr.tcc.MSForms.auth;

import br.ufpr.tcc.MSForms.models.Paciente;
import br.ufpr.tcc.MSForms.models.Pessoa;
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
import java.util.Arrays;
import java.util.Base64;

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
        return tecnicoRepository.findByCpf(loginRequest.cpf())
                .map(tecnico -> {
                    if (checkPassword(loginRequest.senha(), tecnico.getSalt(), tecnico.getPasswordHash())) {
                        Pessoa pessoa = new Pessoa(
                                tecnico.getId(),
                                tecnico.getCpf(),
                                tecnico.getNome(),
                                tecnico.getSexo(),
                                tecnico.getIdade(),
                                tecnico.getEndereco(),
                                tecnico.getDataNasc(),
                                tecnico.getTelefone()
                        );
                        return createSuccessResponse(pessoa, "tecnico");
                    } else {
                        return createFailureResponse("Credenciais inválidas");
                    }
                })
                .orElse(createFailureResponse("Credenciais inválidas"));
    }

    public LoginResponse loginPaciente(LoginRequest loginRequest) {
        return pacienteRepository.findByCpf(loginRequest.cpf())
                .map(paciente -> {
                    if (checkPassword(loginRequest.senha(), paciente.getSalt(), paciente.getPasswordHash())) {
                        Pessoa pessoa = new Pessoa(
                                paciente.getId(),
                                paciente.getCpf(),
                                paciente.getNome(),
                                paciente.getSexo(),
                                paciente.getIdade(),
                                paciente.getEndereco(),
                                paciente.getDataNasc(),
                                paciente.getTelefone()
                        );
                        return createSuccessResponse(pessoa, "paciente");
                    } else {
                        return createFailureResponse("Credenciais inválidas");
                    }
                })
                .orElse(createFailureResponse("Credenciais inválidas"));
    }
    
    public LoginResponse registrarTecnico(@RequestBody Pessoa tecnico) {
        return registrarPessoa(tecnico, "tecnico");
    }

    public LoginResponse registrarPaciente(@RequestBody Pessoa paciente) {
        return registrarPessoa(paciente, "paciente");
    }

    private LoginResponse registrarPessoa(Pessoa pessoa, String tipo) {
        String password = pessoa.getSenha();
        String[] saltAndHash = hashPassword(password);
        pessoa.setSalt(saltAndHash[0]);
        pessoa.setPasswordHash(saltAndHash[1]);

        if (pessoa instanceof Tecnico) {
            return createSuccessResponse(tecnicoRepository.save((Tecnico) pessoa), tipo);
        } else if (pessoa instanceof Paciente) {
            return createSuccessResponse(pacienteRepository.save((Paciente) pessoa), tipo);
        }
        else return createFailureResponse("Erro ao registrar");
    }
    
    private boolean checkPassword(String senha, String salt, String hash) {
        try {
            System.out.println("Iniciando verificação de senha...");
            System.out.println("Senha: " + senha);
            System.out.println("Salt: " + salt);
            System.out.println("Hash: " + hash);

            byte[] saltBytes = Base64.getDecoder().decode(salt);
            System.out.println("Salt decodificado: " + Arrays.toString(saltBytes));

            KeySpec spec = new PBEKeySpec(senha.toCharArray(), saltBytes, ITERACOES, TAMANHO_CHAVE);
            System.out.println("KeySpec criado: " + spec);

            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITMO);
            System.out.println("SecretKeyFactory instanciada: " + f.getAlgorithm());

            byte[] hashCalculado = f.generateSecret(spec).getEncoded();
            System.out.println("Hash calculado: " + Arrays.toString(hashCalculado));

            String hashCalculadoBase64 = Base64.getEncoder().encodeToString(hashCalculado);
            System.out.println("Hash calculado em Base64: " + hashCalculadoBase64);

            boolean resultado = hashCalculadoBase64.equals(hash);
            System.out.println("Resultado da verificação: " + resultado);

            return resultado;
        } catch (Exception e) {
            System.out.println("Erro ao verificar senha: " + e.getMessage());
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

    private LoginResponse createSuccessResponse(Pessoa user, String tipo) {
        String token = generateToken(user, tipo);
        return new LoginResponse(true, user.getId(), user, tipo, token, "Login realizado com sucesso");
    }

    private LoginResponse createFailureResponse(String message) {
        return new LoginResponse(false, null, null, null, null, message);
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