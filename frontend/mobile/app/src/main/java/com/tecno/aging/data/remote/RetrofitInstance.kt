package com.tecno.aging.data.remote

import com.tecno.aging.data.local.SessionManager
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
  //pra emulador  private const val BASE_URL = "http://10.0.2.2:3000/"
  //pra espelhamento
  private const val BASE_URL = "http://172.20.149.231:3000/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

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
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}