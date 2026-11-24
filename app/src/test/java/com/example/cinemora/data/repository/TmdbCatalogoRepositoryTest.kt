package com.example.cinemora.data.repository

import com.example.cinemora.data.api.TmdbApi
import com.example.cinemora.data.dto.TmdbTrendingResponseDto
import com.example.cinemora.data.dto.TmdbVideosResponseDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class TmdbCatalogoRepositoryTest {
    @Test
    fun publicaErrorCuandoFallaLaConexion() = runBlocking {
        val repository = TmdbCatalogoRepository(
            api = ApiSinConexion,
            tokenConfigurado = { true }
        )

        val resultado = repository.obtenerCatalogo()

        assertTrue(resultado is ResultadoCatalogo.Error)
        assertTrue((resultado as ResultadoCatalogo.Error).mensaje.contains("conectar"))
    }

    private object ApiSinConexion : TmdbApi {
        override suspend fun obtenerTendencias(language: String): Response<TmdbTrendingResponseDto> =
            throw IOException("Sin red")

        override suspend fun obtenerVideosDePelicula(
            id: Int,
            language: String
        ): Response<TmdbVideosResponseDto> = throw IOException("Sin red")

        override suspend fun obtenerVideosDeSerie(
            id: Int,
            language: String
        ): Response<TmdbVideosResponseDto> = throw IOException("Sin red")
    }
}
