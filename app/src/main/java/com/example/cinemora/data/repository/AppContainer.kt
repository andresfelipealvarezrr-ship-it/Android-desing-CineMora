package com.example.cinemora.data.repository

/** Ensamblado manual de dependencias, sin framework de inyección. */
object AppContainer {
    val catalogoRepository: CatalogoRepository by lazy { TmdbCatalogoRepository() }
    val miListaRepository: MiListaRepository by lazy { MiListaEnMemoriaRepository() }
}
