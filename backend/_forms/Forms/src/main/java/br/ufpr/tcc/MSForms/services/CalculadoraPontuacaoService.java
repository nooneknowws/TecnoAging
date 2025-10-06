package br.ufpr.tcc.MSForms.services;

import br.ufpr.tcc.MSForms.models.ConfiguracaoPontuacao;
import br.ufpr.tcc.MSForms.models.RegraCalculo;
import br.ufpr.tcc.MSForms.models.Resposta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CalculadoraPontuacaoService {

    public int calcularPontuacaoPergunta(Resposta resposta) {
        ConfiguracaoPontuacao config = resposta.getPergunta().getConfiguracaoPontuacao();
        if (config == null || config.getTipoPontuacao() == null) {
            return 0;
        }

        switch (config.getTipoPontuacao()) {
            case "valor_direto":
                return calcularValorDireto(resposta.getValor());
            case "mapeamento":
                return config.getMapeamentoPontos().getOrDefault(resposta.getValor(), 0);
            case "formula":
                return calcularFormula(resposta.getValor(), config.getFormula());
            default:
                return 0;
        }
    }

    public int calcularPontuacaoEtapa(List<Resposta> respostas, RegraCalculo regra) {
        if (respostas == null || respostas.isEmpty()) {
            return 0;
        }

        if (regra == null || regra.getTipoCalculo() == null) {
            return respostas.stream()
                    .mapToInt(this::calcularPontuacaoPergunta)
                    .sum();
        }

        switch (regra.getTipoCalculo()) {
            case "soma":
                return respostas.stream()
                        .mapToInt(this::calcularPontuacaoPergunta)
                        .sum();
            case "media":
                return (int) respostas.stream()
                        .mapToInt(this::calcularPontuacaoPergunta)
                        .average()
                        .orElse(0);
            case "ponderada":
                return calcularMediaPonderada(respostas, regra.getPesos());
            case "custom":
                // TODO: implementar avaliação de fórmula customizada
                return 0;
            default:
                return 0;
        }
    }

    private int calcularValorDireto(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int calcularFormula(String valor, String formula) {
        if (formula == null || formula.isEmpty()) {
            return 0;
        }

        // Suporte para conversão de HH:MM em minutos
        if (formula.equals("HH:MM_para_minutos")) {
            return converterHoraParaMinutos(valor);
        }

        // TODO: Expandir para suportar outras fórmulas matemáticas
        // Pode usar biblioteca como exp4j ou mXparser para avaliar expressões
        return 0;
    }

    private int converterHoraParaMinutos(String valor) {
        if (valor == null || valor.isEmpty()) {
            return 0;
        }

        Pattern pattern = Pattern.compile("(\\d+):(\\d+)");
        Matcher matcher = pattern.matcher(valor);

        if (matcher.matches()) {
            int horas = Integer.parseInt(matcher.group(1));
            int minutos = Integer.parseInt(matcher.group(2));
            return (horas * 60) + minutos;
        }

        return 0;
    }

    private int calcularMediaPonderada(List<Resposta> respostas, Map<Long, Double> pesos) {
        if (pesos == null || pesos.isEmpty()) {
            return (int) respostas.stream()
                    .mapToInt(this::calcularPontuacaoPergunta)
                    .average()
                    .orElse(0);
        }

        double somaTotal = 0.0;
        double somaPesos = 0.0;

        for (Resposta resposta : respostas) {
            Long perguntaId = resposta.getPergunta().getId();
            Double peso = pesos.getOrDefault(perguntaId, 1.0);
            int pontuacao = calcularPontuacaoPergunta(resposta);

            somaTotal += pontuacao * peso;
            somaPesos += peso;
        }

        if (somaPesos == 0) {
            return 0;
        }

        return (int) Math.round(somaTotal / somaPesos);
    }
}
