package com.example.cinemora.presentation.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cinemora.data.repository.Contenido
import com.example.cinemora.presentation.components.Fondo
import com.example.cinemora.presentation.components.Morado
import com.example.cinemora.presentation.components.Poster
import com.example.cinemora.presentation.components.Tarjeta
import com.example.cinemora.presentation.components.TextoSecundario
import com.example.cinemora.presentation.viewmodel.DetalleViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    item: Contenido?,
    viewModel: DetalleViewModel,
    onTrailer: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lista by viewModel.miLista.collectAsStateWithLifecycle()
    val guardado = item in lista
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Fondo,
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Detalle", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Fondo)
            )
        }
    ) { padding ->
        if (item == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Contenido no disponible. Vuelve a Inicio y carga el catálogo.", color = Color.White)
            }
        } else {
            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(Modifier.fillMaxWidth(.72f).height(300.dp), shape = RoundedCornerShape(16.dp)) {
                    Poster(item, Modifier.fillMaxSize())
                }
                Spacer(Modifier.height(16.dp))
                Text(item.titulo, color = Color.White, fontWeight = FontWeight.ExtraBold)
                Text(
                    "${item.fechaLanzamiento.take(4)} • ${if (item.tipo == "tv") "Serie" else "Película"} • ⭐ ${item.puntuacion}",
                    color = Color(0xFFCBC4CF)
                )
                Spacer(Modifier.height(14.dp))
                Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(15.dp)) {
                        Text("Sinopsis", color = Color(0xFFE2B7FF), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(item.sinopsis, color = Color(0xFFD5CED9))
                    }
                }
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    item.generos.take(3).forEach { genero ->
                        AssistChip(onClick = {}, label = { Text(genero) })
                    }
                }
                Button(
                    onClick = {
                        viewModel.alternarGuardado(item, guardado)
                        scope.launch {
                            snackbar.showSnackbar(
                                if (guardado) "Eliminado de Mi lista temporal"
                                else "Agregado a Mi lista temporal"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(if (guardado) Color(0xFF2B2838) else Morado),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(if (guardado) Icons.Default.BookmarkRemove else Icons.Default.Bookmark, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (guardado) "Quitar de Mi lista" else "Agregar a Mi lista")
                }
                Text(
                    "La lista solo vive durante esta sesión; no modifica TMDB.",
                    color = TextoSecundario,
                    modifier = Modifier.padding(top = 5.dp)
                )
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = onTrailer, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.PlayCircle, null)
                        Spacer(Modifier.width(6.dp))
                        Text("Ver tráiler")
                    }
                    OutlinedButton(
                        onClick = {
                            val query = URLEncoder.encode(item.titulo, StandardCharsets.UTF_8.toString())
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, "https://www.justwatch.com/co/buscar?q=$query".toUri())
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.OpenInNew, null)
                        Spacer(Modifier.width(6.dp))
                        Text("Dónde ver")
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "CineMora no ofrece reproducción completa: TMDB aporta metadatos y tráileres, no URLs de streaming.",
                    color = TextoSecundario
                )
            }
        }
    }
}
