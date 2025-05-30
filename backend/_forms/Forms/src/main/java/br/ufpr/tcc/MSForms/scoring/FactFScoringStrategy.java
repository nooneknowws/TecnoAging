package br.ufpr.tcc.MSForms.scoring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufpr.tcc.MSForms.models.Resposta;

public class FactFScoringStrategy implements ScoringStrategy {
    
    // Mapeamento das escalas de resposta para pontuação
    private final Map<String, Integer> respostaParaPontos = Map.of(
        "\"Nem um pouco\"", 0,
        "\"Um pouco\"", 1,
        "\"Mais ou menos\"", 2,
        "\"Muito\"", 3,
        "\"Muitíssimo\"", 4
    );
    
    // Perguntas que precisam de inversão de pontuação (4 - resposta)
    private final Map<String, Boolean> perguntasComInversao = new HashMap<>();
    
    public FactFScoringStrategy() {
        initializePerguntasComInversao();
    }
    
    private void initializePerguntasComInversao() {
        // Bem-estar Físico - todas precisam inversão
        perguntasComInversao.put("Estou sem energia", true);
        perguntasComInversao.put("Fico enjoado (a)", true);
        perguntasComInversao.put("Por causa do meu estado físico, tenho dificuldade em atender às necessidades da minha família", true);
        perguntasComInversao.put("Tenho dores", true);
        perguntasComInversao.put("Sinto-me incomodado (a) pelos efeitos secundários do tratamento", true);
        perguntasComInversao.put("Sinto-me doente", true);
        perguntasComInversao.put("Tenho que me deitar durante o dia", true);
        
        // Bem-estar Social/Familiar - não precisam inversão
        perguntasComInversao.put("Sinto que tenho uma boa relação com os meus amigos", false);
        perguntasComInversao.put("Recebo apoio emocional da minha família", false);
        perguntasComInversao.put("Recebo apoio dos meus amigos", false);
        perguntasComInversao.put("A minha família aceita a minha doença", false);
        perguntasComInversao.put("Estou satisfeito (a) com a maneira como a minha família fala sobre a minha doença", false);
        perguntasComInversao.put("Sinto-me próximo (a) do(a) meu (minha) parceiro(a)", false);
        perguntasComInversao.put("Estou satisfeito (a) com a minha vida sexual", false);
        
        // Bem-estar Emocional - algumas precisam inversão
        perguntasComInversao.put("Sinto-me triste", true);
        perguntasComInversao.put("Estou satisfeito (a) com a maneira como enfrento a minha doença", false);
        perguntasComInversao.put("Estou perdendo a esperança na luta contra a minha doença", true);
        perguntasComInversao.put("Sinto-me nervoso (a)", true);
        perguntasComInversao.put("Estou preocupado (a) com a idéia de morrer", true);
        perguntasComInversao.put("Estou preocupado (a) que o meu estado venha a piorar", true);
        
        // Bem-estar Funcional - não precisam inversão
        perguntasComInversao.put("Sou capaz de trabalhar (inclusive em casa)", false);
        perguntasComInversao.put("Sinto-me realizado (a) com o meu trabalho (inclusive em casa)", false);
        perguntasComInversao.put("Sou capaz de sentir prazer em viver", false);
        perguntasComInversao.put("Aceito a minha doença", false);
        perguntasComInversao.put("Durmo bem", false);
        perguntasComInversao.put("Gosto das coisas que normalmente faço para me divertir", false);
        perguntasComInversao.put("Estou satisfeito (a) com a qualidade da minha vida neste momento", false);
        
        // Preocupações Adicionais (Fadiga) - algumas precisam inversão
        perguntasComInversao.put("Sinto-me fatigado (a)", true);
        perguntasComInversao.put("Sinto fraqueza generalizada", true);
        perguntasComInversao.put("Sinto-me sem forças", true);
        perguntasComInversao.put("Sinto-me cansado (a)", true);
        perguntasComInversao.put("Tenho dificuldade em começar as coisas porque estou cansado (a)", true);
        perguntasComInversao.put("Tenho dificuldade em acabar as coisas porque estou cansado(a)", true);
        perguntasComInversao.put("Tenho energia", false);
        perguntasComInversao.put("Sou capaz de fazer as minhas atividades normais", false);
        perguntasComInversao.put("Preciso (de) dormir durante o dia", true);
        perguntasComInversao.put("Estou cansado (a) demais para comer", true);
        perguntasComInversao.put("Preciso de ajuda para fazer as minhas atividades normais", true);
        perguntasComInversao.put("Estou frustrado (a) por estar cansado (a) demais para fazer as coisas que quero", true);
        perguntasComInversao.put("Tenho que limitar as minhas atividades sociais por estar cansado (a)", true);
    }
    
    @Override
    public int calculateScore(List<Resposta> respostas) {
        int pontuacaoTotal = 0;
        
        for (Resposta resposta : respostas) {
            String textoPergunta = resposta.getPergunta().getTexto();
            String valorResposta = resposta.getValor();
            
            if (respostaParaPontos.containsKey(valorResposta)) {
                int pontosBrutos = respostaParaPontos.get(valorResposta);
                
                // Verificar se precisa de inversão
                Boolean precisaInversao = perguntasComInversao.get(textoPergunta);
                if (precisaInversao != null && precisaInversao) {
                    pontuacaoTotal += (4 - pontosBrutos);
                } else {
                    pontuacaoTotal += pontosBrutos;
                }
            }
        }
        
        return pontuacaoTotal;
    }
    
    @Override
    public int getMaxScore() {
        // FACT-F: 27 itens gerais (4 pontos cada) + 13 itens de fadiga (4 pontos cada)
        // Total: 40 itens × 4 pontos = 160 pontos
        return 160;
    }
    
    @Override
    public String getFormType() {
        return "factf";
    }
}