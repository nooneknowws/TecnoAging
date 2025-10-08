package br.ufpr.tcc.MSForms.service;

import br.ufpr.tcc.MSForms.models.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenericScoringService {

    /**
     * Calcula a pontuação total de uma avaliação baseado nas configurações genéricas
     */
    public void calculateAndUpdateScore(Avaliacao avaliacao) {
        Formulario formulario = avaliacao.getFormulario();

        if (!Boolean.TRUE.equals(formulario.getCalculaPontuacao())) {
            avaliacao.setPontuacaoTotal(0);
            avaliacao.setPontuacaoMaxima(0);
            return;
        }

        List<Resposta> respostas = avaliacao.getRespostas();

        // Agrupa respostas por etapa
        Map<Long, List<Resposta>> respostasPorEtapa = respostas.stream()
            .filter(r -> r.getPergunta() != null && r.getPergunta().getEtapa() != null)
            .collect(Collectors.groupingBy(r -> r.getPergunta().getEtapa().getId()));

        Map<Long, Integer> pontuacoesPorEtapa = new HashMap<>();
        int pontuacaoTotal = 0;
        int pontuacaoMaxima = 0;

        // Calcula pontuação de cada etapa
        for (Etapa etapa : formulario.getEtapas()) {
            List<Resposta> respostasEtapa = respostasPorEtapa.getOrDefault(etapa.getId(), List.of());

            int pontuacaoEtapa = calcularPontuacaoEtapa(etapa, respostasEtapa);
            int maxEtapa = calcularPontuacaoMaximaEtapa(etapa);

            pontuacoesPorEtapa.put(etapa.getId(), pontuacaoEtapa);
            pontuacaoTotal += pontuacaoEtapa;
            pontuacaoMaxima += maxEtapa;
        }

        // Aplica regra de cálculo final do formulário, se houver
        RegraCalculo regraFinal = formulario.getRegraCalculoFinal();
        if (regraFinal != null && regraFinal.getTipoCalculo() != null) {
            pontuacaoTotal = aplicarRegraCalculo(pontuacoesPorEtapa, regraFinal, formulario.getEtapas().size());
        }

        avaliacao.setPontuacaoTotal(pontuacaoTotal);
        avaliacao.setPontuacaoMaxima(pontuacaoMaxima);
    }

    /**
     * Calcula a pontuação de uma etapa
     */
    private int calcularPontuacaoEtapa(Etapa etapa, List<Resposta> respostas) {
        if (respostas.isEmpty()) {
            return 0;
        }

        // Calcula pontuação de cada pergunta
        int somaPerguntas = 0;
        for (Resposta resposta : respostas) {
            somaPerguntas += calcularPontuacaoPergunta(resposta);
        }

        // Aplica regra de cálculo da etapa, se houver
        RegraCalculo regraEtapa = etapa.getRegraCalculoEtapa();
        if (regraEtapa != null && regraEtapa.getTipoCalculo() != null) {
            Map<Long, Integer> pontuacoesPorPergunta = new HashMap<>();
            for (Resposta resposta : respostas) {
                pontuacoesPorPergunta.put(
                    resposta.getPergunta().getId(),
                    calcularPontuacaoPergunta(resposta)
                );
            }
            return aplicarRegraCalculo(pontuacoesPorPergunta, regraEtapa, respostas.size());
        }

        return somaPerguntas;
    }

    /**
     * Calcula a pontuação de uma pergunta baseado em sua configuração
     */
    private int calcularPontuacaoPergunta(Resposta resposta) {
        if (resposta == null || resposta.getValor() == null) {
            return 0;
        }

        Pergunta pergunta = resposta.getPergunta();
        ConfiguracaoPontuacao config = pergunta.getConfiguracaoPontuacao();

        if (config == null || config.getTipoPontuacao() == null) {
            return 0;
        }

        String tipoPontuacao = config.getTipoPontuacao();
        String valor = resposta.getValor();

        switch (tipoPontuacao) {
            case "VALOR_DIRETO":
                return calcularValorDireto(valor);

            case "MAPEAMENTO_DIRETO":
                return config.getMapeamentoPontos().getOrDefault(valor, 0);

            case "MAPEAMENTO_REVERSO":
                int pontosMaximos = config.getPontosMaximos() != null ? config.getPontosMaximos() : 4;
                int pontosBrutos = config.getMapeamentoPontos().getOrDefault(valor, 0);
                return pontosMaximos - pontosBrutos;

            case "FORMULA":
                return calcularFormula(valor, config.getFormula());

            default:
                return 0;
        }
    }

    /**
     * Calcula pontuação máxima de uma etapa
     */
    private int calcularPontuacaoMaximaEtapa(Etapa etapa) {
        int maxEtapa = 0;

        for (Pergunta pergunta : etapa.getPerguntas()) {
            ConfiguracaoPontuacao config = pergunta.getConfiguracaoPontuacao();

            if (config != null && config.getPontosMaximos() != null) {
                maxEtapa += config.getPontosMaximos();
            }
        }

        return maxEtapa;
    }

    /**
     * Aplica regra de cálculo (etapa ou formulário)
     */
    private int aplicarRegraCalculo(Map<Long, Integer> pontuacoes, RegraCalculo regra, int quantidade) {
        if (pontuacoes.isEmpty()) {
            return 0;
        }

        String tipoCalculo = regra.getTipoCalculo();

        switch (tipoCalculo) {
            case "SOMA_SIMPLES":
                return pontuacoes.values().stream().mapToInt(Integer::intValue).sum();

            case "MEDIA_SIMPLES":
                if (quantidade == 0) return 0;
                int soma = pontuacoes.values().stream().mapToInt(Integer::intValue).sum();
                return Math.round((float) soma / quantidade);

            case "MEDIA_PONDERADA":
                return calcularMediaPonderada(pontuacoes, regra.getPesos());

            case "SOMA_ETAPAS":
                return pontuacoes.values().stream().mapToInt(Integer::intValue).sum();

            case "MEDIA_AJUSTADA":
                // Média ajustada: (SUM / COUNT) * multiplicador
                // Extrai multiplicador da fórmula: "(SUM_PONTOS / COUNT_RESPONDIDAS) * 7"
                if (quantidade == 0) return 0;
                int somaAjustada = pontuacoes.values().stream().mapToInt(Integer::intValue).sum();
                float multiplicador = extrairMultiplicador(regra.getFormulaCustom());
                return Math.round(((float) somaAjustada / quantidade) * multiplicador);

            case "FORMULA_CUSTOM":
                // TODO: implementar avaliador de fórmulas customizadas
                return pontuacoes.values().stream().mapToInt(Integer::intValue).sum();

            default:
                return pontuacoes.values().stream().mapToInt(Integer::intValue).sum();
        }
    }

    /**
     * Calcula média ponderada
     */
    private int calcularMediaPonderada(Map<Long, Integer> pontuacoes, Map<Long, Double> pesos) {
        if (pesos == null || pesos.isEmpty()) {
            // Sem pesos, retorna média simples
            double media = pontuacoes.values().stream().mapToInt(Integer::intValue).average().orElse(0);
            return (int) Math.round(media);
        }

        double somaTotal = 0.0;
        double somaPesos = 0.0;

        for (Map.Entry<Long, Integer> entry : pontuacoes.entrySet()) {
            Long id = entry.getKey();
            Integer pontuacao = entry.getValue();
            Double peso = pesos.getOrDefault(id, 1.0);

            somaTotal += pontuacao * peso;
            somaPesos += peso;
        }

        if (somaPesos == 0) {
            return 0;
        }

        return (int) Math.round(somaTotal / somaPesos);
    }

    /**
     * Calcula valor direto (converte string para int)
     */
    private int calcularValorDireto(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Calcula usando fórmula
     */
    private int calcularFormula(String valor, String formula) {
        if (formula == null || formula.isEmpty()) {
            return 0;
        }

        // Suporte para HH:MM para minutos
        if ("HH:MM_para_minutos".equals(formula)) {
            return converterHoraParaMinutos(valor);
        }

        // TODO: Expandir para outras fórmulas usando biblioteca de avaliação de expressões
        return 0;
    }

    /**
     * Converte hora HH:MM para minutos
     */
    private int converterHoraParaMinutos(String valor) {
        if (valor == null || !valor.matches("\\d+:\\d+")) {
            return 0;
        }

        String[] partes = valor.split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);

        return (horas * 60) + minutos;
    }

    /**
     * Extrai multiplicador de fórmulas do tipo: "(SUM_PONTOS / COUNT_RESPONDIDAS) * N"
     */
    private float extrairMultiplicador(String formula) {
        if (formula == null || formula.isEmpty()) {
            return 1.0f;
        }

        // Procura por padrão: "* N" ou "*N"
        int asteriscoIndex = formula.lastIndexOf('*');
        if (asteriscoIndex == -1) {
            return 1.0f;
        }

        String numeroStr = formula.substring(asteriscoIndex + 1).trim();
        try {
            return Float.parseFloat(numeroStr);
        } catch (NumberFormatException e) {
            return 1.0f;
        }
    }
}