package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** El catálogo remoto es de solo lectura; esta pantalla informa el alcance actual. */
class AgregarContenidoViewModel : ViewModel() {
    private val _mensaje = MutableStateFlow(
        "El catálogo de Inicio proviene de TMDB y es de solo lectura."
    )
    val mensaje = _mensaje.asStateFlow()
}
