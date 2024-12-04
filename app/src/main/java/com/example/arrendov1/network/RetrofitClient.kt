package com.example.arrendov1.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Define la URL base de tu servidor
    private const val BASE_URL = "http://10.0.2.2:8000"

    // Configura el cliente Retrofit
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Usa la URL base para todas las solicitudes
            .addConverterFactory(GsonConverterFactory.create()) // Conversión de JSON
            .build()
            .create(ApiService::class.java)
    }
}