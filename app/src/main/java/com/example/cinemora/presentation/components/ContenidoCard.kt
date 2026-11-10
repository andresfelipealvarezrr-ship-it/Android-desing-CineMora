package com.example.cinemora.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
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
import kotlin.math.round

@Composable
fun ContenidoCard(item: Contenido, guardado: Boolean, onAbrir: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAbrir),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Tarjeta)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(218.dp)
                    .background(Color(0xFF2B2838))
            ) {
                Poster(item, Modifier.fillMaxSize())

                Surface(
                    color = Color(0xD91A171F),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(7.dp)
                ) {
                    Row(
                        Modifier.padding(
                            horizontal = 7.dp,
                            vertical = 3.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFE8B9FF),
                            modifier = Modifier.size(12.dp)
                        )

                        Text(
                            " ${round(item.puntuacion * 10) / 10}",
                            color = Color.White,
                            fontSize = 10.sp
                        )
                    }
                }

                if (guardado) {
                    Surface(
                        color = Morado,
                        shape = RoundedCornerShape(
                            bottomStart = 9.dp
                        ),
                        modifier = Modifier.align(
                            Alignment.TopEnd
                        )
                    ) {
                        Row(
                            Modifier.padding(
                                horizontal = 7.dp,
                                vertical = 4.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )

                            Text(
                                " En lista",
                                color = Color.White,
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }

            Column(Modifier.padding(10.dp)) {
                Text(
                    item.titulo,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp
                )

                Text(
                    "${item.anio} • ${item.generos.firstOrNull() ?: item.tipo}",
                    color = TextoSecundario,
                    maxLines = 1,
                    fontSize = 10.sp
                )

                Text(
                    item.tipo.replaceFirstChar {
                        it.uppercase()
                    },
                    color = Color(0xFFDDB5FF),
                    maxLines = 1,
                    fontSize = 9.sp
                )
            }
        }
    }
}
