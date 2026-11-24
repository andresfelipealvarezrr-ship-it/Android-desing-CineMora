package com.example.cinemora.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemora.data.repository.AppContainer
import com.example.cinemora.data.repository.CatalogoRepository
import com.example.cinemora.data.repository.Contenido
import com.example.cinemora.data.repository.ResultadoTrailer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TrailerUiState {
    data object Cargando : TrailerUiState
    data class Disponible(val videoId: String) : TrailerUiState
    data object NoDisponible : TrailerUiState
    data class Error(val mensaje: String) : TrailerUiState
}

class TrailerPlayerViewModel(
    private val catalogoRepository: CatalogoRepository = AppContainer.catalogoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<TrailerUiState>(TrailerUiState.Cargando)
    val uiState = _uiState.asStateFlow()

    fun cargar(contenido: Contenido) {
        viewModelScope.launch {
            _uiState.value = TrailerUiState.Cargando
            _uiState.value = when (val resultado = catalogoRepository.obtenerTrailer(contenido)) {
                is ResultadoTrailer.Disponible -> TrailerUiState.Disponible(resultado.youtubeId)
                ResultadoTrailer.NoDisponible -> TrailerUiState.NoDisponible
                is ResultadoTrailer.Error -> TrailerUiState.Error(resultado.mensaje)
            }
        }
    }

    fun titulo(contenido: Contenido): String = contenido.titulo
}
