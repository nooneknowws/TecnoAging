package com.tecno.aging.data.remote

import com.tecno.aging.domain.models.DTO.AvaliacaoPostDTO
import com.tecno.aging.domain.models.DTO.CepResponse
import com.tecno.aging.domain.models.DTO.TecnicoUpdateRequest
import com.tecno.aging.domain.models.DTO.VerifyJwtResponse
import com.tecno.aging.domain.models.auth.EnviarCodigoRequest
import com.tecno.aging.domain.models.auth.EnviarCodigoResponse
import com.tecno.aging.domain.models.auth.LoginRequest
import com.tecno.aging.domain.models.auth.LoginResponse
import com.tecno.aging.domain.models.auth.ResetPasswordDTO
import com.tecno.aging.domain.models.auth.TecnicoRequest
import com.tecno.aging.domain.models.forms.GenericForm
import com.tecno.aging.domain.models.historico.HistoricoAvaliacao
import com.tecno.aging.domain.models.pessoa.paciente.Paciente
import com.tecno.aging.domain.models.pessoa.paciente.PacienteRequest
import com.tecno.aging.domain.models.pessoa.tecnico.Tecnico
import retrofit2.Response
import retrofit2.http.*

data class ImageUploadRequest(val image: String)
data class ImageResponse(val image: String)

interface ApiService {
    //auth
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("api/auth/verify-jwt")
    suspend fun verifyJwt(@Body body: Map<String, String> = emptyMap()): Response<VerifyJwtResponse>

    @POST("/api/auth/enviar-codigo")
    suspend fun enviarCodigoRecuperacao(
        @Body request: EnviarCodigoRequest
    ): Response<EnviarCodigoResponse>

    @POST("/api/auth/reset-password")
    suspend fun resetarSenha(
        @Body request: ResetPasswordDTO
    ): Response<Map<String, String>>

    // aux
    @GET("https://viacep.com.br/ws/{cep}/json/")
    suspend fun buscarCep(@Path("cep") cep: String): Response<CepResponse>

    // Tecnicos
    @POST("api/tecnicos")
    suspend fun registrarTecnico(@Body request: TecnicoRequest): Response<Unit>

    @GET("api/tecnicos/{id}")
    suspend fun getTecnicoById(@Path("id") tecnicoId: Int): Response<Tecnico>

    @PUT("api/tecnicos/{id}")
    suspend fun updateTecnico(
        @Path("id") tecnicoId: Int,
        @Body request: TecnicoUpdateRequest
    ): Response<Tecnico>

    @GET("api/tecnicos/{id}/foto")
    suspend fun getFotoTecnico(@Path("id") id: Int): Response<ImageResponse>

    @POST("api/tecnicos/{id}/foto")
    suspend fun uploadFotoTecnico(
        @Path("id") id: Int,
        @Body request: ImageUploadRequest
    ): Response<Unit>

    // Pacientes
    @GET("api/pacientes")
    suspend fun getPacientes(): Response<List<Paciente>>

    @PUT("api/pacientes/{id}")
    suspend fun updatePaciente(
        @Path("id") id: Int,
        @Body paciente: Paciente
    ): Response<Paciente>

    @GET("api/pacientes/{id}")
    suspend fun getPacienteById(@Path("id") pacienteId: Int): Response<Paciente>

    @POST("api/pacientes")
    suspend fun registrarPaciente(@Body request: PacienteRequest): Response<Unit>

    @GET("api/pacientes/{id}/foto")
    suspend fun getFotoPaciente(@Path("id") id: Int): Response<ImageResponse>

    @POST("api/pacientes/{id}/foto")
    suspend fun uploadFotoPaciente(
        @Path("id") id: Int,
        @Body request: ImageUploadRequest
    ): Response<Unit>

    // Avaliações
    @GET("api/avaliacoes/respostas/paciente/{id}")
    suspend fun getRespostasByPaciente(@Path("id") pacienteId: Int): Response<List<HistoricoAvaliacao>>

    @GET("api/avaliacoes/avaliacao/{id}")
    suspend fun getAvaliacaoById(@Path("id") avaliacaoId: Long): Response<HistoricoAvaliacao>

    // Formulários
    @GET("api/formularios/")
    suspend fun getTodosFormularios(): Response<List<GenericForm>>

    @GET("api/formularios/{id}")
    suspend fun getFormularioById(@Path("id") formularioId: Long): Response<GenericForm>

    // Avaliações
    @POST("api/avaliacoes/forms")
    suspend fun salvarAvaliacao(@Body avaliacao: AvaliacaoPostDTO): Response<Map<String, String>>

    @PUT("api/avaliacoes/forms/update/{id}")
    suspend fun updateAvaliacao(
        @Path("id") avaliacaoId: Long,
        @Body avaliacao: AvaliacaoPostDTO
    ): Response<Map<String, Any>>

}