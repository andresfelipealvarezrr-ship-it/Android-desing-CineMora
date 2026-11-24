# CineMora

Aplicación Android en Jetpack Compose que muestra películas y series reales del endpoint de tendencias de TMDB. La interfaz conserva rutas tipadas de Navigation Compose y organiza el código por capas MVVM.

## Arquitectura aplicada

La fuente del catálogo es exclusivamente TMDB. No hay películas escritas a mano ni enlaces de video de Archive.org.

~~~mermaid
sequenceDiagram
    participant H as HomeScreen
    participant V as HomeViewModel
    participant R as TmdbCatalogoRepository
    participant A as TmdbApi (Retrofit)
    participant T as API TMDB

    H->>V: observa uiState: StateFlow
    V->>R: obtenerCatalogo()
    R->>A: obtenerTendencias()
    A->>T: GET /3/trending/all/day
    T-->>A: JSON
    A-->>R: DTO serializables
    R-->>V: Exito, Vacio o Error
    V-->>H: HomeUiState
~~~

El detalle y los tráileres siguen la misma separación. La pantalla de tráiler solicita a su ViewModel; este pide al repositorio el video de TMDB y solo reproduce una clave oficial de YouTube cuando la API la entrega.

## Configuración

1. Abre el proyecto con Android Studio y espera la sincronización de Gradle.
2. En el archivo local local.properties, agrega tu token de lectura de TMDB (v4):

   ~~~properties
   TMDB_ACCESS_TOKEN=pega_aqui_tu_token_v4
   ~~~

3. No subas ese archivo: el repositorio lo ignora con .gitignore.
4. Ejecuta la app y abre **Inicio**. El encabezado “CATÁLOGO EN VIVO · TMDB” y las tarjetas con títulos actuales confirman que la respuesta viene de la API.

El token se transforma en BuildConfig.TMDB_ACCESS_TOKEN durante la compilación. No se deja escrito en Kotlin ni se imprime en logs.

## Estados de Inicio

- **Cargando:** se muestra mientras Retrofit consulta TMDB.
- **Éxito:** se muestran películas y series de la respuesta.
- **Vacío:** se explica que TMDB respondió sin elementos.
- **Error:** se informa una credencial ausente/rechazada, falta de conexión u otro error HTTP, con botón **Reintentar**.

No se presentan elementos de ejemplo como reemplazo de una consulta fallida.

## Alcance de las acciones

- **Mi lista:** se puede agregar o quitar un título real de TMDB durante la sesión actual. Es un StateFlow en memoria: no modifica TMDB y se pierde al cerrar el proceso.
- **Agregar:** permanece como destino de navegación, pero informa que TMDB no permite crear títulos desde esta app. Crear, editar o eliminar datos de forma persistente requeriría una fuente propia (Room o backend), que no forma parte de esta entrega.
- **Reproducir película:** se retiró porque TMDB no proporciona enlaces de streaming completos y el antiguo catálogo local ya no existe. **Ver tráiler** permanece cuando TMDB devuelve un video de YouTube.
- **Dónde ver:** abre una búsqueda externa de JustWatch; no representa reproducción dentro de CineMora.

Consulta [docs/arquitectura_y_flujo.md](docs/arquitectura_y_flujo.md) para la estructura y el recorrido técnico completo.
