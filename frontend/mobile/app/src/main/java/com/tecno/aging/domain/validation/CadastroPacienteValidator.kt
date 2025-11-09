package com.tecno.aging.domain.validation

import com.tecno.aging.domain.utils.ValidationUtils
import com.tecno.aging.ui.screens.cadastro.pacienteCadastro.CadastroPacienteState

object CadastroPacienteValidator {

    fun validateStep1(state: CadastroPacienteState): Map<String, String> {
        val erros = mutableMapOf<String, String>()
        with(state) {
            if (nome.isBlank()) erros["nome"] = "Nome é obrigatório"
            val cpfLimpo = cpf.filter { it.isDigit() }
            if (cpfLimpo.length != 11 || !ValidationUtils.isCpfValido(cpfLimpo)) {
                erros["cpf"] = "CPF inválido"
            }
            if (sexo.isBlank()) erros["sexo"] = "Sexo é obrigatório"
            if (!ValidationUtils.isDataNascimentoValida(dataNascimento)) {
                erros["dataNascimento"] = "Data inválida"
            }
            if (telefone.filter { it.isDigit() }.length < 10) erros["telefone"] = "Telefone inválido"
        }
        return erros
    }

    fun validateStep2(state: CadastroPacienteState): Map<String, String> {
        val erros = mutableMapOf<String, String>()
        with(state) {
            if (estadoCivil.isBlank()) erros["estadoCivil"] = "Estado civil é obrigatório"
            if (escolaridade.isBlank()) erros["escolaridade"] = "Escolaridade é obrigatória"
            if (nacionalidade.isBlank()) erros["nacionalidade"] = "Nacionalidade é obrigatória"
            if (corRaca.isBlank()) erros["corRaca"] = "Cor/Raça é obrigatória"
            if (municipioNasc.isBlank()) erros["municipioNasc"] = "Município de nascimento é obrigatório"
            if (ufNasc.isBlank()) erros["ufNasc"] = "UF de nascimento é obrigatório"
        }
        return erros
    }

    fun validateStep3(state: CadastroPacienteState): Map<String, String> {
        val erros = mutableMapOf<String, String>()
        with(state) {
            if (rg.length < 5) {
                erros["rg"] = "RG inválido (mín. 5 caracteres)"
            }
            if (dataExpedicao.isBlank()) erros["dataExpedicao"] = "Data de expedição é obrigatória"
            if (orgaoEmissor.isBlank()) erros["orgaoEmissor"] = "Órgão emissor é obrigatório"
            if (ufEmissor.isBlank()) erros["ufEmissor"] = "UF de emissão é obrigatória"
            if (peso.isBlank()) erros["peso"] = "Peso é obrigatório"
            if (altura.isBlank()) erros["altura"] = "Altura é obrigatória"
            if (socioeconomico.isBlank()) erros["socioeconomico"] = "Nível socioeconômico é obrigatório"
        }
        return erros
    }

    fun validateStep4(state: CadastroPacienteState): Map<String, String> {
        val erros = mutableMapOf<String, String>()
        with(state) {
            if (endereco.cep.filter { it.isDigit() }.length != 8) erros["cep"] = "CEP inválido"
            if (endereco.logradouro.isBlank()) erros["logradouro"] = "Logradouro é obrigatório"
            if (endereco.numero.isBlank()) erros["numero"] = "Número é obrigatório"
            if (endereco.bairro.isBlank()) erros["bairro"] = "Bairro é obrigatório"
            if (endereco.municipio.isBlank()) erros["municipio"] = "Município é obrigatório"
            if (endereco.uf.isBlank()) erros["uf"] = "UF é obrigatório"
        }
        return erros
    }

    fun validateStep5(state: CadastroPacienteState): Map<String, String> {
        val erros = mutableMapOf<String, String>()
        with(state) {
            if (senha.length < 6) erros["senha"] = "Senha deve ter no mínimo 6 caracteres"
            if (confirmarSenha != senha) erros["confirmarSenha"] = "As senhas não coincidem"
        }
        return erros
    }
}