package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.data.repository.AppContainer
import com.example.cinemora.data.repository.Contenido
import com.example.cinemora.data.repository.MiListaRepository

class MiListaViewModel(
    private val miListaRepository: MiListaRepository = AppContainer.miListaRepository
) : ViewModel() {
    val contenidos = miListaRepository.contenidos
    fun quitar(contenido: Contenido) = miListaRepository.quitar(contenido)
}
