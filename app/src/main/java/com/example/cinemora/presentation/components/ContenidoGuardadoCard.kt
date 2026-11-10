package com.example.cinemora.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinemora.data.model.Contenido

@Composable
fun ContenidoGuardadoCard(item: Contenido, onAbrir: () -> Unit, onQuitar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onAbrir),
        colors = CardDefaults.cardColors(Tarjeta),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.fillMaxWidth().height(122.dp).padding(10.dp)) {
            Poster(
                item,
                Modifier.width(72.dp).fillMaxHeight()
                    .background(Color(0xFF2B2838), RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text("${item.anio} · ${item.tipo}", color = TextoSecundario, fontSize = 9.sp)
                Text(
                    item.titulo,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    item.sinopsis,
                    color = Color(0xFFCCC5D1),
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Ver detalle ›",
                        color = Color(0xFFE3B8FF),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onQuitar, contentPadding = PaddingValues(horizontal = 8.dp)) {
                        Icon(Icons.Default.BookmarkRemove, null, modifier = Modifier.size(15.dp))
                        Text(" Quitar", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
