package com.example.cinemora.data.remote.dto

data class TmdbContenidoDto(
    val id: Int,
    val titulo: String,
    val sinopsis: String,
    val fecha: String,
    val puntuacion: Double,
    val generos: List<Int>,
    val tipo: String,
    val posterPath: String?
)
