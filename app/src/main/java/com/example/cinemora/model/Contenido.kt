package com.example.cinemora.model

data class Contenido(
    val id: Int,
    val titulo: String,
    val sinopsis: String,
    val fechaLanzamiento: String,
    val puntuacion: Double,
    val generos: List<String>,
    val tipo: String,
    val posterRes: Int = 0,
    val videoUrl: String? = null,
    val trailerYoutubeId: String? = null,
    val posterUrl: String? = null,
    val tmdbId: Int? = null
)
