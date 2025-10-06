package br.ufpr.tcc.MSTecnicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.ufpr.tcc.MSTecnicos.models.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {

	Optional<Tecnico> findByCpf(String cpf);
	
	@Query(value = "SELECT id, ativo, cpf, data_nasc, bairro, cep, complemento, logradouro, municipio, numero, uf, null as foto_perfil, idade, matricula, nome, password_hash, salt, senha, sexo, telefone FROM tecnico WHERE REGEXP_REPLACE(cpf, '[^0-9]', '', 'g') = :cpf", nativeQuery = true)
	Optional<Tecnico> findByCpfForAuth(@Param("cpf") String cpf);
}
