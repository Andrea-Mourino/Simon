# Simon Game - Android App (Alexandre Sinisterra & Andrea Mouriño)

## Descripción del proyecto

Simon Game es una implementación del clásico juego de memoria “Simon” para Android, desarrollado usando **Jetpack Compose** y **MVVM**.
El juego presenta al usuario una secuencia de colores que debe memorizar y repetir. Cada ronda agrega un color nuevo a la secuencia, aumentando la dificultad progresivamente. El juego ofrece **retroalimentación visual y sonora** en tiempo real para cada acción.

### Características principales

* Secuencia de colores aleatoria generada por el sistema.
* Validación de aciertos y fallos con reinicio automático del juego.
* Indicadores visuales: parpadeo de colores, botones deshabilitados/activados según el estado.
* Sonido asociado a cada color y retroalimentación sonora de error.
* Visualización clara de la ronda actual.

---

## Instrucciones de compilación y ejecución

1. **Clonar el repositorio:**

   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```

2. **Abrir el proyecto en Android Studio:**

    * Selecciona *Open an existing Android Studio project*.
    * Espera a que Gradle sincronice las dependencias.

3. **Ejecutar la app:**

    * Conecta un dispositivo físico o usa un emulador Android.
    * Haz clic en **Run** y selecciona el dispositivo.

4. **Ejecutar tests unitarios:**

   ```bash
   ./gradlew test
   ```

    * Los tests cubren la **lógica del ViewModel**, incluyendo generación de secuencia, verificación de aciertos, fallos y transiciones de estado.

---

## Arquitectura del proyecto

El proyecto sigue la arquitectura **MVVM (Model-View-ViewModel)**:

* **Model:** Representado por los enums `Colores` y `GameState`, que contienen la información de colores y estados del juego.
* **ViewModel (`MyViewModel`):**

    * Controla la **lógica del juego**, incluyendo generación de números aleatorios, manejo de secuencia, validación de aciertos/fallos y gestión de rondas.
    * Mantiene variables reactivas (`MutableStateFlow`) para que la UI se actualice automáticamente.
* **View (Compose UI):**

    * `IU()` es el punto central de la interfaz, mostrando botones de colores, botón Start y la ronda actual.
    * Los botones reflejan el estado actual del juego y muestran colores activos o pulsados.

**Flujo de datos:**

```
ViewModel -> StateFlow -> UI (Jetpack Compose)
UI -> eventos de usuario -> ViewModel
```

---

## Justificación de decisiones de diseño

1. **Separación de lógica y UI:**

    * Toda la lógica reside en `MyViewModel` para facilitar testing y mantener la UI declarativa solo para la presentación.

2. **Uso de StateFlow y Compose:**

    * Permite que los cambios de estado se reflejen instantáneamente en la UI sin callbacks complejos.

3. **Enums para colores y estados:**

    * Mejora la legibilidad y permite extender fácilmente los colores o estados sin cambiar la lógica central.

4. **Funciones pequeñas y modulares:**

    * Cada función del ViewModel realiza una única tarea (generar número, mostrar secuencia, comprobar botón), lo que facilita pruebas unitarias y depuración.

5. **Animaciones y sonidos:**

    * Se decidió usar `animateColorAsState` y `ToneGenerator` directamente para mantener la experiencia de juego simple y reactiva sin librerías externas.

6. **Testing con alta cobertura:**

    * Todos los cambios de estado y funciones críticas del juego están cubiertos por tests unitarios, garantizando confiabilidad.


