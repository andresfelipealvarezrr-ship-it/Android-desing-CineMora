package com.example.cinemora.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cinemora.model.Contenido
import com.example.cinemora.ui.components.Fondo
import com.example.cinemora.ui.components.Morado
import com.example.cinemora.ui.components.Poster
import com.example.cinemora.ui.components.Tarjeta
import com.example.cinemora.ui.components.TextoSecundario
import com.example.cinemora.viewmodel.DetalleViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(item: Contenido?, viewModel: DetalleViewModel, onPlay: () -> Unit, onTrailer: () -> Unit, onBack: () -> Unit) {
    val context = LocalContext.current
    val lista by viewModel.miLista.collectAsStateWithLifecycle(); val guardado = item in lista
    val snackbar = remember { SnackbarHostState() }; val scope = rememberCoroutineScope()
    Scaffold(containerColor = Fondo, snackbarHost = { SnackbarHost(snackbar) }, topBar = {
        TopAppBar(title = { Text("Detalle", color = Color.White) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = Color.White) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Fondo))
    }) { padding ->
        if (item == null) Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Contenido no encontrado", color = Color.White) }
        else Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(Modifier.fillMaxWidth(.72f).height(300.dp), shape = RoundedCornerShape(16.dp)) { Poster(item, Modifier.fillMaxSize()) }
            Spacer(Modifier.height(16.dp)); Text(item.titulo, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.ExtraBold)
            Text("${item.fechaLanzamiento.take(4)}   •   ${item.generos.firstOrNull() ?: item.tipo}   •   ⭐ ${item.puntuacion}", color = Color(0xFFCBC4CF), style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(14.dp))
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color(0xFF3A1D4A)), shape = RoundedCornerShape(12.dp)) {
                Row(Modifier.fillMaxWidth().padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFF5A2B75), shape = RoundedCornerShape(9.dp)) { Icon(Icons.Default.CollectionsBookmark, null, tint = Color(0xFFE4B8FF), modifier = Modifier.padding(9.dp)) }
                    Spacer(Modifier.width(10.dp)); Column { Text("En tu cinemateca", color = Color.White, fontWeight = FontWeight.Bold); Text("Este título ya forma parte de tu colección personal.", color = Color(0xFFD0BED8), style = MaterialTheme.typography.labelSmall) }
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailFact("🎬", "Dirección", "CineMora", Modifier.weight(1f))
                DetailFact("◉", "Audio", "Original", Modifier.weight(1f))
                DetailFact("◎", "Clasif.", "+13 años", Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(12.dp)) { Column(Modifier.padding(15.dp)) { Text("▤  Sinopsis", color = Color(0xFFE2B7FF), fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp)); Text(item.sinopsis, color = Color(0xFFD5CED9), style = MaterialTheme.typography.bodyMedium) } }
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) { item.generos.take(3).forEach { AssistChip(onClick = {}, label = { Text(it, style = MaterialTheme.typography.labelSmall) }) } }
            if (item.videoUrl != null) {
                Button(onClick = onPlay, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(Morado)) { Icon(Icons.Default.PlayArrow, null); Spacer(Modifier.width(8.dp)); Text("Reproducir película") }
                Text("Película completa · Dominio público", color = TextoSecundario, style = MaterialTheme.typography.labelSmall)
                Spacer(Modifier.height(8.dp))
            }
            Button(onClick = { viewModel.alternarGuardado(item, guardado); scope.launch { snackbar.showSnackbar(if (guardado) "Eliminado de Mi lista" else "Agregado a Mi lista") } }, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(if (guardado) Color(0xFF2B2838) else Morado), shape = RoundedCornerShape(12.dp)) {
                Icon(if (guardado) Icons.Default.BookmarkRemove else Icons.Default.Bookmark, null); Spacer(Modifier.width(8.dp)); Text(if (guardado) "Quitar de Mi lista" else "Agregar a Mi lista")
            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = onTrailer, enabled = item.trailerYoutubeId != null || item.tmdbId != null, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.PlayCircle, null); Spacer(Modifier.width(6.dp)); Text(if (item.trailerYoutubeId != null || item.tmdbId != null) "Ver tráiler" else "Sin tráiler")
                }
                OutlinedButton(onClick = {
                    val query = URLEncoder.encode(item.titulo, StandardCharsets.UTF_8.toString())
                    context.startActivity(Intent(Intent.ACTION_VIEW, "https://www.justwatch.com/co/buscar?q=$query".toUri()))
                }, modifier = Modifier.weight(1f)) { Icon(Icons.AutoMirrored.Filled.OpenInNew, null); Spacer(Modifier.width(6.dp)); Text("Dónde ver") }
            }
        }
    }
}

@Composable
private fun DetailFact(icon: String, label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(10.dp)) {
        Column(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, color = Color(0xFFE2B7FF)); Text(label, color = TextoSecundario, style = MaterialTheme.typography.labelSmall); Text(value, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
        }
    }
}
