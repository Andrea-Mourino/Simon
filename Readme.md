# Planificación: Integración de MongoDB en Simón Dice (MVVM)

## 🤖 Configuración del Asistente IA (GitHub Copilot)
Para la planificación y desarrollo de esta tarea, se ha utilizado GitHub Copilot. Para darle el contexto adecuado sobre la arquitectura del proyecto, se ha creado un archivo de configuración a nivel de repositorio.

Se ha añadido el archivo `.github/copilot-instructions.md` con las siguientes directrices para la IA:
1. **Contexto:** Se le ha indicado que es una app nativa en Kotlin siguiendo la arquitectura **MVVM**.
2. **Objetivo:** Añadir persistencia en MongoDB sin eliminar las estructuras actuales de `SharedPreferences` y `SQLite`.
3. **Rol:** Se le ha pedido que actúe como un Tech Lead, dividiendo el trabajo en tareas (Issues) pequeñas, respetando los principios SOLID y el Patrón Repositorio para que la Vista (UI) quede totalmente desacoplada de la base de datos.

## 📋 Issues Planificados
Gracias al contexto proporcionado mediante el archivo de instrucciones, Copilot ha generado una planificación limpia y modular. Los issues creados en este repositorio son:

* **#1 Crear interfaz `ScoreDataSource`:** Para abstraer las fuentes de datos (CRUD).
* **#2 Implementar `MongoDbScoreDataSource`:** Implementación concreta para MongoDB.
* **#3 Crear `ScoreRepository`:** Capa de abstracción que coordine SharedPreferences, SQLite y Mongo (Patrón Strategy).
* **#4 Configurar inyección de dependencias:** Configuración de módulos (Hilt/Dagger) para instanciar repositorios y DataSources.
* **#5 Actualizar ViewModel:** Refactorización para usar el nuevo `ScoreRepository` manteniendo la reactividad.
* **#6 Implementar sincronización bidireccional:** Lógica para guardar localmente y luego en MongoDB.
* **#7 Pruebas Unitarias:** Añadir tests para el `ScoreRepository` usando mocks.
* **#8 Documentación:** Documentar configuración de MongoDB y variables de entorno.

El prompt que he utilizado para generar esta planificación es el siguiente:
Solamente habría que crear los issues en GitHub con los títulos y descripciones técnicas proporcionadas, asegurándose de asignar las etiquetas correspondientes (ej. "backend", "database", "refactor", "testing") para facilitar la gestión del proyecto.
```
Basándote en las directrices del archivo .github/copilot-instructions.md, ayúdame a planificar la integración de MongoDB. Por favor, divídeme el trabajo en una lista de Issues de GitHub. Cada issue debe tener un título claro y una breve descripción de lo que hay que hacer a nivel técnico (patrón repositorio, inyección de dependencias, etc.
```

El resultado:
```
# Plan de Integración de MongoDB - Issues

## Issue 1: Crear interfaz `ScoreDataSource` para abstraer fuentes de datos
**Descripción técnica:**
Implementar una interfaz que defina operaciones CRUD para récords. Esto permitirá desacoplar la lógica de negocio de las implementaciones específicas (SharedPreferences, SQLite, MongoDB).

```kotlin
interface ScoreDataSource {
    suspend fun saveScore(score: Score): Result<Unit>
    suspend fun getTopScores(limit: Int): Result<List<Score>>
    suspend fun getAllScores(): Result<List<Score>>
    suspend fun deleteScore(id: String): Result<Unit>
}
```

---

## Issue 2: Implementar `MongoDbScoreDataSource`
**Descripción técnica:**
Crear la implementación concreta para MongoDB usando Realm SDK o una API REST. Incluir configuración de autenticación y manejo de errores de red.

---

## Issue 3: Crear `ScoreRepository` como capa de abstracción
**Descripción técnica:**
Implementar un repositorio que coordine múltiples fuentes de datos (SharedPreferences, SQLite, MongoDB). Debe aplicar patrón Strategy para permitir sincronización y caché entre fuentes.

---

## Issue 4: Configurar inyección de dependencias (Hilt/Dagger)
**Descripción técnica:**
Crear módulos de inyección que permitan instanciar `ScoreDataSource` y `ScoreRepository` con las dependencias correctas (retrofit, Realm client, etc.).

---

## Issue 5: Actualizar ViewModel para usar el `ScoreRepository`
**Descripción técnica:**
Refactorizar el ViewModel existente para usar el repositorio en lugar de acceder directamente a las fuentes de datos. Mantener la reactivity con Flow/LiveData.

---

## Issue 6: Implementar sincronización MongoDB ↔ Local
**Descripción técnica:**
Crear estrategia de sincronización bidireccional: guardar localmente primero, luego en MongoDB de forma asíncrona. Manejar conflictos y reintentos.

---

## Issue 7: Añadir pruebas unitarias para `ScoreRepository`
**Descripción técnica:**
Tests usando mocks de las implementaciones de `ScoreDataSource`. Validar lógica de sincronización y manejo de errores.

---

## Issue 8: Documentar configuración de MongoDB y variables de entorno
**Descripción técnica:**
Guía de setup: credenciales, endpoints, variables en `local.properties` o `gradle.properties`.
```