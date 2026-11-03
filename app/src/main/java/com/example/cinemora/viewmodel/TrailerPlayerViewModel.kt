package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.model.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrailerPlayerViewModel : ViewModel() {
    private val _videoId = MutableStateFlow("")
    val videoId = _videoId.asStateFlow()

    val cargando = MutableStateFlow(false).asStateFlow()

    fun cargar(contenido: Contenido?) {
        _videoId.value = contenido?.trailerYoutubeId.orEmpty()
    }

    fun titulo(contenido: Contenido?): String = contenido?.titulo.orEmpty()
}
