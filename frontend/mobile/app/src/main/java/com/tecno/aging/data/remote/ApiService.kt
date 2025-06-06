package com.tecno.aging.data.remote

import com.tecno.aging.domain.models.DTO.CepResponse
import com.tecno.aging.domain.models.auth.LoginRequest
import com.tecno.aging.domain.models.auth.LoginResponse
import com.tecno.aging.domain.models.auth.TecnicoRequest
import com.tecno.aging.domain.models.paciente.Paciente
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import retrofit2.Response
import retrofit2.http.*
interface ApiService {
    @POST("api/tecnicos")
    suspend fun registrarTecnico(@Body request: TecnicoRequest): Response<Unit>


    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("https://viacep.com.br/ws/{cep}/json/")
    suspend fun buscarCep(@Path("cep") cep: String): Response<CepResponse>

    @GET("api/pacientes")
    suspend fun getPacientes(): Response<List<Paciente>>
}