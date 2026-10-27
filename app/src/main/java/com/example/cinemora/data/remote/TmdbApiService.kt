package com.example.cinemora.data.remote

import com.example.cinemora.BuildConfig
import com.example.cinemora.data.remote.dto.TmdbContenidoDto
import com.example.cinemora.model.Contenido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object TmdbApiService {
    private const val BASE_URL = "https://api.themoviedb.org/3"
    private const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"

    suspend fun obtenerTendencias(): List<Contenido> = withContext(Dispatchers.IO) {
        val token = BuildConfig.TMDB_ACCESS_TOKEN.trim()
        if (token.isBlank()) return@withContext emptyList()

        runCatching {
            val connection = (URL("$BASE_URL/trending/all/day?language=es-CO").openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 10_000
                readTimeout = 10_000
                setRequestProperty("Accept", "application/json")
                setRequestProperty("Authorization", "Bearer $token")
            }
            try {
                if (connection.responseCode !in 200..299) return@runCatching emptyList()
                val json = connection.inputStream.bufferedReader().use { it.readText() }
                val resultados = JSONObject(json).getJSONArray("results")
                buildList {
                    for (index in 0 until resultados.length()) {
                        val item = resultados.getJSONObject(index)
                        val dto = item.toDto() ?: continue
                        add(dto.toContenido())
                    }
                }
            } finally {
                connection.disconnect()
            }
        }.getOrDefault(emptyList())
    }

    suspend fun enriquecerContenido(contenido: Contenido): Contenido = withContext(Dispatchers.IO) {
        val token = BuildConfig.TMDB_ACCESS_TOKEN.trim()
        if (token.isBlank()) return@withContext contenido
        runCatching {
            val tipoTmdb = if (contenido.tipo.contains("serie", ignoreCase = true)) "tv" else "movie"
            val query = URLEncoder.encode(contenido.titulo, StandardCharsets.UTF_8.toString())
            val resultados = solicitar("/search/$tipoTmdb?query=$query&language=es-CO").getJSONArray("results")
            if (resultados.length() == 0) return@runCatching contenido
            val exacto = (0 until resultados.length())
                .map { resultados.getJSONObject(it) }
                .firstOrNull {
                    val titulo = if (tipoTmdb == "movie") it.optString("title") else it.optString("name")
                    titulo.equals(contenido.titulo, ignoreCase = true)
                } ?: resultados.getJSONObject(0)
            val tmdbId = exacto.getInt("id")
            val posterPath = exacto.optString("poster_path").takeIf { it.isNotBlank() && it != "null" }
            contenido.copy(
                posterUrl = posterPath?.let { "$IMAGE_URL$it" } ?: contenido.posterUrl,
                trailerYoutubeId = contenido.trailerYoutubeId ?: obtenerTrailerInterno(tmdbId, tipoTmdb),
                tmdbId = tmdbId
            )
        }.getOrDefault(contenido)
    }

    suspend fun obtenerTrailer(tmdbId: Int, tipo: String): String? = withContext(Dispatchers.IO) {
        val tipoTmdb = if (tipo.contains("serie", ignoreCase = true)) "tv" else "movie"
        runCatching { obtenerTrailerInterno(tmdbId, tipoTmdb) }.getOrNull()
    }

    private fun obtenerTrailerInterno(tmdbId: Int, tipoTmdb: String): String? {
        val videosEs = solicitar("/$tipoTmdb/$tmdbId/videos?language=es-CO").getJSONArray("results")
        val videos = if (videosEs.length() > 0) videosEs
        else solicitar("/$tipoTmdb/$tmdbId/videos?language=en-US").getJSONArray("results")
        val candidatos = (0 until videos.length()).map { videos.getJSONObject(it) }
            .filter { it.optString("site") == "YouTube" }
        return candidatos.firstOrNull { it.optString("type") == "Trailer" && it.optBoolean("official") }
            ?.optString("key")
            ?: candidatos.firstOrNull { it.optString("type") == "Trailer" }?.optString("key")
            ?: candidatos.firstOrNull()?.optString("key")
    }

    private fun solicitar(path: String): JSONObject {
        val connection = (URL("$BASE_URL$path").openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10_000
            readTimeout = 10_000
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Authorization", "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN.trim()}")
        }
        return try {
            if (connection.responseCode !in 200..299) error("TMDB respondió ${connection.responseCode}")
            JSONObject(connection.inputStream.bufferedReader().use { it.readText() })
        } finally {
            connection.disconnect()
        }
    }

    private fun JSONObject.toDto(): TmdbContenidoDto? {
        val mediaType = optString("media_type")
        if (mediaType != "movie" && mediaType != "tv") return null
        val title = if (mediaType == "movie") optString("title") else optString("name")
        if (title.isBlank()) return null
        val genreArray = optJSONArray("genre_ids")
        val genres = buildList {
            if (genreArray != null) for (index in 0 until genreArray.length()) add(genreArray.getInt(index))
        }
        return TmdbContenidoDto(
            id = getInt("id"),
            titulo = title,
            sinopsis = optString("overview").ifBlank { "Sinopsis no disponible en español." },
            fecha = if (mediaType == "movie") optString("release_date") else optString("first_air_date"),
            puntuacion = optDouble("vote_average", 0.0),
            generos = genres,
            tipo = if (mediaType == "movie") "película" else "serie",
            posterPath = optString("poster_path").takeIf { it.isNotBlank() && it != "null" }
        )
    }

    private fun TmdbContenidoDto.toContenido() = Contenido(
        id = id,
        titulo = titulo,
        sinopsis = sinopsis,
        fechaLanzamiento = fecha.ifBlank { "Sin fecha" },
        puntuacion = puntuacion,
        generos = generos.mapNotNull(::nombreGenero).ifEmpty { listOf("Sin género") },
        tipo = tipo,
        posterRes = 0,
        posterUrl = posterPath?.let { "$IMAGE_URL$it" },
        tmdbId = id
    )

    private fun nombreGenero(id: Int): String? = mapOf(
        28 to "Acción", 12 to "Aventura", 16 to "Animación", 35 to "Comedia",
        80 to "Crimen", 99 to "Documental", 18 to "Drama", 10751 to "Familia",
        14 to "Fantasía", 36 to "Historia", 27 to "Terror", 10402 to "Música",
        9648 to "Misterio", 10749 to "Romance", 878 to "Ciencia ficción",
        53 to "Suspenso", 10752 to "Bélica", 37 to "Western",
        10759 to "Acción y aventura", 10765 to "Ciencia ficción y fantasía"
    )[id]
}
