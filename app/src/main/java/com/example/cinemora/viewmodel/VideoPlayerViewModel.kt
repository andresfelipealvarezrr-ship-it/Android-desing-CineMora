package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VideoPlayerViewModel : ViewModel() {
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun informarError() {
        _error.value = "No se pudo cargar la película. Comprueba tu conexión e inténtalo nuevamente."
    }
}
