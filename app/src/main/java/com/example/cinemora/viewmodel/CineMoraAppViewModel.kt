package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.model.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Destino { INICIO, LISTA, AGREGAR, DETALLE, REPRODUCTOR, TRAILER }

class CineMoraAppViewModel : ViewModel() {
    private val _destino = MutableStateFlow(Destino.INICIO)
    val destino = _destino.asStateFlow()

    private val _seleccionado = MutableStateFlow<Contenido?>(null)
    val seleccionado = _seleccionado.asStateFlow()

    fun navegar(destino: Destino) { _destino.value = destino }
    fun abrirDetalle(contenido: Contenido) {
        _seleccionado.value = contenido
        _destino.value = Destino.DETALLE
    }
}
