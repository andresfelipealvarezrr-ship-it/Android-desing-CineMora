package com.example.cinemora.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.cinemora.viewmodel.CineMoraViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.hasRoute
import androidx.navigation.toRoute
import com.example.cinemora.ui.components.Fondo
import com.example.cinemora.ui.components.Morado
import com.example.cinemora.ui.components.TextoSecundario

@Composable
fun CineMoraApp(
    cineMoraViewModel: CineMoraViewModel = viewModel()
) {
    val catalogo by cineMoraViewModel.catalogo.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val mostrarBarra = currentDestination?.let { destination ->
        !destination.isRoute<DetalleRoute>() &&
            !destination.isRoute<ReproductorRoute>() &&
            !destination.isRoute<TrailerRoute>()
    } ?: true

    Scaffold(
        containerColor = Fondo,
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = Fondo) {
                    NavItem(
                        selected = currentDestination.isRoute<HomeRoute>(),
                        label = "Inicio",
                        icon = { Icon(Icons.Default.Home, null) }
                    ) { navController.navigateTab(HomeRoute) }

                    NavItem(
                        selected = currentDestination.isRoute<MiListaRoute>(),
                        label = "Mi lista",
                        icon = { Icon(Icons.Default.Bookmark, null) }
                    ) { navController.navigateTab(MiListaRoute) }

                    NavItem(
                        selected = currentDestination.isRoute<AgregarRoute>(),
                        label = "Agregar",
                        icon = { Icon(Icons.Default.Add, null) }
                    ) { navController.navigateTab(AgregarRoute) }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(padding)
        ) {
            composable<HomeRoute> {
                HomeScreen(cineMoraViewModel) { contenido ->
                    navController.navigate(
                        DetalleRoute(contenidoId = contenido.id, tipo = contenido.tipo)
                    )
                }
            }

            composable<MiListaRoute> {
                MiListaScreen(
                    viewModel = cineMoraViewModel,
                    onAbrir = { contenido ->
                        navController.navigate(
                            DetalleRoute(contenidoId = contenido.id, tipo = contenido.tipo)
                        )
                    },
                    onExplorar = { navController.navigateTab(HomeRoute) }
                )
            }

            composable<AgregarRoute> {
                AgregarContenidoScreen(cineMoraViewModel)
            }

            composable<DetalleRoute> { backStackEntry ->
                val args = backStackEntry.toRoute<DetalleRoute>()
                val contenido = catalogo.firstOrNull {
                    it.id == args.contenidoId && it.tipo == args.tipo
                }

                DetalleScreen(
                    item = contenido,
                    viewModel = cineMoraViewModel,
                    onPlay = {
                        navController.navigate(
                            ReproductorRoute(args.contenidoId, args.tipo)
                        )
                    },
                    onTrailer = {
                        navController.navigate(
                            TrailerRoute(args.contenidoId, args.tipo)
                        )
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable<ReproductorRoute> { backStackEntry ->
                val args = backStackEntry.toRoute<ReproductorRoute>()
                val contenido = catalogo.firstOrNull {
                    it.id == args.contenidoId && it.tipo == args.tipo
                }

                if (contenido != null) {
                    VideoPlayerScreen(
                        contenido = contenido,
                        onBack = { navController.popBackStack() }
                    )
                } else {
                    ContenidoNoDisponible { navController.popBackStack() }
                }
            }

            composable<TrailerRoute> { backStackEntry ->
                val args = backStackEntry.toRoute<TrailerRoute>()
                val contenido = catalogo.firstOrNull {
                    it.id == args.contenidoId && it.tipo == args.tipo
                }

                if (contenido != null) {
                    TrailerPlayerScreen(
                        contenido = contenido,
                        onBack = { navController.popBackStack() }
                    )
                } else {
                    ContenidoNoDisponible { navController.popBackStack() }
                }
            }
        }
    }
}

private inline fun <reified T : Any> NavDestination?.isRoute(): Boolean =
    this?.hasRoute<T>() == true

private inline fun <reified T : Any> NavHostController.navigateTab(route: T) {
    navigate(route) {
        popUpTo<HomeRoute> {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun ContenidoNoDisponible(onBack: () -> Unit) {
    androidx.compose.foundation.layout.Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text("No se encontró este contenido.", color = Color.White)
        androidx.compose.material3.TextButton(onClick = onBack) {
            Text("Volver")
        }
    }
}

@Composable
private fun RowScope.NavItem(
    selected: Boolean,
    label: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) = NavigationBarItem(
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
