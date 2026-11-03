# Navegación Type-Safe en CineMora

- Se conserva Navigation Compose con rutas `@Serializable`.
- El estado compartido del catálogo, favoritos y altas manuales está en `CineMoraViewModel`.
- No se usa Repository ni capa Remote.
- El catálogo es local y está definido en el ViewModel principal.
- Los tráileres funcionan con los IDs de YouTube guardados en cada contenido; no se consultan servicios externos para buscarlos.
- La navegación de detalle, reproductor y tráiler continúa enviando ID y tipo del contenido.

Abre el proyecto en Android Studio y sincroniza Gradle. La reproducción de vídeos existentes puede requerir conexión a Internet.
