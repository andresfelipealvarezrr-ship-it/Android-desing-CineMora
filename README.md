# CineMora

Proyecto de diseño de una aplicación Android para consultar contenido cinematográfico, organizar listas personales y guardar preferencias.

## Prototipo UI/UX

Ver prototipo en [Google Stitch](https://stitch.withgoogle.com/projects/10239007104782718379)

## Diagrama entidad-relación

```mermaid
erDiagram
    USUARIO {
        int id_usuario PK
        string nombre
        datetime fecha_creacion
    }

    LISTA_PERSONAL {
        int id_lista PK
        int id_usuario FK
        string nombre
        datetime fecha_creacion
    }

    CONTENIDO {
        int id_contenido PK
        int id_tmdb
        string titulo
        string tipo
        string sinopsis
        string fecha_lanzamiento
        float puntuacion
        string poster_path
    }

    GENERO {
        int id_genero PK
        string nombre
    }

    ELEMENTO_LISTA {
        int id_elemento PK
        int id_lista FK
        int id_contenido FK
        string estado
        datetime fecha_agregado
    }

    CONTENIDO_GENERO {
        int id_contenido FK
        int id_genero FK
    }

    USUARIO ||--o{ LISTA_PERSONAL : posee
    LISTA_PERSONAL ||--o{ ELEMENTO_LISTA : contiene
    CONTENIDO ||--o{ ELEMENTO_LISTA : aparece_en
    CONTENIDO ||--o{ CONTENIDO_GENERO : se_clasifica_por
    GENERO ||--o{ CONTENIDO_GENERO : agrupa
```

## Diagrama de arquitectura

```mermaid
flowchart TD
    A[Usuario] --> B[UI: Jetpack Compose]
    B --> C[ViewModel]
    C --> D[Estados UI: Loading / Success / Error]
    C --> E[Repository]

    E --> F[Remote Data Source]
    E --> G[Local Data Source]

    F --> H[Retrofit + Kotlin Serialization]
    H --> I[API de TMDB]

    G --> J[Room: Mi lista]
    G --> K[DataStore: Preferencias]
```

## Pantallas planificadas

1. Inicio y exploración de películas o series.
2. Detalle de contenido: título, sinopsis, géneros, puntuación y póster.
3. Lista personal del usuario.
4. Preferencias de la aplicación.

## Recursos provenientes de la API TMDB

- Películas y series.
- Títulos.
- Sinopsis.
- Fecha de lanzamiento.
- Puntuación.
- Póster.
- Géneros.
