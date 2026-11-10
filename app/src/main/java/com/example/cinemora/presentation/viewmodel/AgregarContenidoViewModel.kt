package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.data.model.Contenido
import com.example.cinemora.data.repository.CineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FormularioContenido(
    val titulo: String = "",
    val tipo: String = "película",
    val sinopsis: String = "",
    val errorTitulo: Boolean = false
)

class AgregarContenidoViewModel : ViewModel() {
    private val _formulario = MutableStateFlow(FormularioContenido())
    val formulario = _formulario.asStateFlow()

    fun cambiarTitulo(valor: String) {
        _formulario.update { it.copy(titulo = valor, errorTitulo = false) }
    }

    fun cambiarTipo(valor: String) {
        _formulario.update { it.copy(tipo = valor) }
    }

    fun cambiarSinopsis(valor: String) {
        _formulario.update { it.copy(sinopsis = valor) }
    }

    fun guardar(): Contenido? {
        val actual = _formulario.value
        if (actual.titulo.isBlank()) {
            _formulario.update { it.copy(errorTitulo = true) }
            return null
        }
        val contenido = CineRepository.agregarContenido(actual.titulo, actual.tipo, actual.sinopsis)
        _formulario.value = FormularioContenido()
        return contenido
    }
}
