package br.ufpr.tcc.MSPacientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.ufpr.tcc.MSPacientes.models.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

	Optional<Paciente> findByCpf(String cpf);
	
	@Query(value = "SELECT id, altura, cor_raca, cpf, data_expedicao, data_nasc, bairro, cep, complemento, logradouro, municipio, numero, uf, escolaridade, estado_civil, null as foto_perfil, idade, imc, municipio_nasc, nacionalidade, nome, orgao_emissor, password_hash, peso, rg, salt, senha, sexo, socioeconomico, telefone, uf_emissor, uf_nasc, version FROM paciente WHERE REGEXP_REPLACE(cpf, '[^0-9]', '', 'g') = :cpf", nativeQuery = true)
	Optional<Paciente> findByCpfForAuth(@Param("cpf") String cpf);
}
