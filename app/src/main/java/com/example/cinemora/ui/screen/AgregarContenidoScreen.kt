package com.example.cinemora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cinemora.ui.components.*
import com.example.cinemora.viewmodel.AgregarContenidoViewModel
import kotlinx.coroutines.launch

@Composable
fun AgregarContenidoScreen(viewModel: AgregarContenidoViewModel) {
    val titulo by viewModel.titulo.collectAsStateWithLifecycle()
    val esError by viewModel.esError.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }; val scope = rememberCoroutineScope()
    Scaffold(containerColor = Fondo, snackbarHost = { SnackbarHost(snackbar) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            BrandHeader()
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(color = Color(0xFF4B2263), shape = RoundedCornerShape(16.dp)) { Text("‹› Jetpack Compose", color = Color(0xFFE5BCFF), fontSize = 9.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) }
                    Text("mutableStateOf(\"\")", color = Color(0xFFE4B7FF), fontFamily = FontFamily.Monospace, fontSize = 9.sp)
                }
                Spacer(Modifier.height(8.dp))
                Text("Agregar contenido", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
                Text("Escribe un título para añadirlo al catálogo y observa la reactividad en tiempo real.", color = Color(0xFFE3DCE8), fontSize = 12.sp, lineHeight = 17.sp)
                Spacer(Modifier.height(13.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    AssistChip(onClick = {}, label = { Text(if (titulo.isBlank()) "1. Vacío" else "1. Completado", fontSize = 9.sp) })
                    AssistChip(onClick = {}, label = { Text("2. Escribiendo", fontSize = 9.sp) })
                    AssistChip(onClick = {}, label = { Text("3. Error", fontSize = 9.sp) })
                }
                Card(colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(13.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Row { Icon(Icons.Default.Code, null, tint = Color(0xFFE1B5FF), modifier = Modifier.size(17.dp)); Spacer(Modifier.width(6.dp)); Text("Estado de la Variable", color = Color(0xFFE1B5FF), fontWeight = FontWeight.Bold, fontSize = 11.sp); Spacer(Modifier.weight(1f)); Text(if (titulo.isBlank()) "Estado 1: Vacío" else "Estado 2: Escribiendo", color = Color.White, fontSize = 9.sp) }
                        Spacer(Modifier.height(12.dp))
                        Surface(color = Color(0xFF0F0F13), shape = RoundedCornerShape(8.dp)) { Text("var titulo by remember { mutableStateOf(\"$titulo\") }", color = Color(0xFFE4B7FF), fontFamily = FontFamily.Monospace, fontSize = 9.sp, modifier = Modifier.fillMaxWidth().padding(11.dp)) }
                    }
                }
                Spacer(Modifier.height(13.dp))
                Card(colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(13.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Text("Título de la película", color = Color(0xFFE1B5FF), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(value = titulo, onValueChange = viewModel::cambiarTitulo, placeholder = { Text("Ejemplo: Interestelar") }, isError = esError, supportingText = { if (esError) Text("Escribe un título para continuar") else Text("${titulo.length}/60", modifier = Modifier.fillMaxWidth()) }, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Morado, unfocusedBorderColor = Color(0xFF39383E), focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        Text("Categoría rápida", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
                        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) { SuggestionChip({}, { Text("+ Ciencia Ficción", fontSize = 8.sp) }); SuggestionChip({}, { Text("+ Drama", fontSize = 8.sp) }); SuggestionChip({}, { Text("+ Aventura", fontSize = 8.sp) }) }
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { if (viewModel.guardar()) scope.launch { snackbar.showSnackbar("Contenido agregado correctamente") } }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(Morado), shape = RoundedCornerShape(12.dp)) { Icon(Icons.Default.Save, null); Spacer(Modifier.width(8.dp)); Text("Guardar contenido", fontWeight = FontWeight.Bold) }
                    }
                }
                Spacer(Modifier.height(13.dp))
                Card(colors = CardDefaults.cardColors(Tarjeta), shape = RoundedCornerShape(13.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) { Text("🎓 ¿Cómo funciona en Jetpack Compose?", color = Color(0xFFE1B5FF), fontWeight = FontWeight.Bold, fontSize = 11.sp); Spacer(Modifier.height(8.dp)); Text("En el Estado 1, el valor inicial es vacío. El composable OutlinedTextField lee este estado y dibuja su contenido. Al escribir, mutableStateOf notifica a Compose y la pantalla se actualiza.", color = Color(0xFFD2CBD5), fontSize = 10.sp, lineHeight = 15.sp) }
                }
                Spacer(Modifier.height(18.dp))
            }
        }
    }
}
