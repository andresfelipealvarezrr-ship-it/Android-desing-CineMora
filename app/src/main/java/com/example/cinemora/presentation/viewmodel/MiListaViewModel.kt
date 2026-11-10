package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.data.model.Contenido
import com.example.cinemora.data.repository.CineRepository

class MiListaViewModel : ViewModel() {
    val contenidos = CineRepository.miLista
    fun quitar(contenido: Contenido) = CineRepository.quitarDeMiLista(contenido)
}
