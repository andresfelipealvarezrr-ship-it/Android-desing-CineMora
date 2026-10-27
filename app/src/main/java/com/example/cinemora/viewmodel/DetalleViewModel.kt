package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.model.Contenido
import com.example.cinemora.repository.CineRepository

class DetalleViewModel : ViewModel() {
    val miLista = CineRepository.miLista

    fun alternarGuardado(contenido: Contenido, guardado: Boolean) {
        if (guardado) CineRepository.quitarDeMiLista(contenido)
        else CineRepository.agregarAMiLista(contenido)
    }
}
