package com.example.cinemora.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cinemora.data.model.Contenido
import com.example.cinemora.presentation.components.BrandHeader
import com.example.cinemora.presentation.components.Morado
import com.example.cinemora.presentation.components.Tarjeta
import com.example.cinemora.presentation.components.Titulo
import com.example.cinemora.presentation.viewmodel.AgregarContenidoViewModel

@Composable
fun AgregarContenidoScreen(
    viewModel: AgregarContenidoViewModel,
    onGuardado: (Contenido) -> Unit
) {
    val formulario by viewModel.formulario.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        BrandHeader()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Titulo("Agregar contenido", "Registra una película o serie en el catálogo.")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(Tarjeta),
                shape = RoundedCornerShape(13.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val coloresCampo = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Morado,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White
                    )
                    OutlinedTextField(
                        value = formulario.titulo,
                        onValueChange = viewModel::cambiarTitulo,
                        label = { Text("Título") },
                        isError = formulario.errorTitulo,
                        supportingText = {
                            if (formulario.errorTitulo) Text("Escribe un título para continuar")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = coloresCampo
                    )
                    Text("Tipo de contenido", color = Color.White)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = formulario.tipo == "película",
                            onClick = { viewModel.cambiarTipo("película") },
                            label = { Text("Película") }
                        )
                        FilterChip(
                            selected = formulario.tipo == "serie",
                            onClick = { viewModel.cambiarTipo("serie") },
                            label = { Text("Serie") }
                        )
                    }
                    OutlinedTextField(
                        value = formulario.sinopsis,
                        onValueChange = viewModel::cambiarSinopsis,
                        label = { Text("Sinopsis (opcional)") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        colors = coloresCampo
                    )
                    Button(
                        onClick = { viewModel.guardar()?.let(onGuardado) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(Morado),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar y ver detalle")
                    }
                }
            }
        }
    }
}
