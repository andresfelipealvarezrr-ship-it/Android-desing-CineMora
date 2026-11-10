package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.data.repository.CineRepository

class HomeViewModel : ViewModel() {
    val catalogo = CineRepository.catalogo
    val miLista = CineRepository.miLista

    fun actualizarCatalogo() = CineRepository.actualizarDesdeTmdb()
}
