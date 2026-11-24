package com.example.cinemora.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cinemora.presentation.components.BrandHeader
import com.example.cinemora.presentation.components.Tarjeta
import com.example.cinemora.presentation.components.TextoSecundario
import com.example.cinemora.presentation.viewmodel.AgregarContenidoViewModel

@Composable
fun AgregarContenidoScreen(viewModel: AgregarContenidoViewModel) {
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize()) {
        BrandHeader()
        Column(Modifier.padding(14.dp)) {
            Text("Agregar contenido", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
            Spacer(Modifier.height(8.dp))
            Text("Alcance actual del catálogo", color = Color(0xFFE3C8F5))
            Spacer(Modifier.height(14.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(Tarjeta),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFFE4B8FF))
                    Spacer(Modifier.height(10.dp))
                    Text(mensaje, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "No se pueden crear películas o series en TMDB desde CineMora. Para guardar una creación manual de forma real se necesitaría definir una fuente propia (por ejemplo, Room o un backend), que no se incorpora en esta entrega.",
                        color = TextoSecundario
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Para armar tu selección temporal usa “Agregar a Mi lista” desde el detalle de un título de TMDB.",
                        color = Color(0xFFE3C8F5)
                    )
                }
            }
        }
    }
}
