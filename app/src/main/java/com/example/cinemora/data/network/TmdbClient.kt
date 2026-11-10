package com.example.cinemora.data.network

import com.example.cinemora.data.api.TmdbApiService

/** Punto único de acceso remoto para facilitar pruebas y futuros cambios de API. */
object TmdbClient {
    val api: TmdbApiService = TmdbApiService
}
