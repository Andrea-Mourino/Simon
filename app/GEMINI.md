# Gemini Custom Directives for Simon Game

This document provides project-specific guidelines for the Gemini CLI to ensure that any modifications align with the existing architecture, style, and conventions of the Simon game application.

## Project Overview

Simon Game is a mobile application that replicates the classic memory game "Simon." It is built for Android using modern development practices, including Jetpack Compose for the UI and the MVVM (Model-View-ViewModel) architecture to separate concerns.

**Core Features:**
-   **Random Sequence Generation:** The game creates and extends a sequence of colors that the player must memorize.
-   **State-Driven UI:** The user interface is built with Jetpack Compose and reacts to state changes from the ViewModel.
-   **MVVM Architecture:** Logic is cleanly separated from the UI, with `MyViewModel` managing the game state and `IU.kt` handling the presentation.
-   **Visual and Audio Feedback:** The game provides immediate feedback through color animations and sounds for each user interaction.
-   **Progressive Difficulty:** Each successful round increases the length of the sequence, making the game more challenging.

## Architecture and Design

The application follows the **MVVM (Model-View-ViewModel)** design pattern, which is standard for modern Android development.

-   **Model:** The data layer is represented by simple data holders and enums, primarily:
    -   `Colores`: An enum that defines the properties of each colored button (color values, text, etc.).
    -   `GameState`: An enum that defines the possible states of the game (e.g., `INICIO`, `MOSTRANDO`, `ADIVINANDO`), ensuring predictable UI behavior.

-   **View (`IU.kt`):** The UI is implemented entirely with Jetpack Compose.
    -   It is a stateless composable that receives a `MyViewModel` instance.
    -   It observes `StateFlow` objects exposed by the ViewModel to automatically update the UI when the game state changes.
    -   User interactions (button clicks) are forwarded directly to the ViewModel for processing.

-   **ViewModel (`MyViewModel.kt`):** This is the core of the application's logic.
    -   It holds the game's state, including the color sequence (`_listaSecuencia`), the current round (`_ronda`), and the current game status (`estadoActual`).
    -   It exposes this state to the UI using `MutableStateFlow` for reactive updates.
    -   All game logic resides here: generating new sequence steps, validating user input, managing rounds, and triggering audio feedback.
    -   It uses `viewModelScope` to launch coroutines for asynchronous operations, such as displaying the color sequence with delays.

### Data Flow

The data flow is unidirectional, which makes the application predictable and easier to debug:
1.  **UI Event:** The user presses a button (e.g., "Start" or a color).
2.  **ViewModel Action:** The `onClick` lambda in the View calls a public function in `MyViewModel` (e.g., `generarNNuevo()`, `comprobar()`).
3.  **State Update:** The ViewModel processes the action, updates its internal state (e.g., changes `estadoActual`, modifies `_listaSecuencia`), and exposes it via `StateFlow`.
4.  **UI Reaction:** The composables in `IU.kt`, collecting the `StateFlow` objects, automatically recompose to reflect the new state (e.g., a button changes color, the round counter updates).

## Key Files

-   `app/src/main/java/com/example/simon/MainActivity.kt`: The entry point of the application. It initializes `MyViewModel` and sets up the Jetpack Compose content with the `IU` composable.
-   `app/src/main/java/com/example/simon/IU.kt`: Contains all the Jetpack Compose code for the user interface. It defines the layout, buttons, and text, and observes the ViewModel's state.
-   `app/src/main/java/com/example/simon/MyViewModel.kt`: The brain of the application. It manages all game logic, state, and coroutine-based asynchronous tasks.
-   `app/src/main/java/com/example/simon/Datos.kt`: Defines the core data structures of the game, including the `Colores` and `GameState` enums. This file is critical for understanding the different states and data objects the app works with.
-   `app/build.gradle.kts`: The application's Gradle build script, which lists all dependencies. Key libraries include `androidx.lifecycle.runtime.ktx` (for ViewModel), `androidx.activity.compose`, and the `androidx.compose` suite.

## Development Workflow

-   **Building the project:** Use the standard Gradle wrapper command:
    ```bash
    ./gradlew build
    ```
-   **Running the application:** Open the project in Android Studio and run it on an emulator or a physical device.
-   **Running tests:** The project includes unit tests for the ViewModel. To run them, execute:
    ```bash
    ./gradlew test
    ```

## Code Style and Conventions

When modifying the code, please adhere to the following conventions:
-   **State Management:** All state that affects the UI must be managed within `MyViewModel` and exposed via `StateFlow`. Avoid holding state directly in composables unless it is simple, transient UI state (e.g., using `remember`).
-   **Immutability:** Treat the state exposed by the ViewModel as immutable. The View should only read from it and trigger ViewModel functions to request changes.
-   **Enums for State:** Use enums (`GameState`, `Colores`) to represent fixed sets of states or types. This improves readability and prevents invalid state combinations.
-   **Coroutines for Async:** Use `viewModelScope.launch` for any long-running or asynchronous work initiated from the ViewModel. Use `delay` for timed operations, as is currently done for showing the color sequence.
-   **Naming:** Follow existing naming conventions. `StateFlow` objects are prefixed with an underscore (e.g., `_ronda`) for the mutable version inside the ViewModel and exposed as a non-mutable `StateFlow` if needed (though the current implementation exposes the mutable version directly).
-   **Comments:** Add comments only to explain complex logic ("why"), not to describe what the code does ("what"). The existing comments in `MyViewModel.kt` that justify the separation of functions are a good example.
-   **Logging:** Use the `Log.d` function for debugging, with the tag `miDebug`, to maintain consistency with existing logs.

By following these guidelines, you will help maintain the quality and consistency of the Simon game codebase.
