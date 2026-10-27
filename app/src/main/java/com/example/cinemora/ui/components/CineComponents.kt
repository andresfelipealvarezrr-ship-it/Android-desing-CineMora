package com.example.cinemora.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cinemora.model.Contenido
import coil3.compose.AsyncImage

val Fondo = Color(0xFF131318)
val Tarjeta = Color(0xFF1B1B20)
val Morado = Color(0xFF9D4EDD)
val TextoSecundario = Color(0xFF9E9DA6)

@Composable
fun BrandHeader() {
    Row(
        Modifier.fillMaxWidth().height(58.dp).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = Color(0xFF24152F), shape = RoundedCornerShape(8.dp), modifier = Modifier.size(30.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.MovieFilter, null, tint = Morado, modifier = Modifier.size(18.dp)) }
        }
        Spacer(Modifier.width(10.dp))
        Text("CineMora", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        Surface(color = Color(0xFFDDB5FF), shape = CircleShape, modifier = Modifier.size(32.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, "Perfil", tint = Color(0xFF4A1768), modifier = Modifier.size(18.dp)) }
        }
    }
}

@Composable
fun Titulo(titulo: String, subtitulo: String) {
    Text(titulo, style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
    Text(subtitulo, color = Color(0xFFCBC4CF))
}

@Composable
fun Poster(item: Contenido, modifier: Modifier = Modifier) {
    if (item.posterUrl != null) {
        AsyncImage(
            model = item.posterUrl,
            contentDescription = "Póster de ${item.titulo}",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else if (item.posterRes != 0) {
        Image(
            painter = painterResource(item.posterRes),
            contentDescription = "Póster de ${item.titulo}",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        Box(modifier.background(Color(0xFF2B2838)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Movie, null, tint = Morado, modifier = Modifier.size(52.dp))
        }
    }
}
