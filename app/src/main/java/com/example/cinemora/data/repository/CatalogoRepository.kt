package com.example.cinemora.data.repository

/** Resultado explícito de una consulta del catálogo, para no esconder fallos como datos locales. */
sealed interface ResultadoCatalogo {
    data class Exito(val contenidos: List<Contenido>) : ResultadoCatalogo
    data object Vacio : ResultadoCatalogo
    data class Error(val mensaje: String) : ResultadoCatalogo
}

sealed interface ResultadoTrailer {
    data class Disponible(val youtubeId: String) : ResultadoTrailer
    data object NoDisponible : ResultadoTrailer
    data class Error(val mensaje: String) : ResultadoTrailer
}

interface CatalogoRepository {
    suspend fun obtenerCatalogo(): ResultadoCatalogo
    suspend fun obtenerTrailer(contenido: Contenido): ResultadoTrailer
    fun buscarContenido(id: Int, tipo: String): Contenido?
}
