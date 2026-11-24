package com.example.cinemora.data.repository

import org.junit.Assert.assertEquals
import org.junit.Test

class MiListaEnMemoriaRepositoryTest {
    @Test
    fun evitaDuplicadosYQuitaSoloElElementoSeleccionado() {
        val repository = MiListaEnMemoriaRepository()
        val pelicula = Contenido(
            id = 42,
            titulo = "Título de prueba",
            sinopsis = "Sinopsis",
            fechaLanzamiento = "2026-01-01",
            puntuacion = 8.0,
            generos = listOf("Drama"),
            tipo = "movie"
        )

        repository.agregar(pelicula)
        repository.agregar(pelicula)
        assertEquals(1, repository.contenidos.value.size)

        repository.quitar(pelicula)
        assertEquals(emptyList<Contenido>(), repository.contenidos.value)
    }
}
