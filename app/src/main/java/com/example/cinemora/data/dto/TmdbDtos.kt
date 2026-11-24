package com.example.cinemora.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Respuesta real de GET /3/trending/all/day de TMDB. */
@Serializable
data class TmdbTrendingResponseDto(
    val page: Int = 1,
    val results: List<TmdbContenidoDto> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 0,
    @SerialName("total_results") val totalResults: Int = 0
)

/** Campos comunes de películas y series en el endpoint de tendencias. */
@Serializable
data class TmdbContenidoDto(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    val overview: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("poster_path") val posterPath: String? = null
)

/** Respuesta real de los endpoints /videos de TMDB. */
@Serializable
data class TmdbVideosResponseDto(
    val id: Int,
    val results: List<TmdbVideoDto> = emptyList()
)

@Serializable
data class TmdbVideoDto(
    val key: String,
    val site: String,
    val type: String,
    val official: Boolean = false
)
