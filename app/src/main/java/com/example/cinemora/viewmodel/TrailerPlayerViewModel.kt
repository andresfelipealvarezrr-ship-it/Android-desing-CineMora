package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemora.data.remote.TmdbClient
import com.example.cinemora.model.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrailerPlayerViewModel : ViewModel() {
    private val _videoId = MutableStateFlow("")
    val videoId = _videoId.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    fun cargar(contenido: Contenido?) {
        if (contenido == null || _cargando.value || _videoId.value.isNotBlank()) return
        contenido.trailerYoutubeId?.let {
            _videoId.value = it
            return
        }
        val tmdbId = contenido.tmdbId ?: return
        viewModelScope.launch {
            _cargando.value = true
            _videoId.value = TmdbClient.api.obtenerTrailer(tmdbId, contenido.tipo).orEmpty()
            _cargando.value = false
        }
    }

    fun titulo(contenido: Contenido?): String = contenido?.titulo.orEmpty()
}
