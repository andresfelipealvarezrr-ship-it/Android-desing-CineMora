package com.example.cinemora.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
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
import com.example.cinemora.model.Contenido
import com.example.cinemora.ui.components.*
import com.example.cinemora.viewmodel.HomeViewModel
import kotlin.math.round

@Composable
fun HomeScreen(viewModel: HomeViewModel, onAbrir: (Contenido) -> Unit) {
    val catalogo by viewModel.catalogo.collectAsStateWithLifecycle()
    val lista by viewModel.miLista.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        BrandHeader()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 18.dp)
        ) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Column {
                    Surface(color = Color(0xFF211A29), shape = RoundedCornerShape(14.dp)) {
                        Column(Modifier.padding(14.dp)) {
                            Surface(color = Color(0xFF36233E), shape = RoundedCornerShape(20.dp)) {
                                Text("◆ CINEMATECA PERSONAL", color = Color(0xFFE8C8FF), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp))
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("CineMora", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
                            Text("Tu próxima historia empieza aquí", color = Color(0xFFE3C8F5), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("Colecciona instantes, revive clásicos y descubre nuevas perspectivas en tu archivo fílmico.", color = TextoSecundario, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFFE4B8FF), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Explora el catálogo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.weight(1f))
                        Surface(color = Tarjeta, shape = RoundedCornerShape(18.dp)) { Text("${catalogo.size} títulos", color = Color(0xFFDDB5FF), fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) }
                    }
                }
            }
            items(catalogo, key = { "${it.tipo}-${it.id}" }) { item ->
                Card(Modifier.fillMaxWidth().clickable { onAbrir(item) }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(Tarjeta)) {
                    Column {
                        Box(Modifier.fillMaxWidth().height(218.dp).background(Color(0xFF2B2838))) {
                            Poster(item, Modifier.fillMaxSize())
                            Surface(color = Color(0xD91A171F), shape = RoundedCornerShape(12.dp), modifier = Modifier.align(Alignment.BottomStart).padding(7.dp)) {
                                Row(Modifier.padding(horizontal = 7.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFE8B9FF), modifier = Modifier.size(12.dp))
                                    Text(" ${round(item.puntuacion * 10) / 10}", color = Color.White, fontSize = 10.sp)
                                }
                            }
                            if (item in lista) Surface(color = Morado, shape = RoundedCornerShape(bottomStart = 9.dp), modifier = Modifier.align(Alignment.TopEnd)) {
                                Row(Modifier.padding(horizontal = 7.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Bookmark, null, tint = Color.White, modifier = Modifier.size(12.dp)); Text(" En lista", color = Color.White, fontSize = 9.sp)
                                }
                            }
                        }
                        Column(Modifier.padding(10.dp)) {
                            Text(item.titulo, color = Color.White, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp)
                            Text("${item.fechaLanzamiento.take(4)} • ${item.generos.firstOrNull() ?: item.tipo}", color = TextoSecundario, maxLines = 1, fontSize = 10.sp)
                            Text(item.tipo.replaceFirstChar { it.uppercase() }, color = Color(0xFFDDB5FF), maxLines = 1, fontSize = 9.sp)
                        }
                    }
                }
            }
        }
    }
}
