package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemora.data.repository.AppContainer
import com.example.cinemora.data.repository.CatalogoRepository
import com.example.cinemora.data.repository.Contenido
import com.example.cinemora.data.repository.MiListaRepository
import com.example.cinemora.data.repository.ResultadoCatalogo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Cargando : HomeUiState
    data class Exito(val contenidos: List<Contenido>) : HomeUiState
    data object Vacio : HomeUiState
    data class Error(val mensaje: String) : HomeUiState
}

class HomeViewModel(
    private val catalogoRepository: CatalogoRepository = AppContainer.catalogoRepository,
    miListaRepository: MiListaRepository = AppContainer.miListaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Cargando)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    val miLista = miListaRepository.contenidos

    init {
        cargarCatalogo()
    }

    fun cargarCatalogo() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Cargando
            _uiState.value = when (val resultado = catalogoRepository.obtenerCatalogo()) {
                is ResultadoCatalogo.Exito -> HomeUiState.Exito(resultado.contenidos)
                ResultadoCatalogo.Vacio -> HomeUiState.Vacio
                is ResultadoCatalogo.Error -> HomeUiState.Error(resultado.mensaje)
            }
        }
    }
}
