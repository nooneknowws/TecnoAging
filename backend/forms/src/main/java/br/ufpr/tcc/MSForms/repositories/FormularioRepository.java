package br.ufpr.tcc.MSForms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ufpr.tcc.MSForms.models.Formulario;

public interface FormularioRepository extends JpaRepository<Formulario, Long> {
}
