package com.example.cinemora.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatCard(etiqueta: String, cantidad: Int, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = Tarjeta, shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(cantidad.toString(), color = Color.White, style = MaterialTheme.typography.titleLarge)
            Text(etiqueta, color = TextoSecundario, style = MaterialTheme.typography.labelMedium)
        }
    }
}
