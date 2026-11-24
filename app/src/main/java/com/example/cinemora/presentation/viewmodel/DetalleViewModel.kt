package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.data.repository.AppContainer
import com.example.cinemora.data.repository.CatalogoRepository
import com.example.cinemora.data.repository.Contenido
import com.example.cinemora.data.repository.MiListaRepository

class DetalleViewModel(
    private val catalogoRepository: CatalogoRepository = AppContainer.catalogoRepository,
    private val miListaRepository: MiListaRepository = AppContainer.miListaRepository
) : ViewModel() {
    val miLista = miListaRepository.contenidos

    fun obtenerContenido(id: Int, tipo: String): Contenido? =
        catalogoRepository.buscarContenido(id, tipo)

    fun alternarGuardado(contenido: Contenido, guardado: Boolean) {
        if (guardado) miListaRepository.quitar(contenido)
        else miListaRepository.agregar(contenido)
    }
}
