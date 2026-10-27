package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.model.Contenido
import com.example.cinemora.repository.CineRepository

class MiListaViewModel : ViewModel() {
    val contenidos = CineRepository.miLista
    fun quitar(contenido: Contenido) = CineRepository.quitarDeMiLista(contenido)
}
