package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.repository.CineRepository

class HomeViewModel : ViewModel() {
    val catalogo = CineRepository.catalogo
    val miLista = CineRepository.miLista

    fun actualizarCatalogo() = CineRepository.actualizarDesdeTmdb()
}
