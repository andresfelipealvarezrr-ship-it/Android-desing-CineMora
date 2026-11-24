package com.example.cinemora.data.repository

import com.example.cinemora.data.api.TmdbApi
import com.example.cinemora.data.dto.TmdbContenidoDto
import com.example.cinemora.data.dto.TmdbTrendingResponseDto
import com.example.cinemora.data.dto.TmdbVideoDto
import com.example.cinemora.data.network.TmdbNetwork
import java.io.IOException

/** Implementación que obtiene únicamente datos reales de TMDB. */
class TmdbCatalogoRepository(
    private val api: TmdbApi = TmdbNetwork.api,
    private val tokenConfigurado: () -> Boolean = TmdbNetwork::tieneToken
) : CatalogoRepository {
    private var ultimoCatalogo: List<Contenido> = emptyList()

    override suspend fun obtenerCatalogo(): ResultadoCatalogo {
        if (!tokenConfigurado()) {
            return ResultadoCatalogo.Error(
                "Falta la credencial de TMDB. Configura TMDB_ACCESS_TOKEN en local.properties."
            )
        }

        return try {
            val response = api.obtenerTendencias()
            if (!response.isSuccessful) {
                return ResultadoCatalogo.Error(mensajeHttp(response.code()))
            }

            val contenidos = response.body().orEmpty()
                .mapNotNull(TmdbContenidoDto::aContenido)

            ultimoCatalogo = contenidos
            if (contenidos.isEmpty()) ResultadoCatalogo.Vacio
            else ResultadoCatalogo.Exito(contenidos)
        } catch (_: IOException) {
            ResultadoCatalogo.Error("No se pudo conectar con TMDB. Revisa tu conexión e inténtalo de nuevo.")
        } catch (_: Exception) {
            ResultadoCatalogo.Error("No se pudo interpretar la respuesta de TMDB. Inténtalo nuevamente.")
        }
    }

    override suspend fun obtenerTrailer(contenido: Contenido): ResultadoTrailer {
        if (!tokenConfigurado()) {
            return ResultadoTrailer.Error("Falta la credencial de TMDB para consultar el tráiler.")
        }
        return try {
            val respuesta = when (contenido.tipo) {
                "movie" -> api.obtenerVideosDePelicula(contenido.id)
                "tv" -> api.obtenerVideosDeSerie(contenido.id)
                else -> return ResultadoTrailer.NoDisponible
            }
            if (!respuesta.isSuccessful) return ResultadoTrailer.Error(mensajeHttp(respuesta.code()))
            val videos = respuesta.body()?.results.orEmpty()
            val video = videos.primeroReproducible()
            if (video == null) ResultadoTrailer.NoDisponible
            else ResultadoTrailer.Disponible(video.key)
        } catch (_: IOException) {
            ResultadoTrailer.Error("No se pudo conectar con TMDB para buscar el tráiler.")
        } catch (_: Exception) {
            ResultadoTrailer.Error("No se pudo cargar el tráiler de TMDB.")
        }
    }

    override fun buscarContenido(id: Int, tipo: String): Contenido? =
        ultimoCatalogo.firstOrNull { it.id == id && it.tipo == tipo }

    private fun mensajeHttp(codigo: Int): String = when (codigo) {
        401, 403 -> "TMDB rechazó la credencial. Revisa TMDB_ACCESS_TOKEN en local.properties."
        429 -> "TMDB limitó temporalmente las consultas. Espera un momento y reintenta."
        else -> "TMDB respondió con un error ($codigo). Inténtalo nuevamente."
    }
}

private fun TmdbContenidoDto.aContenido(): Contenido? {
    val tipoRemoto = mediaType?.takeIf { it == "movie" || it == "tv" } ?: return null
    val titulo = if (tipoRemoto == "movie") title else name
    if (titulo.isNullOrBlank()) return null

    return Contenido(
        id = id,
        titulo = titulo,
        sinopsis = overview?.takeIf(String::isNotBlank) ?: "Sinopsis no disponible.",
        fechaLanzamiento = (if (tipoRemoto == "movie") releaseDate else firstAirDate).orEmpty(),
        puntuacion = voteAverage,
        generos = genreIds.mapNotNull(::nombreGenero).ifEmpty { listOf("Sin género") },
        tipo = tipoRemoto,
        posterUrl = posterPath
            ?.takeIf { it.isNotBlank() }
            ?.let { "https://image.tmdb.org/t/p/w500$it" }
    )
}

private fun List<TmdbVideoDto>.primeroReproducible(): TmdbVideoDto? {
    val youtube = filter { it.site.equals("YouTube", ignoreCase = true) }
    return youtube.firstOrNull { it.type == "Trailer" && it.official }
        ?: youtube.firstOrNull { it.type == "Trailer" }
        ?: youtube.firstOrNull()
}

private fun TmdbTrendingResponseDto?.orEmpty() = this?.results.orEmpty()

private fun nombreGenero(id: Int): String? = mapOf(
    28 to "Acción", 12 to "Aventura", 16 to "Animación", 35 to "Comedia",
    80 to "Crimen", 99 to "Documental", 18 to "Drama", 10751 to "Familia",
    14 to "Fantasía", 36 to "Historia", 27 to "Terror", 10402 to "Música",
    9648 to "Misterio", 10749 to "Romance", 878 to "Ciencia ficción",
    53 to "Suspenso", 10752 to "Bélica", 37 to "Western",
    10759 to "Acción y aventura", 10765 to "Ciencia ficción y fantasía"
)[id]
