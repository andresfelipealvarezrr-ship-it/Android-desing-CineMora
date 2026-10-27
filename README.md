# CineMora

Proyecto de diseño de una aplicación Android para consultar contenido cinematográfico, organizar listas personales y guardar preferencias.

## Prototipo UI/UX

Ver prototipo en Google Stitch([Mermaid Live Editor](https://mermaid.live/edit#pako:eNptUctOwzAQ_BVrTyCSKg3Oy-KCWipx4MQNVUJuYlIrsV38UEtL_53EkFBafLFn1jOeXR-gVBUDAmVLjZlzWmsqlhJ1615yQVt09xmGaO7K5pJdcLO-ZF_YStM_NEE3XFpEa3ZOP1vNZY1qJiumT4u9xDxR0R2vrs8Kglo2kD62j3f4JtBoumK0malW6bFgtlwMwg6-O1o2Az6e-vWNjX5hn93wPXuUC8bsSJdUPlD7r96P4DfQSqkWcfO65W01ktrJUQsB1JpXQKx2LADBtKA9BO-xBLtmgi2BdEfNKrcLK6qbsPTNwVL2-g2VL0qJwUIrV6-BvNHWdMhtqm5mP787XvFDnyknLZA49hZADrADMs3jSREXcbenCY5wEcAHkCSbFEWGC5ynWY6LaY6PAez9o9EkxbdpjtN8GmdREkfJ8Qtl0bQM))

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