# Arquitectura y flujo de CineMora

## Estructura real

~~~text
com.example.cinemora/
├── data/
│   ├── api/
│   │   └── TmdbApi.kt
│   ├── dto/
│   │   └── TmdbDtos.kt
│   ├── network/
│   │   └── TmdbNetwork.kt
│   └── repository/
│       ├── AppContainer.kt
│       ├── CatalogoRepository.kt
│       ├── TmdbCatalogoRepository.kt
│       ├── MiListaRepository.kt
│       └── Contenido.kt
├── presentation/
│   ├── components/
│   │   └── CineComponents.kt
│   ├── navigation/
│   │   └── CineMoraDestination.kt
│   ├── screens/
│   │   ├── CineMoraApp.kt
│   │   ├── HomeScreen.kt
│   │   ├── DetalleScreen.kt
│   │   ├── MiListaScreen.kt
│   │   ├── AgregarContenidoScreen.kt
│   │   ├── TrailerPlayerScreen.kt
│   │   └── ProfileScreen.kt
│   └── viewmodel/
│       ├── HomeViewModel.kt
│       ├── DetalleViewModel.kt
│       ├── MiListaViewModel.kt
│       ├── AgregarContenidoViewModel.kt
│       └── TrailerPlayerViewModel.kt
├── ui/theme/
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
└── MainActivity.kt
~~~

## Responsabilidades

| Capa | Responsabilidad |
| --- | --- |
| presentation/screens | Dibuja Compose y reacciona a estados, sin llamar HTTP. |
| presentation/viewmodel | Inicia casos de uso con viewModelScope y expone StateFlow. |
| data/repository | Define contratos, traduce DTO a Contenido, conserva el último catálogo y modela errores. |
| data/api | Declara los endpoints Retrofit de TMDB. |
| data/network | Configura base URL, Json de Kotlinx Serialization, OkHttp y el encabezado Bearer. |
| data/dto | Representa JSON real de TMDB con Serializable y SerialName. |

AppContainer realiza la inyección manual de las dos implementaciones actuales. Esto evita que las pantallas construyan clientes de red o repositorios.

## Recorrido de Inicio

~~~mermaid
flowchart LR
    HS[HomeScreen] -->|observa uiState| HVM[HomeViewModel]
    HVM -->|viewModelScope| CR[CatalogoRepository]
    CR -->|implementación| TR[TmdbCatalogoRepository]
    TR --> API[TmdbApi Retrofit]
    API --> NET[TmdbNetwork + OkHttp]
    NET --> TMDB[TMDB /3/trending/all/day]
    TMDB -->|JSON| DTO[TmdbTrendingResponseDto]
    DTO -->|mapeo| TR
    TR -->|Exito / Vacio / Error| HVM
    HVM -->|StateFlow HomeUiState| HS
~~~

1. HomeViewModel inicia cargarCatalogo() al crearse y publica Cargando.
2. TmdbCatalogoRepository comprueba que exista el token y llama al endpoint Retrofit.
3. Retrofit usa OkHttp para enviar el Bearer y Kotlinx Serialization para deserializar los DTO.
4. El repositorio mapea películas y series a Contenido, sin mezclar títulos locales.
5. El ViewModel publica Exito, Vacio o Error. HomeScreen compone el estado correspondiente y permite reintentar los dos últimos casos.

Los mensajes HTTP distinguen, entre otros, 401/403 (credencial), 429 (límite de TMDB) y problemas de conexión. El interceptor no registra la cabecera ni el token.

## Navegación y detalle

Las rutas de CineMoraDestination.kt se mantienen tipadas con Serializable. Inicio y Mi lista navegan con Detalle(id, tipo), y Detalle abre Trailer(id, tipo). El objeto completo no se serializa en la ruta; DetalleViewModel lo recupera del catálogo obtenido en la sesión.

~~~mermaid
flowchart TD
    I[Inicio] -->|Detalle id/tipo| D[Detalle]
    L[Mi lista] -->|Detalle id/tipo| D
    D -->|Trailer id/tipo| T[Tráiler]
    D -->|navigateUp| I
    T -->|navigateUp| D
~~~

TrailerPlayerViewModel consulta movie/{id}/videos o tv/{id}/videos, elige preferentemente un tráiler oficial de YouTube y comunica Cargando, Disponible, NoDisponible o Error. La pantalla no inventa identificadores de video.

## Credencial y demostración en clase

1. Mantén TMDB_ACCESS_TOKEN en local.properties, nunca en Kotlin.
2. Sincroniza, ejecuta y activa conexión.
3. En Inicio observa el indicador de carga y luego tarjetas que cambian al consultar tendencias de TMDB.
4. Abre un detalle: título, póster, sinopsis, género y puntuación proceden del DTO remoto.
5. Pulsa **Ver tráiler** para demostrar el segundo endpoint.
6. Desactiva la red o reemplaza temporalmente el token local por un valor inválido: Inicio debe mostrar un error y **Reintentar**, sin títulos de ejemplo.

No copies una credencial al README, capturas ni consola.

## Límites deliberados

TMDB es una fuente de metadatos y no da una URL de reproducción completa. Por eso se eliminó el reproductor de archivos Archive.org y se conserva únicamente el tráiler entregado por TMDB.

MiListaEnMemoriaRepository demuestra el flujo reactivo de agregar/quitar durante la ejecución, pero no es persistencia. No modifica el catálogo remoto. La pantalla **Agregar** aclara que una operación CRUD propia quedaría pendiente hasta que se autorice Room o un backend; por ese motivo tampoco se incorpora el CRUD que un ejemplo como AutoRent podría requerir.
