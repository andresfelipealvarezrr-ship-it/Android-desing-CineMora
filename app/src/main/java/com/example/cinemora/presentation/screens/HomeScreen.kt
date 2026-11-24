package com.example.cinemora.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cinemora.data.repository.Contenido
import com.example.cinemora.presentation.components.BrandHeader
import com.example.cinemora.presentation.components.Morado
import com.example.cinemora.presentation.components.Poster
import com.example.cinemora.presentation.components.Tarjeta
import com.example.cinemora.presentation.components.TextoSecundario
import com.example.cinemora.presentation.viewmodel.HomeUiState
import com.example.cinemora.presentation.viewmodel.HomeViewModel
import kotlin.math.round

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAbrir: (Contenido) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lista by viewModel.miLista.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        BrandHeader()
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onNavigateToProfile) {
                Icon(Icons.Default.Person, null, tint = Morado)
                Spacer(Modifier.width(6.dp))
                Text("Ver perfil", color = Color(0xFFDDB5FF))
            }
        }

        when (val estado = uiState) {
            HomeUiState.Cargando -> EstadoCargando()
            HomeUiState.Vacio -> EstadoVacio(onReintentar = viewModel::cargarCatalogo)
            is HomeUiState.Error -> EstadoError(
                mensaje = estado.mensaje,
                onReintentar = viewModel::cargarCatalogo
            )
            is HomeUiState.Exito -> CatalogoGrid(
                catalogo = estado.contenidos,
                lista = lista,
                onAbrir = onAbrir
            )
        }
    }
}

@Composable
private fun EstadoCargando() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Morado)
            Spacer(Modifier.height(14.dp))
            Text("Consultando el catálogo de TMDB…", color = Color.White)
        }
    }
}

@Composable
private fun EstadoVacio(onReintentar: () -> Unit) {
    EstadoInformativo(
        titulo = "TMDB no devolvió títulos",
        mensaje = "La respuesta fue correcta, pero no contiene películas ni series para mostrar.",
        onReintentar = onReintentar
    )
}

@Composable
private fun EstadoError(mensaje: String, onReintentar: () -> Unit) {
    EstadoInformativo(
        titulo = "No pudimos cargar Inicio",
        mensaje = mensaje,
        onReintentar = onReintentar
    )
}

@Composable
private fun EstadoInformativo(titulo: String, mensaje: String, onReintentar: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Card(colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(16.dp)) {
            Column(
                Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(titulo, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(mensaje, color = TextoSecundario, fontSize = 13.sp)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onReintentar, colors = ButtonDefaults.buttonColors(Morado)) {
                    Icon(Icons.Default.Refresh, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Reintentar")
                }
            }
        }
    }
}

@Composable
private fun CatalogoGrid(
    catalogo: List<Contenido>,
    lista: List<Contenido>,
    onAbrir: (Contenido) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 18.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Column {
                Surface(color = Color(0xFF211A29), shape = RoundedCornerShape(14.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            "◆ CATÁLOGO EN VIVO · TMDB",
                            color = Color(0xFFE8C8FF),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("CineMora", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
                        Text(
                            "Películas y series recibidas desde TMDB",
                            color = Color(0xFFE3C8F5),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFFE4B8FF), modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Explora el catálogo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.weight(1f))
                    Surface(color = Tarjeta, shape = RoundedCornerShape(18.dp)) {
                        Text(
                            "${catalogo.size} títulos",
                            color = Color(0xFFDDB5FF),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
        items(catalogo, key = { "${it.tipo}-${it.id}" }) { item ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onAbrir(item) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(Tarjeta)
            ) {
                Column {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(218.dp).background(Color(0xFF2B2838))
                    ) {
                        Poster(item, Modifier.fillMaxSize())
                        Surface(
                            color = Color(0xD91A171F),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.BottomStart).padding(7.dp)
                        ) {
                            Row(
                                Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFE8B9FF), modifier = Modifier.size(12.dp))
                                Text(" ${round(item.puntuacion * 10) / 10}", color = Color.White, fontSize = 10.sp)
                            }
                        }
                        if (item in lista) {
                            Surface(
                                color = Morado,
                                shape = RoundedCornerShape(bottomStart = 9.dp),
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Row(
                                    Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Bookmark, null, tint = Color.White, modifier = Modifier.size(12.dp))
                                    Text(" En lista", color = Color.White, fontSize = 9.sp)
                                }
                            }
                        }
                    }
                    Column(Modifier.padding(10.dp)) {
                        Text(item.titulo, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp)
                        Text(
                            "${item.fechaLanzamiento.take(4)} • ${item.generos.firstOrNull() ?: item.tipo}",
                            color = TextoSecundario,
                            maxLines = 1,
                            fontSize = 10.sp
                        )
                        Text(
                            if (item.tipo == "tv") "Serie" else "Película",
                            color = Color(0xFFDDB5FF),
                            fontSize = 9.sp
                        )
                    }
                }
            }
        }
    }
}
