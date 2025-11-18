package br.ufpr.tcc.MSForms.service;

import br.ufpr.tcc.MSForms.models.Avaliacao;
import br.ufpr.tcc.MSForms.models.Resposta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service para calcular pontuação do IPAQ (International Physical Activity Questionnaire)
 *
 * O IPAQ calcula MET-minutos/semana e classifica o nível de atividade física em:
 * - Nível 1: Baixo (Sedentário)
 * - Nível 2: Moderado
 * - Nível 3: Alto (Vigoroso)
 */
@Service
public class IPAQScoringService {

    // Valores MET para cada tipo de atividade
    private static final double MET_CAMINHADA = 3.3;
    private static final double MET_MODERADA = 4.0;
    private static final double MET_VIGOROSA = 8.0;

    // IDs das perguntas (devem corresponder aos IDs no banco)
    // Caminhada
    private static final long PERGUNTA_CAMINHADA_TEMPO = 107;
    private static final long PERGUNTA_CAMINHADA_DIAS = 108;

    // Atividade Moderada
    private static final long PERGUNTA_MODERADA_TEMPO = 48;
    private static final long PERGUNTA_MODERADA_DIAS = 49;

    // Atividade Vigorosa
    private static final long PERGUNTA_VIGOROSA_TEMPO = 50;
    private static final long PERGUNTA_VIGOROSA_DIAS = 51;

    /**
     * Calcula a pontuação IPAQ para uma avaliação
     */
    public void calculateIPAQScore(Avaliacao avaliacao) {
        List<Resposta> respostas = avaliacao.getRespostas();

        // Criar mapa de respostas por ID da pergunta
        Map<Long, String> respostaMap = respostas.stream()
            .filter(r -> r.getPergunta() != null && r.getValor() != null)
            .collect(Collectors.toMap(
                r -> r.getPergunta().getId(),
                Resposta::getValor,
                (v1, v2) -> v1 // Em caso de duplicata, mantém o primeiro
            ));

        // Extrair valores
        int caminhadaMinutos = extractMinutosPorDia(respostaMap.get(PERGUNTA_CAMINHADA_TEMPO));
        int caminhadaDias = extractDias(respostaMap.get(PERGUNTA_CAMINHADA_DIAS));

        int moderadaMinutos = extractMinutosPorDia(respostaMap.get(PERGUNTA_MODERADA_TEMPO));
        int moderadaDias = extractDias(respostaMap.get(PERGUNTA_MODERADA_DIAS));

        int vigorosaMinutos = extractMinutosPorDia(respostaMap.get(PERGUNTA_VIGOROSA_TEMPO));
        int vigorosaDias = extractDias(respostaMap.get(PERGUNTA_VIGOROSA_DIAS));

        // Calcular MET-min/semana para cada atividade
        double metCaminhada = MET_CAMINHADA * caminhadaMinutos * caminhadaDias;
        double metModerada = MET_MODERADA * moderadaMinutos * moderadaDias;
        double metVigorosa = MET_VIGOROSA * vigorosaMinutos * vigorosaDias;

        // Total MET-min/semana
        double totalMET = metCaminhada + metModerada + metVigorosa;

        // Classificar nível de atividade
        int nivelAtividade = classificarNivelAtividade(
            caminhadaMinutos, caminhadaDias,
            moderadaMinutos, moderadaDias,
            vigorosaMinutos, vigorosaDias,
            totalMET
        );

        // Definir pontuação (0-3: onde 3 = Alto, 2 = Moderado, 1 = Baixo)
        avaliacao.setPontuacaoTotal((int) Math.round(totalMET));
        avaliacao.setPontuacaoMaxima(3000); // Máximo para nível Alto

        // Armazenar classificação como metadado (opcional - pode criar campo específico)
        System.out.println("=== IPAQ SCORING ===");
        System.out.println("Caminhada: " + metCaminhada + " MET-min/semana");
        System.out.println("Moderada: " + metModerada + " MET-min/semana");
        System.out.println("Vigorosa: " + metVigorosa + " MET-min/semana");
        System.out.println("Total MET: " + totalMET + " MET-min/semana");
        System.out.println("Nível de Atividade: " + nivelAtividade + " (" + getNivelDescricao(nivelAtividade) + ")");
    }

    /**
     * Classifica o nível de atividade física segundo critérios do IPAQ
     */
    private int classificarNivelAtividade(
        int caminhadaMinutos, int caminhadaDias,
        int moderadaMinutos, int moderadaDias,
        int vigorosaMinutos, int vigorosaDias,
        double totalMET
    ) {
        // Nível 3: Alto (Vigoroso)
        // Critério A: Atividade vigorosa >= 3 dias E >= 1500 MET-min/semana
        if (vigorosaDias >= 3 && totalMET >= 1500) {
            return 3;
        }

        // Critério B: 7 dias de qualquer combinação E >= 3000 MET-min/semana
        int totalDias = caminhadaDias + moderadaDias + vigorosaDias;
        if (totalDias >= 7 && totalMET >= 3000) {
            return 3;
        }

        // Nível 2: Moderado
        // Critério A: >= 3 dias de atividade vigorosa com >= 20 min/dia
        if (vigorosaDias >= 3 && vigorosaMinutos >= 20) {
            return 2;
        }

        // Critério B: >= 5 dias de atividade moderada ou caminhada com >= 30 min/dia
        int diasModeradosOuCaminhada = Math.max(moderadaDias, caminhadaDias);
        int minutosModeradosOuCaminhada = Math.max(moderadaMinutos, caminhadaMinutos);
        if (diasModeradosOuCaminhada >= 5 && minutosModeradosOuCaminhada >= 30) {
            return 2;
        }

        // Critério C: >= 5 dias de qualquer combinação E >= 600 MET-min/semana
        if (totalDias >= 5 && totalMET >= 600) {
            return 2;
        }

        // Nível 1: Baixo (Sedentário)
        // Não atinge critérios dos níveis 2 ou 3
        return 1;
    }

    /**
     * Extrai minutos por dia de uma string no formato HH:MM
     */
    private int extractMinutosPorDia(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return 0;
        }

        try {
            // Formato esperado: "HH:MM" ou "H:MM"
            if (valor.contains(":")) {
                String[] partes = valor.split(":");
                int horas = Integer.parseInt(partes[0].trim());
                int minutos = Integer.parseInt(partes[1].trim());
                return (horas * 60) + minutos;
            }

            // Se for só número, assume que são minutos
            return Integer.parseInt(valor.trim());
        } catch (Exception e) {
            System.err.println("Erro ao extrair minutos de: " + valor);
            return 0;
        }
    }

    /**
     * Extrai número de dias por semana
     */
    private int extractDias(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return 0;
        }

        try {
            int dias = Integer.parseInt(valor.trim());
            // Validar entre 0 e 7
            return Math.max(0, Math.min(7, dias));
        } catch (Exception e) {
            System.err.println("Erro ao extrair dias de: " + valor);
            return 0;
        }
    }

    /**
     * Retorna descrição do nível de atividade
     */
    private String getNivelDescricao(int nivel) {
        switch (nivel) {
            case 3: return "Alto (Vigoroso)";
            case 2: return "Moderado";
            case 1: return "Baixo (Sedentário)";
            default: return "Desconhecido";
        }
    }
}
