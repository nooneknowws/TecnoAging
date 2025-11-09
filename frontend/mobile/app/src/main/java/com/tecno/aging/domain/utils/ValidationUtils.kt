package com.tecno.aging.domain.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

object ValidationUtils {

    fun isCpfValido(cpf: String): Boolean {
        val numeros = cpf.filter { it.isDigit() }

        if (numeros.length != 11) return false

        if (numeros.all { it == numeros[0] }) return false

        try {
            var soma = 0
            for (i in 0 until 9) {
                soma += numeros[i].digitToInt() * (10 - i)
            }
            var resto = (soma * 10) % 11
            val digito1 = if (resto == 10) 0 else resto

            if (digito1 != numeros[9].digitToInt()) return false

            soma = 0
            for (i in 0 until 10) {
                soma += numeros[i].digitToInt() * (11 - i)
            }
            resto = (soma * 10) % 11
            val digito2 = if (resto == 10) 0 else resto

            if (digito2 != numeros[10].digitToInt()) return false

            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun isDataNascimentoValida(dataNascString: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val dataNascimento = LocalDate.parse(dataNascString, formatter)
            val hoje = LocalDate.now()

            if (dataNascimento.isAfter(hoje)) {
                return false
            }

            val idade = ChronoUnit.YEARS.between(dataNascimento, hoje)

            idade in 18..150

        } catch (e: DateTimeParseException) {
            false
        } catch (e: Exception) {
            false
        }
    }
}