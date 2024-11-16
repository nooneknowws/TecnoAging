package br.ufpr.tcc.MSForms.service;

import org.springframework.stereotype.Service;

import br.ufpr.tcc.MSForms.models.Formulario;
import br.ufpr.tcc.MSForms.repositories.FormularioRepository;

@Service
public class FormularioService {

    private final FormularioRepository formularioRepository;

    public FormularioService(FormularioRepository formularioRepository) {
        this.formularioRepository = formularioRepository;
    }

    public Formulario salvarFormulario(Formulario formulario) {
        return formularioRepository.save(formulario);
    }
}
