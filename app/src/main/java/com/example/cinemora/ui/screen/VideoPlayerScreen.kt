package com.example.cinemora.ui.screen

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinemora.model.Contenido
import com.example.cinemora.viewmodel.VideoPlayerViewModel

@Composable
fun VideoPlayerScreen(contenido: Contenido?, onBack: () -> Unit, viewModel: VideoPlayerViewModel = viewModel()) {
    val context = LocalContext.current
    val rootView = LocalView.current
    val errorMessage by viewModel.error.collectAsStateWithLifecycle()
    val player = remember(contenido?.videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            contenido?.videoUrl?.let { setMediaItem(MediaItem.fromUri(it)) }
            prepare()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    viewModel.informarError()
                }
            })
        }
    }

    DisposableEffect(player, rootView) {
        rootView.keepScreenOn = true
        onDispose {
            rootView.keepScreenOn = false
            player.release()
        }
    }
    BackHandler(onBack = onBack)

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF08080C)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.Start).padding(8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
        }
        Text(contenido?.titulo ?: "Reproductor", color = Color.White, style = MaterialTheme.typography.titleLarge)
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = player
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                    useController = true
                }
            },
            modifier = Modifier.weight(1f).fillMaxSize()
        )
        if (errorMessage != null) {
            Text(errorMessage!!, color = Color(0xFFFFB4AB), modifier = Modifier.padding(12.dp))
        } else {
            Text("Película completa · Dominio público · La carga inicial puede tardar unos segundos", color = Color(0xFFB9B2C0), modifier = Modifier.padding(12.dp))
        }
    }
}
