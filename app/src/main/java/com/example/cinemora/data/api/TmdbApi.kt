package com.example.cinemora.data.api

import com.example.cinemora.data.dto.TmdbTrendingResponseDto
import com.example.cinemora.data.dto.TmdbVideosResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Contrato HTTP: Retrofit crea la implementación a partir de esta interfaz. */
interface TmdbApi {
    @GET("3/trending/all/day")
    suspend fun obtenerTendencias(
        @Query("language") language: String = "es-CO"
    ): Response<TmdbTrendingResponseDto>

    @GET("3/movie/{id}/videos")
    suspend fun obtenerVideosDePelicula(
        @Path("id") id: Int,
        @Query("language") language: String = "es-CO"
    ): Response<TmdbVideosResponseDto>

    @GET("3/tv/{id}/videos")
    suspend fun obtenerVideosDeSerie(
        @Path("id") id: Int,
        @Query("language") language: String = "es-CO"
    ): Response<TmdbVideosResponseDto>
}
