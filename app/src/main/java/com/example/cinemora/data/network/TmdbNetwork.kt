package com.example.cinemora.data.network

import com.example.cinemora.BuildConfig
import com.example.cinemora.data.api.TmdbApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Único lugar que configura Retrofit y OkHttp. El token se obtiene del BuildConfig
 * generado desde local.properties; nunca se escribe ni se registra aquí.
 */
object TmdbNetwork {
    private const val BASE_URL = "https://api.themoviedb.org/"
    private val accessToken: String get() = BuildConfig.TMDB_ACCESS_TOKEN.trim()

    fun tieneToken(): Boolean = accessToken.isNotBlank()

    val api: TmdbApi by lazy {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Accept", "application/json")
                .apply {
                    if (accessToken.isNotBlank()) {
                        header("Authorization", "Bearer $accessToken")
                    }
                }
                .build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TmdbApi::class.java)
    }
}
