package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.data.model.Contenido
import com.example.cinemora.data.repository.CineRepository

class DetalleViewModel : ViewModel() {
    val miLista = CineRepository.miLista

    fun eliminarContenidoManual(contenido: Contenido) = CineRepository.eliminarContenidoManual(contenido)

    fun alternarGuardado(contenido: Contenido, guardado: Boolean) {
        if (guardado) CineRepository.quitarDeMiLista(contenido)
        else CineRepository.agregarAMiLista(contenido)
    }
}
