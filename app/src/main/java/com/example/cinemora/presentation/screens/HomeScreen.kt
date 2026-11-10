package com.example.cinemora.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cinemora.data.model.Contenido
import com.example.cinemora.presentation.components.*
import com.example.cinemora.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAbrir: (Contenido) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val catalogo by viewModel.catalogo.collectAsStateWithLifecycle()
    val lista by viewModel.miLista.collectAsStateWithLifecycle()
    val idsGuardados = remember(lista) { lista.map(Contenido::id).toSet() }

    Column(Modifier.fillMaxSize()) {
        BrandHeader()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onNavigateToProfile) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Morado
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Ver perfil",
                    color = Color(0xFFDDB5FF)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 18.dp)
        ) {
            item(
                span = {
                    androidx.compose.foundation.lazy.grid.GridItemSpan(2)
                }
            ) {
                Column {
                    Surface(
                        color = Color(0xFF211A29),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Surface(
                                color = Color(0xFF36233E),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    "◆ CINEMATECA PERSONAL",
                                    color = Color(0xFFE8C8FF),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(
                                        horizontal = 9.dp,
                                        vertical = 4.dp
                                    )
                                )
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "CineMora",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 25.sp
                            )

                            Text(
                                "Tu próxima historia empieza aquí",
                                color = Color(0xFFE3C8F5),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )

                            Spacer(Modifier.height(4.dp))

                            Text(
                                "Colecciona instantes, revive clásicos y descubre nuevas perspectivas en tu archivo fílmico.",
                                color = TextoSecundario,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard("En el catálogo", catalogo.size, Modifier.weight(1f))
                        StatCard("En Mi lista", lista.size, Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFE4B8FF),
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(Modifier.width(6.dp))

                        Text(
                            "Explora el catálogo",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            items(
                catalogo,
                key = Contenido::id
            ) { item ->
                ContenidoCard(
                    item = item,
                    guardado = item.id in idsGuardados,
                    onAbrir = { onAbrir(item) }
                )
            }
        }
    }
}
