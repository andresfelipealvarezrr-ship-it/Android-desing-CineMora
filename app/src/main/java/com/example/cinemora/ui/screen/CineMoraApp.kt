package com.example.cinemora.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cinemora.ui.components.Fondo
import com.example.cinemora.ui.components.Morado
import com.example.cinemora.ui.components.TextoSecundario
import com.example.cinemora.viewmodel.AgregarContenidoViewModel
import com.example.cinemora.viewmodel.CineMoraAppViewModel
import com.example.cinemora.viewmodel.Destino
import com.example.cinemora.viewmodel.DetalleViewModel
import com.example.cinemora.viewmodel.HomeViewModel
import com.example.cinemora.viewmodel.MiListaViewModel

@Composable
fun CineMoraApp(
    appViewModel: CineMoraAppViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    listaViewModel: MiListaViewModel = viewModel(),
    detalleViewModel: DetalleViewModel = viewModel(),
    agregarViewModel: AgregarContenidoViewModel = viewModel()
) {
    val destino by appViewModel.destino.collectAsStateWithLifecycle()
    val seleccionado by appViewModel.seleccionado.collectAsStateWithLifecycle()
    Scaffold(containerColor = Fondo, bottomBar = {
        if (destino != Destino.DETALLE) NavigationBar(containerColor = Fondo) {
            NavItem(destino == Destino.INICIO, "Inicio", { Icon(Icons.Default.Home, null) }) { appViewModel.navegar(Destino.INICIO) }
            NavItem(destino == Destino.LISTA, "Mi lista", { Icon(Icons.Default.Bookmark, null) }) { appViewModel.navegar(Destino.LISTA) }
            NavItem(destino == Destino.AGREGAR, "Agregar", { Icon(Icons.Default.Add, null) }) { appViewModel.navegar(Destino.AGREGAR) }
        }
    }) { padding ->
        Box(Modifier.padding(padding)) {
            when (destino) {
                Destino.INICIO -> HomeScreen(homeViewModel, appViewModel::abrirDetalle)
                Destino.LISTA -> MiListaScreen(listaViewModel, appViewModel::abrirDetalle) { appViewModel.navegar(Destino.INICIO) }
                Destino.AGREGAR -> AgregarContenidoScreen(agregarViewModel)
                Destino.DETALLE -> DetalleScreen(
                    item = seleccionado,
                    viewModel = detalleViewModel,
                    onPlay = { appViewModel.navegar(Destino.REPRODUCTOR) },
                    onTrailer = { appViewModel.navegar(Destino.TRAILER) },
                    onBack = { appViewModel.navegar(Destino.INICIO) }
                )
                Destino.REPRODUCTOR -> VideoPlayerScreen(
                    contenido = seleccionado,
                    onBack = { appViewModel.navegar(Destino.DETALLE) }
                )
                Destino.TRAILER -> TrailerPlayerScreen(
                    contenido = seleccionado,
                    onBack = { appViewModel.navegar(Destino.DETALLE) }
                )
            }
        }
    }
}

@Composable
private fun RowScope.NavItem(selected: Boolean, label: String, icon: @Composable () -> Unit, onClick: () -> Unit) =
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        label = { Text(label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White,
            selectedTextColor = Color.White,
            indicatorColor = Morado,
            unselectedIconColor = TextoSecundario,
            unselectedTextColor = TextoSecundario
        )
    )
