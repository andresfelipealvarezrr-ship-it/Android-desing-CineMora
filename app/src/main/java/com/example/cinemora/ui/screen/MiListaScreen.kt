package com.example.cinemora.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cinemora.model.Contenido
import com.example.cinemora.ui.components.*
import com.example.cinemora.viewmodel.MiListaViewModel

@Composable
fun MiListaScreen(viewModel: MiListaViewModel, onAbrir: (Contenido) -> Unit, onExplorar: () -> Unit) {
    val lista by viewModel.contenidos.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        BrandHeader()
        Column(Modifier.padding(horizontal = 14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column { Text("Mi lista", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 25.sp); Text("Tus historias guardadas", color = Color(0xFFE1B8FA), fontSize = 12.sp) }
                Spacer(Modifier.weight(1f)); Surface(color = Tarjeta, shape = CircleShape) { Icon(Icons.Default.FilterList, "Filtrar", tint = TextoSecundario, modifier = Modifier.padding(10.dp)) }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                FilterChip(true, { }, { Text("Todos (${lista.size})", fontSize = 10.sp) })
                FilterChip(false, { }, { Text("Películas", fontSize = 10.sp) })
                FilterChip(false, { }, { Text("Series", fontSize = 10.sp) })
            }
            Spacer(Modifier.height(12.dp))
        }
        if (lista.isEmpty()) {
            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)) {
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
                        Box(Modifier.size(104.dp).background(Color(0xFF39234A), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.VideoLibrary, null, tint = Color(0xFFE2B6FF), modifier = Modifier.size(50.dp)) }
                        Spacer(Modifier.height(22.dp)); Text("Todavía no has guardado\ncontenidos", color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 17.sp)
                        Spacer(Modifier.height(9.dp)); Text("Explora el catálogo y agrega tus historias y películas favoritas para tenerlas siempre a mano.", color = TextoSecundario, textAlign = TextAlign.Center, fontSize = 12.sp, lineHeight = 17.sp)
                        Spacer(Modifier.height(22.dp)); Button(onClick = onExplorar, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(Morado), shape = RoundedCornerShape(24.dp)) { Icon(Icons.Default.Explore, null); Spacer(Modifier.width(7.dp)); Text("Explorar catálogo", fontWeight = FontWeight.Bold) }
                        Spacer(Modifier.height(24.dp))
                    }
                }
                item { RecommendationCard() }
            }
        } else {
            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(lista, key = { "${it.tipo}-${it.id}" }) { item ->
                    Card(Modifier.fillMaxWidth().clickable { onAbrir(item) }, colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(12.dp)) {
                        Row(Modifier.fillMaxWidth().height(122.dp).padding(10.dp)) {
                            Poster(item, Modifier.width(72.dp).fillMaxHeight().background(Color(0xFF2B2838), RoundedCornerShape(8.dp)))
                            Spacer(Modifier.width(11.dp))
                            Column(Modifier.weight(1f)) {
                                Text("${item.fechaLanzamiento.take(4)} · ${item.tipo}", color = TextoSecundario, fontSize = 9.sp)
                                Text(item.titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text(item.sinopsis, color = Color(0xFFCCC5D1), fontSize = 10.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                Spacer(Modifier.weight(1f))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Ver detalle ›", color = Color(0xFFE3B8FF), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                    Spacer(Modifier.weight(1f))
                                    TextButton(onClick = { viewModel.quitar(item) }, contentPadding = PaddingValues(horizontal = 8.dp)) { Icon(Icons.Default.BookmarkRemove, null, modifier = Modifier.size(15.dp)); Text(" Quitar", fontSize = 10.sp) }
                                }
                            }
                        }
                    }
                }
                item { RecommendationCard() }
            }
        }
    }
}

@Composable
private fun RecommendationCard() {
    Card(colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.fillMaxWidth().padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = Color(0xFF3B2450), shape = CircleShape) { Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFFE1B5FF), modifier = Modifier.padding(10.dp)) }
            Spacer(Modifier.width(10.dp)); Column(Modifier.weight(1f)) { Text("Recomendación basada en tu lista", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp); Text("Prueba historias con atmósferas parecidas.", color = TextoSecundario, fontSize = 10.sp) }
            Text("Explorar", color = Color(0xFFE1B5FF), fontSize = 10.sp)
        }
    }
}
