package com.tecno.aging.data.remote

import com.tecno.aging.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = SessionManager.getAuthToken()

        val newRequest = originalRequest.newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
            addHeader("Accept", "application/json")
            addHeader("Content-Type", "application/json")
        }.build()

        chain.proceed(newRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Usa HEADERS ao invés de BODY para evitar problemas com respostas grandes (fotos em base64)
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    fun parseErrorBody(errorBody: String?): String? {
        if (errorBody == null) return null
        return try {
            val jsonObj = JSONObject(errorBody)
            jsonObj.optString("error", jsonObj.optString("message", ""))
        } catch (e: Exception) {
            null
        }
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}