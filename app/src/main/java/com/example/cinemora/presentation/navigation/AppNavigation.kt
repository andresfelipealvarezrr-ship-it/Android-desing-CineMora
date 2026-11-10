package com.example.cinemora.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cinemora.data.model.Contenido
import com.example.cinemora.presentation.components.Fondo
import com.example.cinemora.presentation.components.Morado
import com.example.cinemora.presentation.components.TextoSecundario
import com.example.cinemora.presentation.screens.AgregarContenidoScreen
import com.example.cinemora.presentation.screens.DetalleScreen
import com.example.cinemora.presentation.screens.HomeScreen
import com.example.cinemora.presentation.screens.MiListaScreen
import com.example.cinemora.presentation.screens.ProfileScreen
import com.example.cinemora.presentation.screens.TrailerPlayerScreen
import com.example.cinemora.presentation.screens.VideoPlayerScreen
import com.example.cinemora.presentation.viewmodel.HomeViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val actual = backStackEntry?.destination
    val enInicio = actual?.hasRoute<Destino.Inicio>() == true
    val enMiLista = actual?.hasRoute<Destino.MiLista>() == true
    val enAgregar = actual?.hasRoute<Destino.Agregar>() == true

    val catalogo by homeViewModel.catalogo.collectAsStateWithLifecycle()
    val abrirDetalle: (Contenido) -> Unit = { contenido ->
        navController.navigate(Destino.Detalle(contenido.id)) { launchSingleTop = true }
    }
    val volver: () -> Unit = { navController.popBackStack() }

    Scaffold(
        containerColor = Fondo,
        bottomBar = {
            if (enInicio || enMiLista || enAgregar) {
                NavigationBar(containerColor = Fondo) {
                    NavItem(enInicio, "Inicio", { Icon(Icons.Default.Home, null) }) {
                        navController.abrirPestana(Destino.Inicio)
                    }
                    NavItem(enMiLista, "Mi lista", { Icon(Icons.Default.Bookmark, null) }) {
                        navController.abrirPestana(Destino.MiLista)
                    }
                    NavItem(enAgregar, "Agregar", { Icon(Icons.Default.Add, null) }) {
                        navController.abrirPestana(Destino.Agregar)
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destino.Inicio,
            modifier = Modifier.padding(padding).consumeWindowInsets(padding)
        ) {
            composable<Destino.Inicio> {
                HomeScreen(
                    viewModel = homeViewModel,
                    onAbrir = abrirDetalle,
                    onNavigateToProfile = {
                        navController.navigate(Destino.Perfil) { launchSingleTop = true }
                    }
                )
            }
            composable<Destino.MiLista> {
                MiListaScreen(
                    viewModel = viewModel(),
                    onAbrir = abrirDetalle,
                    onExplorar = { navController.abrirPestana(Destino.Inicio) }
                )
            }
            composable<Destino.Agregar> {
                AgregarContenidoScreen(viewModel = viewModel(), onGuardado = abrirDetalle)
            }
            composable<Destino.Perfil> {
                ProfileScreen(username = "usuario_demo", onBack = volver)
            }
            composable<Destino.Detalle> { entry ->
                val ruta = entry.toRoute<Destino.Detalle>()
                DetalleScreen(
                    item = catalogo.firstOrNull { it.id == ruta.id },
                    viewModel = viewModel(),
                    onPlay = {
                        navController.navigate(Destino.Reproductor(ruta.id)) {
                            launchSingleTop = true
                        }
                    },
                    onTrailer = {
                        navController.navigate(Destino.Trailer(ruta.id)) { launchSingleTop = true }
                    },
                    onBack = volver
                )
            }
            composable<Destino.Reproductor> { entry ->
                val ruta = entry.toRoute<Destino.Reproductor>()
                VideoPlayerScreen(
                    contenido = catalogo.firstOrNull { it.id == ruta.id },
                    onBack = volver
                )
            }
            composable<Destino.Trailer> { entry ->
                val ruta = entry.toRoute<Destino.Trailer>()
                TrailerPlayerScreen(
                    contenido = catalogo.firstOrNull { it.id == ruta.id },
                    onBack = volver
                )
            }
        }
    }
}

private fun NavHostController.abrirPestana(destino: Destino) {
    navigate(destino) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

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
