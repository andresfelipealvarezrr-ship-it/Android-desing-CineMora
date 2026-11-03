package com.example.cinemora.ui.screen

import kotlinx.serialization.Serializable

/**
 * Destinos tipados de CineMora.
 * Para pasar contenido entre pantallas solo se envían su ID y tipo;
 * así evitamos serializar objetos completos o listas de datos.
 */
@Serializable
object HomeRoute

@Serializable
object MiListaRoute

@Serializable
object AgregarRoute

@Serializable
data class DetalleRoute(
    val contenidoId: Int,
    val tipo: String
)

@Serializable
data class ReproductorRoute(
    val contenidoId: Int,
    val tipo: String
)

@Serializable
data class TrailerRoute(
    val contenidoId: Int,
    val tipo: String
)
