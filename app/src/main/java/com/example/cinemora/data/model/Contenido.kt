package com.example.cinemora.data.model

data class Contenido(
    val id: String,
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
) {
    val esManual: Boolean
        get() = id.startsWith("manual:")

    val anio: String
        get() = fechaLanzamiento.take(4).takeIf { it.length == 4 && it.all(Char::isDigit) } ?: "Sin fecha"
}
