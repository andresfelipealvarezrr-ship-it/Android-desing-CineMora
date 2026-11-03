package com.example.cinemora.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cinemora.R
import com.example.cinemora.model.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Estado compartido de CineMora. Los datos se mantienen localmente en memoria. */
class CineMoraViewModel : ViewModel() {
    private val _catalogo = MutableStateFlow(
        listOf(
        Contenido(1, "Interestelar", "Un grupo de exploradores viaja más allá de nuestra galaxia para buscar un nuevo hogar para la humanidad.", "2014-11-07", 8.7, listOf("Ciencia ficción", "Drama"), "película", R.drawable.poster_space, trailerYoutubeId = "zSWdZVtXT7E"),
        Contenido(2, "El Viaje de Chihiro", "Una joven viajera descubre una ciudad fantástica llena de luces, criaturas y secretos.", "2001-07-20", 8.6, listOf("Animación", "Fantasía"), "película", R.drawable.poster_fantasy, trailerYoutubeId = "ByXuk9QqQkk"),
        Contenido(3, "Blade Runner 2049", "Un detective del futuro descubre un secreto que puede cambiar el destino de su sociedad.", "2017-10-06", 8.0, listOf("Neo-noir", "Ciencia ficción"), "película", R.drawable.poster_noir, trailerYoutubeId = "gCcx85zbxz4"),
        Contenido(4, "Duna: Parte Dos", "Un viajero cruza un desierto remoto hacia un destino capaz de transformar su mundo.", "2024-03-01", 8.5, listOf("Aventura", "Ciencia ficción"), "película", R.drawable.poster_desert, trailerYoutubeId = "Way9Dexny3w"),
        Contenido(
            9,
            "Night of the Living Dead",
            "Largometraje completo de terror dirigido por George A. Romero. Una noche de supervivencia dentro de una casa rodeada por muertos vivientes.",
            "1968-10-01",
            7.8,
            listOf("Terror", "Suspenso"),
            "película completa",
            R.drawable.poster_noir,
            "https://archive.org/download/night-of-the-living-dead-1968-horror-mystery-480-x-640.mp-4-converted/Night%20of%20the%20Living%20Dead%201968%20Horror%20Mystery%20480%20X%20640.mp4_converted.mp4"
        ),
        Contenido(10, "His Girl Friday", "Una reportera intenta abandonar el periodismo, pero su editor y exesposo la involucra en la noticia del año.", "1940-01-18", 7.8, listOf("Comedia", "Romance"), "película completa", R.drawable.poster_library, "https://archive.org/download/HisGirlFriday1940_201711/His%20Girl%20Friday%20%281940%29.mp4"),
        Contenido(11, "Charade", "Una mujer perseguida en París intenta descubrir quién puede ayudarla y quién busca una fortuna desaparecida.", "1963-12-05", 7.9, listOf("Misterio", "Romance"), "película completa", R.drawable.poster_fantasy, "https://archive.org/download/charade-1963-cary-grant-audrey-hepburn-comedy-mystery-romance-thriller-full-movie/Charade_READY.mp4"),
        Contenido(12, "The Last Man on Earth", "El último superviviente de una epidemia mundial se enfrenta cada noche a criaturas infectadas.", "1964-03-08", 6.7, listOf("Terror", "Ciencia ficción"), "película completa", R.drawable.poster_arctic, "https://archive.org/download/TheLastManOnEarth1964_201808/The%20Last%20Man%20on%20Earth%20%281964%29.mp4")
        )
    )
    val catalogo = _catalogo.asStateFlow()

    private val _miLista = MutableStateFlow<List<Contenido>>(emptyList())
    val miLista = _miLista.asStateFlow()
    val contenidos = miLista

    private val _titulo = MutableStateFlow("")
    val titulo = _titulo.asStateFlow()

    private val _esError = MutableStateFlow(false)
    val esError = _esError.asStateFlow()

    fun cambiarTitulo(valor: String) {
        _titulo.value = valor
        if (valor.isNotBlank()) _esError.value = false
    }

    fun guardar(): Boolean {
        val tituloLimpio = _titulo.value.trim()
        if (tituloLimpio.isBlank()) {
            _esError.value = true
            return false
        }
        val nuevoId = (_catalogo.value.maxOfOrNull { it.id } ?: 0) + 1
        val nuevoContenido = Contenido(
            id = nuevoId,
            titulo = tituloLimpio,
            sinopsis = "Contenido agregado manualmente para practicar estados en Compose.",
            fechaLanzamiento = "Pendiente",
            puntuacion = 0.0,
            generos = listOf("Sin género"),
            tipo = "película",
            posterRes = R.drawable.poster_desert
        )
        _catalogo.value = _catalogo.value + nuevoContenido
        _titulo.value = ""
        _esError.value = false
        return true
    }

    fun agregarAMiLista(contenido: Contenido) {
        val yaExiste = _miLista.value.any { it.id == contenido.id && it.tipo == contenido.tipo }
        if (!yaExiste) _miLista.value = _miLista.value + contenido
    }

    fun quitarDeMiLista(contenido: Contenido) {
        _miLista.value = _miLista.value.filterNot { it.id == contenido.id && it.tipo == contenido.tipo }
    }

    fun alternarGuardado(contenido: Contenido, guardado: Boolean) {
        if (guardado) quitarDeMiLista(contenido) else agregarAMiLista(contenido)
    }

    fun quitar(contenido: Contenido) = quitarDeMiLista(contenido)
}
