package com.example.cinemora.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MiListaRepository {
    val contenidos: StateFlow<List<Contenido>>
    fun agregar(contenido: Contenido)
    fun quitar(contenido: Contenido)
}

/**
 * Lista de sesión: no escribe en TMDB ni persiste al cerrar la app.
 * Queda aislada para sustituirla más adelante por una fuente local si se autoriza.
 */
class MiListaEnMemoriaRepository : MiListaRepository {
    private val _contenidos = MutableStateFlow<List<Contenido>>(emptyList())
    override val contenidos: StateFlow<List<Contenido>> = _contenidos.asStateFlow()

    override fun agregar(contenido: Contenido) {
        if (_contenidos.value.none { it.id == contenido.id && it.tipo == contenido.tipo }) {
            _contenidos.value += contenido
        }
    }

    override fun quitar(contenido: Contenido) {
        _contenidos.value = _contenidos.value.filterNot {
            it.id == contenido.id && it.tipo == contenido.tipo
        }
    }
}
