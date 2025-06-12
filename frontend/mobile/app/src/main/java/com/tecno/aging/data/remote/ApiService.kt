package com.tecno.aging.data.remote

import com.tecno.aging.domain.models.DTO.CepResponse
import com.tecno.aging.domain.models.DTO.VerifyJwtResponse
import com.tecno.aging.domain.models.auth.LoginRequest
import com.tecno.aging.domain.models.auth.LoginResponse
import com.tecno.aging.domain.models.auth.TecnicoRequest
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import com.tecno.aging.domain.models.paciente.Paciente
import com.tecno.aging.domain.models.pessoa.Endereco
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import retrofit2.Response
import retrofit2.http.*
interface ApiService {
    //auth
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("api/auth/verify-jwt")
    suspend fun verifyJwt(@Body body: Map<String, String> = emptyMap()): Response<VerifyJwtResponse>

    // aux
    @GET("https://viacep.com.br/ws/{cep}/json/")
    suspend fun buscarCep(@Path("cep") cep: String): Response<CepResponse>

    // Tecnicos
    @POST("api/tecnicos")
    suspend fun registrarTecnico(@Body request: TecnicoRequest): Response<Unit>

    @GET("api/tecnicos/{id}")
    suspend fun getTecnicoById(@Path("id") tecnicoId: Int): Response<Paciente>

    // Pacientes
    @GET("api/pacientes")
    suspend fun getPacientes(): Response<List<Paciente>>

    @GET("api/pacientes/{id}")
    suspend fun getPacienteById(@Path("id") pacienteId: Int): Response<Paciente>

    // Avaliações
    @GET("api/avaliacoes/respostas/paciente/{id}")
    suspend fun getRespostasByPaciente(@Path("id") pacienteId: Int): Response<List<HistoricoAvaliacao>>

    @GET("api/avaliacoes/respostas/tecnico/{id}")
    suspend fun getRespostasByTecnico(@Path("id") tecnicoId: Int): Response<Unit>

    @GET("api/avaliacoes/avalicao/{id}")
    suspend fun getAvaliacoes(@Path("id") avalicaoId: Int): Response<Unit>

}