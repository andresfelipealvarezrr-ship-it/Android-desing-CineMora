package com.example.cinemora.data.remote

/** Punto único de acceso remoto para facilitar pruebas y futuros cambios de API. */
object TmdbClient {
    val api: TmdbApiService = TmdbApiService
}
