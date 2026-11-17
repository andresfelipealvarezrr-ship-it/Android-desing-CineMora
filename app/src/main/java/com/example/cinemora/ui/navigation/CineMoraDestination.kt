package com.example.cinemora.ui.navigation

import kotlinx.serialization.Serializable

/** Rutas tipadas de Navigation Compose. */
@Serializable
data object Inicio

@Serializable
data object Lista

@Serializable
data object AgregarContenido

@Serializable
data class Detalle(
    val contenidoId: Int,
    val contenidoTipo: String
)

@Serializable
data object Perfil

@Serializable
data class Reproductor(
    val contenidoId: Int,
    val contenidoTipo: String
)

@Serializable
data class Trailer(
    val contenidoId: Int,
    val contenidoTipo: String
)
