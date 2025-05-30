package br.ufpr.tcc.MSForms.scoring;

public class ScoringStrategyFactory {
    
    public static ScoringStrategy createStrategy(String formType) {
        switch (formType.toLowerCase()) {
            case "factf":
                return new FactFScoringStrategy();
            case "minimental":
                return new MiniMentalScoringStrategy();
            case "ivcf20":
            	return new IVCF20ScoringStrategy();
            default:
                throw new IllegalArgumentException("Tipo de formulário não suportado: " + formType);
        }
    }
}