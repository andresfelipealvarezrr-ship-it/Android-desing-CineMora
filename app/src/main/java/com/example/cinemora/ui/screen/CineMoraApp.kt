package com.example.cinemora.ui.screen

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cinemora.repository.CineRepository
import com.example.cinemora.ui.components.Fondo
import com.example.cinemora.ui.components.Morado
import com.example.cinemora.ui.components.TextoSecundario
import com.example.cinemora.ui.navigation.AgregarContenido
import com.example.cinemora.ui.navigation.Detalle
import com.example.cinemora.ui.navigation.Inicio
import com.example.cinemora.ui.navigation.Lista
import com.example.cinemora.ui.navigation.Perfil
import com.example.cinemora.ui.navigation.Reproductor
import com.example.cinemora.ui.navigation.Trailer

@Composable
fun CineMoraApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destinoActual = navBackStackEntry?.destination

    Scaffold(
        containerColor = Fondo,
        bottomBar = {
            if (destinoActual.esDestinoPrincipal()) {
                NavigationBar(containerColor = Fondo) {
                    NavItem(
                        selected = destinoActual.tieneRuta<Inicio>(),
                        label = "Inicio",
                        icon = { Icon(Icons.Default.Home, null) },
                        onClick = { navController.navegarADestinoPrincipal(Inicio) }
                    )
                    NavItem(
                        selected = destinoActual.tieneRuta<Lista>(),
                        label = "Mi lista",
                        icon = { Icon(Icons.Default.Bookmark, null) },
                        onClick = { navController.navegarADestinoPrincipal(Lista) }
                    )
                    NavItem(
                        selected = destinoActual.tieneRuta<AgregarContenido>(),
                        label = "Agregar",
                        icon = { Icon(Icons.Default.Add, null) },
                        onClick = { navController.navegarADestinoPrincipal(AgregarContenido) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Inicio,
            modifier = Modifier.padding(padding)
        ) {
            composable<Inicio> {
                HomeScreen(
                    viewModel = viewModel(),
                    onAbrir = { contenido ->
                        navController.navigate(Detalle(contenido.id, contenido.tipo))
                    },
                    onNavigateToProfile = { navController.navigate(Perfil) }
                )
            }

            composable<Lista> {
                MiListaScreen(
                    viewModel = viewModel(),
                    onAbrir = { contenido ->
                        navController.navigate(Detalle(contenido.id, contenido.tipo))
                    },
                    onExplorar = { navController.navegarADestinoPrincipal(Inicio) }
                )
            }

            composable<AgregarContenido> {
                AgregarContenidoScreen(viewModel = viewModel())
            }

            composable<Detalle> { backStackEntry ->
                val ruta = backStackEntry.toRoute<Detalle>()
                val contenido = CineRepository.obtenerContenido(
                    id = ruta.contenidoId,
                    tipo = ruta.contenidoTipo
                )
                DetalleScreen(
                    item = contenido,
                    viewModel = viewModel(),
                    onPlay = {
                        navController.navigate(Reproductor(ruta.contenidoId, ruta.contenidoTipo))
                    },
                    onTrailer = {
                        navController.navigate(Trailer(ruta.contenidoId, ruta.contenidoTipo))
                    },
                    onBack = { navController.navigateUp() }
                )
            }

            composable<Reproductor> { backStackEntry ->
                val ruta = backStackEntry.toRoute<Reproductor>()
                VideoPlayerScreen(
                    contenido = CineRepository.obtenerContenido(ruta.contenidoId, ruta.contenidoTipo),
                    onBack = { navController.navigateUp() }
                )
            }

            composable<Trailer> { backStackEntry ->
                val ruta = backStackEntry.toRoute<Trailer>()
                TrailerPlayerScreen(
                    contenido = CineRepository.obtenerContenido(ruta.contenidoId, ruta.contenidoTipo),
                    onBack = { navController.navigateUp() }
                )
            }

            composable<Perfil> {
                ProfileScreen(
                    username = "usuario_demo",
                    onBack = { navController.navigateUp() }
                )
            }
        }
    }
}

private inline fun <reified T : Any> NavHostController.navegarADestinoPrincipal(destino: T) {
    navigate(destino) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun NavDestination?.esDestinoPrincipal(): Boolean =
    this.tieneRuta<Inicio>() || this.tieneRuta<Lista>() || this.tieneRuta<AgregarContenido>()

private inline fun <reified T : Any> NavDestination?.tieneRuta(): Boolean =
    this?.hasRoute<T>() == true

@Composable
private fun RowScope.NavItem(
    selected: Boolean,
    label: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
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
}
