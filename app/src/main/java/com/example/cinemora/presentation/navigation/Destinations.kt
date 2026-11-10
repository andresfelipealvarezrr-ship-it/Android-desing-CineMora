package com.example.cinemora.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Destino {
    @Serializable
    data object Inicio : Destino

    @Serializable
    data object MiLista : Destino

    @Serializable
    data object Agregar : Destino

    @Serializable
    data object Perfil : Destino

    @Serializable
    data class Detalle(val id: String) : Destino

    @Serializable
    data class Reproductor(val id: String) : Destino

    @Serializable
    data class Trailer(val id: String) : Destino
}
