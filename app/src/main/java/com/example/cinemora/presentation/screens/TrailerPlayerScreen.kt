package com.example.cinemora.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinemora.data.repository.Contenido
import com.example.cinemora.presentation.viewmodel.TrailerPlayerViewModel
import com.example.cinemora.presentation.viewmodel.TrailerUiState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun TrailerPlayerScreen(
    contenido: Contenido?,
    onBack: () -> Unit,
    viewModel: TrailerPlayerViewModel = viewModel()
) {
    BackHandler(onBack = onBack)
    if (contenido == null) {
        PantallaTrailerMensaje("Contenido no encontrado", onBack)
        return
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(contenido.id, contenido.tipo) { viewModel.cargar(contenido) }
    Column(Modifier.fillMaxSize().background(Color(0xFF08080C))) {
        IconButton(onClick = onBack, modifier = Modifier.padding(8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
        }
        Text(
            "Tráiler: ${viewModel.titulo(contenido)}",
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        when (val estado = uiState) {
            TrailerUiState.Cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is TrailerUiState.Disponible -> YouTubeTrailer(videoId = estado.videoId)
            TrailerUiState.NoDisponible -> PantallaTrailerMensaje("TMDB no tiene un tráiler de YouTube para este título.", onBack)
            is TrailerUiState.Error -> PantallaTrailerMensaje(estado.mensaje, onBack) {
                viewModel.cargar(contenido)
            }
        }
    }
}

@Composable
private fun YouTubeTrailer(videoId: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val playerView = remember(videoId) {
        YouTubePlayerView(context).apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        }
    }
    DisposableEffect(playerView, lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(playerView)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(playerView)
            playerView.release()
        }
    }
    AndroidView(factory = { playerView }, modifier = Modifier.fillMaxSize())
}

@Composable
private fun PantallaTrailerMensaje(
    mensaje: String,
    onBack: () -> Unit,
    onReintentar: (() -> Unit)? = null
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text(mensaje, color = Color.White)
            if (onReintentar != null) {
                Button(onClick = onReintentar, modifier = Modifier.padding(top = 14.dp)) {
                    Icon(Icons.Default.Refresh, null)
                    Text(" Reintentar")
                }
            }
        }
    }
}
