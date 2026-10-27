package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.repository.CineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AgregarContenidoViewModel : ViewModel() {
    private val _titulo = MutableStateFlow("")
    val titulo = _titulo.asStateFlow()

    private val _esError = MutableStateFlow(false)
    val esError = _esError.asStateFlow()

    fun cambiarTitulo(valor: String) {
        _titulo.value = valor
        if (valor.isNotBlank()) _esError.value = false
    }

    fun guardar(): Boolean {
        if (_titulo.value.isBlank()) {
            _esError.value = true
            return false
        }
        CineRepository.agregarContenido(_titulo.value)
        _titulo.value = ""
        return true
    }
}
