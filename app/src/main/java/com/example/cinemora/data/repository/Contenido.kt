package com.example.cinemora.data.repository

data class Contenido(
    val id: Int,
    val titulo: String,
    val sinopsis: String,
    val fechaLanzamiento: String,
    val puntuacion: Double,
    val generos: List<String>,
    /** `movie` o `tv`, tal como lo identifica TMDB. */
    val tipo: String,
    val posterUrl: String? = null
)
