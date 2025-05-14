package br.ufpr.tcc.MSForms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufpr.tcc.MSForms.models.Avaliacao;
import br.ufpr.tcc.MSForms.repositories.AvaliacaoRepository;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository respostaRepository;

    public List<Object[]> getPerguntasAndValores(Long avaliacaoId) {
        return respostaRepository.findPerguntasAndValoresByAvaliacaoId(avaliacaoId);
    }
}