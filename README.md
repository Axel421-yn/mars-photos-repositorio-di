# Mars Photos: repositorio e inyección de dependencias manual

Práctica de Android con Kotlin y Jetpack Compose, basada en el [codelab oficial](https://developer.android.com/codelabs/basic-android-kotlin-compose-add-repository?hl=es-419).

## Solución

La aplicación consulta el servicio de Mars Photos y muestra el número de fotografías recibidas. Esta etapa del curso muestra un contador, no una galería de imágenes.

- `MarsPhotosRepository`: contrato suspendido de acceso a datos.
- `NetworkMarsPhotosRepository`: delega en un `MarsApiService` recibido por constructor.
- `DefaultAppContainer`: construye Retrofit y proporciona el repositorio con inicialización diferida.
- `MarsPhotosApplication`: crea el contenedor y está registrada en el manifiesto.
- `MarsViewModel.Factory`: obtiene el contenedor e inyecta el repositorio.
- `MarsPhotosApp`: crea el ViewModel mediante su Factory.
- Pruebas con datos y servicios falsos: sin dependencia de la API externa.

## Ejecutar

Requisitos verificados: JDK 21, Android SDK 35, Gradle 8.11.1 y Android Gradle Plugin 8.9.2. Android mínimo: API 24; target: API 34.

1. Clonar este repositorio y abrirlo en Android Studio.
2. Configurar el JDK de Gradle en 21 y sincronizar.
3. Instalar Android SDK 35 si no está disponible. Android Studio configura `local.properties`; este archivo no se versiona.
4. Ejecutar `app` en un dispositivo o emulador con acceso a Internet.

En Windows:

~~~powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest lintDebug
~~~

En Linux/macOS usar `./gradlew`.

## Evidencias

Consultar [el informe](docs/Informe.pdf) y [los registros](docs/evidencias/). El informe distingue pruebas locales de JVM, renderizado de Compose y ejecución en un dispositivo Android.

Las pruebas unitarias comprueban éxito, lista vacía, propagación de errores, carga, errores HTTP/IO y recuperación. Las pruebas visuales usan Robolectric y ejecutan la interfaz Compose en la PC con repositorios de prueba inyectados. Las capturas se generan en `app/build/evidence/`.

## Historial y procedencia

Los commits separan el código inicial, el repositorio, la DI manual, la adaptación del entorno, las pruebas y el informe. Ver [historial](https://github.com/Axel421-yn/mars-photos-repositorio-di/commits/main/) y [procedencia](PROCEDENCIA.md).

El proyecto parte de la rama `repo-starter`, commit `ba9470a`, del repositorio de formación de Google. Se conserva la licencia Apache 2.0.
