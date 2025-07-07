package br.ufpr.tcc.shared.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class RespostaDTO {

    private Long perguntaId;
    private String valor; // Armazena o JSON serializado

    public RespostaDTO() {}

    public RespostaDTO(Long perguntaId, List<String> valores) {
        this.perguntaId = perguntaId;
        this.valor = convertListToJson(valores); // Serializa a lista para JSON
    }

    public Long getPerguntaId() {
        return perguntaId;
    }

    public void setPerguntaId(Long perguntaId) {
        this.perguntaId = perguntaId;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setValores(List<String> valores) {
        this.valor = convertListToJson(valores);
    }

    public List<String> getValores() {
        return convertJsonToList(this.valor);
    }

    private String convertListToJson(List<String> valores) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(valores);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter lista para JSON", e);
        }
    }

    private List<String> convertJsonToList(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para lista", e);
        }
    }
}
