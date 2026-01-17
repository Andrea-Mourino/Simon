---
tipo: "feature"
nombre: "mongodb-client"
version: 1
estado: "Planificado (🔵)"
agente: 'agente'
descripción: 'Plan paso a paso para integrar MongoDB Realm / Realm Sync (cliente directo) en la app Android/Kotlin sin backend intermedio.'
herramientas: ['changes','search/codebase','edit/editFiles','runCommands/terminalLastCommand']
---

# 🚀 Plan de Desarrollo: mongodb-client

![Estado: Planificado](https://img.shields.io/badge/estado-Planificado-1f78ff)
![Prioridad: ALTA](https://img.shields.io/badge/prioridad-🚨_ALTA-red)
![Esfuerzo: MEDIO](https://img.shields.io/badge/esfuerzo-⚖️_MEDIO-yellow)

**Versión**: 1  
**Última actualización**: 2026-01-17  
**Creador/Responsable**: Equipo Simon ✨

## 🎯 Objetivo Principal
Permitir que la app Android (Kotlin + Gradle) guarde registros localmente y opcionalmente sincronice con MongoDB Atlas sin usar un backend intermedio, usando MongoDB Realm (Realm Kotlin / Realm Sync) para gestionar persistencia local + sincronización en la colección `records`, centrado en los campos `record` y `date`.

## 🧐 Contexto y Motivación

### ¿Por qué me apetece hacer este proyecto?
- [ ] **Necesidad funcional**: Guardar registros con fecha y tener opción de sincronización segura con Atlas.
- [ ] **Aprendizaje**: Integrar Realm Kotlin + Sync y buenas prácticas (auth, reglas).
- [ ] **Portafolio/Operacional**: Evitar coste/latencia de backend manteniendo sync P2C vía Realm.

### ¿Qué problema soluciona?
1. El rollo actual: la app solo tiene persistencia local o no sincroniza directamente con Atlas.
2. La solución chula: usar Realm Kotlin con Device Sync para persistencia local y sincronización automática con MongoDB Atlas, reduciendo la necesidad de un backend propio.

---

## 🗺️ Alcance (Qué SÍ y qué NO)

### ✅ SÍ va a incluir
- [ ] Integración de Realm Kotlin/Realm Sync en la app `app/`.
- [ ] Modelo `RecordEntity` con `id`, `record`, `date`.
- [ ] Inicialización en `Application`, login anónimo y CRUD básico (insert/consulta).
- [ ] Configuración mínima en MongoDB Atlas / Realm (cluster, app, reglas anónimas).
- [ ] Snippets de Gradle (KTS) y cambios concretos en archivos del repo.
- [ ] Tests unitarios (serialización, realm in-memory).

### ❌ NO va a incluir (por ahora)
- [ ] Backend personalizado (Node/Java) ni API REST.
- [ ] Reglas de seguridad complejas por rol (solo ejemplo para desarrollo).
- [ ] Migraciones de esquema avanzadas fuera de la mínima requerida.

---

## 🛠️ Kit de Herramientas (Stack Tecnológico)

| Categoría | Tecnología Elegida | ¿Por qué esta? |
|-----------|-------------------|----------------|
| Mobile | Android (Kotlin) | Proyecto existente en Kotlin/Gradle |
| DB local+sync | MongoDB Realm / Realm Kotlin SDK (+ Realm Sync opcional) | Permite persistencia local y sincronización automática con Atlas sin backend |
| Serialización | kotlinx-serialization / kbson | Compatibilidad con Kotlin y BSON/JSON |
| Testing | JUnit5, MockK, Realm in-memory | Permite pruebas unitarias y pruebas con realm local en memoria |

---

## 📅 Plan de Acción (Pasito a Pasito)

(Se listan tareas con prefijos REQ- y TASK-; cada TASK apunta a archivos y símbolos concretos)

### Requerimientos funcionales (REQ-)
- REQ-01: Persistencia local de registros (`record`, `date`).
- REQ-02: Opcional: sincronización segura con MongoDB Atlas (Realm Sync).
- REQ-03: Soporte para inserción, consulta y listado en UI.
- REQ-04: No exponer credenciales en repo; usar `local.properties` o variables en CI.

### Fase 0 — Asunciones iniciales (antes de tocar código)
- Proyecto Android Kotlin con Gradle (KTS), minSdk >= 21.
- Uso de AndroidX.
- App tiene acceso a Internet.
- Dispones (o crearás) una cuenta en MongoDB Atlas.
- Objetivo: persistencia local obligatoria; sincronización cloud (opcional) mediante Realm Sync.

(Nota: si alguna asunción no se cumple, indicar y ajustar plan.)

### Fase 1 — Preparación y dependencias
TASK-01: Añadir dependencias y plugin Realm en `app/build.gradle.kts` y, si procede, en `build.gradle.kts` de proyecto.  
- Archivo: `app/build.gradle.kts` (insertar en dependencies y plugins).
- Archivo: `build.gradle.kts` (classpath si el plugin lo requiere).
- Añadir permiso INTERNET en `app/src/main/AndroidManifest.xml`.

TASK-02: Crear paquetes y clases (borrador de rutas).
- `app/src/main/kotlin/com/example/simon/data/mongodb/MongoDbClient.kt`
- `app/src/main/kotlin/com/example/simon/data/model/RecordEntity.kt`
- `app/src/main/kotlin/com/example/simon/data/repository/MongoRepository.kt`
- `app/src/main/kotlin/com/example/simon/ui/RecordViewModel.kt`
- (Opcional) `app/src/main/kotlin/com/example/simon/SimonApplication.kt` (Application subclass)

### Fase 2 — Modelado y configuración Atlas/Realm
TASK-03: Modelar `RecordEntity` y definir esquemas Realm.  
TASK-04: En Atlas: crear cluster, DB, colección `records`, Realm App, habilitar auth (anónima) y Sync (si se desea).

### Fase 3 — Implementación de cliente Realm + CRUD
TASK-05: Inicializar Realm/RealmApp en `Application`.  
TASK-06: Autenticación anónima y apertura de Realm (o sincronización).  
TASK-07: Implementar `MongoRepository` con funciones: insertRecord(record: String), getAllRecords(): Flow<List<RecordEntity>>.  
TASK-08: Integrar `RecordViewModel` y exponer LiveData/StateFlow a UI.  

### Fase 4 — Tests y verificación
TASK-09: Escribir tests unitarios para serialización y testing in-memory Realm.  
TASK-10: Ejecutar `./gradlew assembleDebug` y `./gradlew test`.

### Fase 5 — Pull Request y rollout
TASK-11: Crear rama `feature/mongodb-client`, PR hacia `develop`, tag/version bump (ver semver).

---

## 🔧 Cambios exactos en archivos y rutas (lista detallada)

A continuación se listan los archivos que se deberán tocar o crear. Para cada archivo indico la ruta exacta y una breve nota del cambio.

1. Modificar: `app/build.gradle.kts`
   - Agregar plugin de Realm (si procede) y dependencias:
     - Sección `plugins { ... }` añadir `id("realm-android")` o `id("io.realm.kotlin")` según SDK.
     - Sección `dependencies` añadir `io.realm.kotlin:library-base:1.10.0` y `io.realm.kotlin:sync:1.10.0` (ver versión en notas).
   - Línea aproximada: plugins al inicio (1-20), dependencies en bloque de 40-120.

2. Posible modificación: `build.gradle.kts` (raíz)
   - Agregar classpath del plugin Realm si se requiere (líneas iniciales del buildscript).

3. Modificar: `app/src/main/AndroidManifest.xml`
   - Añadir permiso:
     - <uses-permission android:name="android.permission.INTERNET" />
   - Aproximado: dentro del bloque manifiesto, líneas iniciales.

4. Crear: `app/src/main/kotlin/com/example/simon/SimonApplication.kt`
   - Inicialización de Realm/RealmApp aquí.
   - Ruta: `app/src/main/kotlin/com/example/simon/SimonApplication.kt`

5. Crear: `app/src/main/kotlin/com/example/simon/data/model/RecordEntity.kt`
   - Kotlin data class / Realm model (ver sección Modelos).
   - Ruta y nombre exacto.

6. Crear: `app/src/main/kotlin/com/example/simon/data/mongodb/MongoDbClient.kt`
   - Clase responsable de inicializar Realm App, login y obtener Realm instance (API de alto nivel).

7. Crear: `app/src/main/kotlin/com/example/simon/data/repository/MongoRepository.kt`
   - Implementa operaciones CRUD (insert, query, observe).

8. Crear/Modificar: `app/src/main/kotlin/com/example/simon/ui/RecordViewModel.kt`
   - ViewModel que usa `MongoRepository`.

9. Tests:
   - Crear: `app/src/test/kotlin/com/example/simon/data/RecordEntityTest.kt`
   - Crear: `app/src/test/kotlin/com/example/simon/data/MongoRepositoryTest.kt` (Realm in-memory).

10. Documentación:
   - Crear: `/plan/feature-mongodb-client-1.md` (este archivo).
   - Actualizar Readme si procede: `Readme.md` con instrucciones de configuración Atlas.

---

## 🧭 Especificación del modelo de datos (Kotlin)

Archivo: `app/src/main/kotlin/com/example/simon/data/model/RecordEntity.kt`

- Definición (orientativa, compatible con Realm Kotlin):
  - Campos:
    - `id: String` — identificador (ObjectId como String o `org.bson.types.ObjectId` según SDK).
    - `record: String` — texto libre o JSON serializado.
    - `date: String` (ISO-8601) o `Instant`/`Date` según compatibilidad SDK.
- Ejemplo de data class / Realm model (concepto):

RecordEntity (concepto)
- id: ObjectId / String
- record: String
- date: String (ISO-8601) o Instant

Serialización y conversiones:
- En la app, al crear: obtener instante `Instant.now()` y formatear con `java.time.Instant` -> `toString()` (ISO-8601).
- Si el SDK soporta `org.bson.types.ObjectId`, convertir `ObjectId().toHexString()` para guardarlo como String o usar el tipo ObjectId directo si el SDK lo permite.

Notas:
- Realm Kotlin puede requerir que el modelo implemente/extends `RealmObject` o use anotación `@RealmClass` (depende de la versión). Ajustar la clase a la API del SDK usado.

---

## ⚙️ Pasos de configuración en MongoDB Atlas / Realm (panel)

1. Crear cuenta / iniciar sesión:
   - URL: https://cloud.mongodb.com
   - Si no tienes cuenta: Sign up → crear cuenta gratuita.

2. Crear un cluster:
   - Panel: Atlas → Build a Cluster → Create a Free Tier cluster (o usar cluster existente).
   - Nombre sugerido: `simon-cluster`.

3. Crear base de datos y colección:
   - Atlas → Collections → Create Database
   - Database name: `simon_db`
   - Collection name: `records`

4. Crear App Realm (MongoDB Realm):
   - Menú izquierdo: Realm → Create a New App
   - Vincular al cluster `simon-cluster`.
   - App Name: `simon-app`
   - Obtener App ID: lo encontrarás en Realm → App Settings → General → App ID. (Se usará en la app móvil)

5. Autenticación:
   - Realm → Authentication → Providers → Enable `Anonymous` (para desarrollo).
   - Opcional: habilitar `Email/Password` para producción y usuarios registrados.

6. Sync (opcional — si quieres sincronización):
   - Realm → Sync → Enable Flexible Sync (o Partition-based Sync según preferencia).
   - Mapear la colección `simon_db.records` para sincronizar.
   - Configurar reglas para lectura/escritura: para desarrollo, permitir escritura/lectura a usuarios autenticados; para producción, definir reglas que limiten por usuario/propiedad.

7. Rules de seguridad (mínimas para pruebas):
   - Realm → Rules → seleccionar `records` collection → editar `read`/`write` a `true` para `%%true` (solo en entorno de desarrollo).
   - Para producción: usar `request.auth != null` y validaciones.

8. Obtener App ID y configuración:
   - Guardar `Realm App ID` en `local.properties` como `MONGODB_REALM_APP_ID=tu-app-id` o como `gradle.properties`/CI secret.

---

## 🧩 Código de ejemplo (Kotlin) — snippets completos (ejemplos orientativos)

Nota: Los snippets siguientes son ejemplos integrados al plan. Ajustar imports y versiones según SDK elegido.

1) Inicializar Realm/RealmApp en `SimonApplication.kt` (concepto)

```kotlin
// app/src/main/kotlin/com/example/simon/SimonApplication.kt
package com.example.simon

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration

class SimonApplication : Application() {
    companion object {
        lateinit var realmApp: App
    }

    override fun onCreate() {
        super.onCreate()
        val appId = BuildConfig.MONGODB_REALM_APP_ID // aportado desde BuildConfig o local.properties
        realmApp = App.create(AppConfiguration.Builder(appId).build())
        // Configuración adicional opcional...
    }
}
```

2) Autenticación anónima y abrir Realm (MongoDbClient.kt)

```kotlin
// app/src/main/kotlin/com/example/simon/data/mongodb/MongoDbClient.kt
package com.example.simon.data.mongodb

import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MongoDbClient(private val app: App) {

    suspend fun loginAnonymously(): User {
        return withContext(Dispatchers.IO) {
            val result = app.login(Credentials.anonymous())
            result
        }
    }

    fun openLocalRealm(config: RealmConfiguration): Realm {
        return Realm.open(config)
    }

    // Si Sync está habilitado, construir RealmConfiguration con sync
}
```

3) Insertar documento `{ record: ..., date: ... }` (MongoRepository.kt)

```kotlin
// app/src/main/kotlin/com/example/simon/data/repository/MongoRepository.kt
package com.example.simon.data.repository

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class MongoRepository(private val realm: Realm) {

    fun insertRecord(recordText: String) {
        realm.writeBlocking {
            val rec = RecordEntity().apply {
                id = org.bson.types.ObjectId().toHexString() // si usas String id
                record = recordText
                date = Instant.now().toString()
            }
            copyToRealm(rec)
        }
    }

    fun getAllRecords(): Flow<List<RecordEntity>> {
        return realm.query<RecordEntity>().asFlow().map { results ->
            results.list.map { it.copy() } // map a objetos inmutables si se necesita
        }
    }

    fun close() {
        realm.close()
    }
}
```

4) ViewModel de ejemplo (RecordViewModel.kt)

```kotlin
// app/src/main/kotlin/com/example/simon/ui/RecordViewModel.kt
package com.example.simon.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecordViewModel(private val repo: MongoRepository) : ViewModel() {

    val records = repo.getAllRecords()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addRecord(text: String) {
        viewModelScope.launch {
            repo.insertRecord(text)
        }
    }

    override fun onCleared() {
        repo.close()
        super.onCleared()
    }
}
```

5) Cerrar / limpiar recursos
- Llamar a `realm.close()` en `onCleared()` del ViewModel o en `onDestroy()` del componente que abrió la Realm instance.

Notas de coherencia:
- Asegurar que `RecordEntity` sea una clase compatible con Realm (puede requerir `open class RecordEntity: RealmObject { ... }` o la interfaz `RealmObject`).
- Ajustar llamadas `writeBlocking`/`write` según la API de la versión de Realm.

---

## 🧾 Cambios en Gradle: versiones y snippets (Kotlin DSL)

Asunción de versiones (revisar y ajustar con la documentación oficial en el momento de implementación):

- Realm Kotlin: 1.10.0 (ejemplo)
- Kotlin stdlib: usar la version del proyecto
- Coroutines: 1.6.4 (ejemplo)

Ejemplo - `app/build.gradle.kts` (fragmento)

```kotlin
plugins {
    id("com.android.application")
    kotlin("android")
    id("io.realm.kotlin") version "1.10.0" // si el plugin lo requiere
}

android {
    // ... existing configuration ...
}

dependencies {
    implementation("io.realm.kotlin:library-base:1.10.0")
    implementation("io.realm.kotlin:sync:1.10.0") // Si usas Sync
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}
```

`gradle.properties` (opcionales)
```
MONGODB_REALM_APP_ID=tu-app-id-aqui
```
(Recomendado: no subir `gradle.properties` con secretos; mejor usar `local.properties` o CI secrets.)

---

## ✅ Pruebas y verificación

Pruebas manuales:
1. Build y run:
   - Ejecutar: `./gradlew assembleDebug` (PowerShell: `.\gradlew.bat assembleDebug`)
2. Ejecutar la app en un emulador/dispositivo con Internet.
3. Desde UI, crear un registro; verificar:
   - Local: listado aparece inmediatamente.
   - Si Sync activo: verificar en Atlas → Collections → `simon_db.records` que el documento con `record` y `date` exista.

Pruebas automáticas:
- Unit test: serialización de `RecordEntity` a JSON/KBSON.
- In-memory Realm test:
  - Crear RealmConfiguration en memoria y ejecutar inserción/consulta.
  - Archivos de ejemplo:
    - `app/src/test/kotlin/com/example/simon/data/RecordEntityTest.kt`
    - `app/src/test/kotlin/com/example/simon/data/MongoRepositoryTest.kt`

Ejemplo de test unitario (concepto):
```kotlin
@Test
fun recordSerialization_and_insertInMemoryRealm() {
    val config = RealmConfiguration.Builder(schema = setOf(RecordEntity::class))
        .inMemory(true)
        .build()
    val realm = Realm.open(config)
    val repo = MongoRepository(realm)
    repo.insertRecord("test")
    val list = runBlocking { repo.getAllRecords().first() }
    assertTrue(list.any { it.record == "test" })
    realm.close()
}
```

Comandos para pruebas:
- `\.\gradlew.bat test` (Windows PowerShell)
- `\.\gradlew.bat connectedAndroidTest` (para instrumented tests)

---

## 🧪 Calidad y gates (Build / Lint / Tests)

Checklist de verificación antes de PR:
- [ ] `\.\gradlew.bat assembleDebug` → PASS
- [ ] `\.\gradlew.bat test` → PASS
- [ ] Lint (si existe) `\.\gradlew.bat lint` → PASS
- [ ] Verificación visual en emulador → PASS
- [ ] Ver en Atlas que documento aparece (si Sync activo) → PASS

Cómo deshacer cambios (rollback):
- Revertir commits en la rama feature o reset a `develop`:
  - `git checkout develop; git pull`
  - `git checkout -b feature/mongodb-client` para rehacer
  - Para deshacer commits locales: `git reset --hard origin/develop`

---

## ⏱️ Estimación de esfuerzo por tarea y checklist ejecutable

Estimación total: 2–5 días (según familiaridad con Realm y Atlas).

Tareas desglosadas con estimación:

- TASK-01 (Deps & manifest): 0.5 día
- TASK-02 (Clases y paquetes): 0.5 día
- TASK-03 (Schemas & modelos): 0.5 día
- TASK-04 (Atlas + Realm App config): 0.5 día
- TASK-05 (Init + Auth): 0.5 día
- TASK-06 (CRUD + Repo): 1 día
- TASK-07 (ViewModel + UI hookup): 0.5 día
- TASK-08 (Tests): 0.5 día
- TASK-09 (PR + revisión): 0.5 día

Checklist ejecutable (marcar cuando hecho):
- REQ-01 [ ] Persistencia local implementada
- REQ-02 [ ] Auth anónima y App ID guardado
- REQ-03 [ ] Insert y consulta funcionando
- REQ-04 [ ] Tests unitarios passing
- TASK-PR [ ] Rama `feature/mongodb-client` creada
- TASK-PR [ ] PR hacia `develop` con descripción y checklist

Criterios de aceptación:
- CA-1: Insertar un record con fecha crea un documento en la base local.
- CA-2: Si Sync habilitado: documento aparece en Atlas → `simon_db.records` con `date` en formato ISO-8601.
- CA-3: Tests locales pasan (`./gradlew test`).
- CA-4: No hay credenciales en el repo.

---

## 🔐 Notas de seguridad y privacidad

- Nunca subir App ID/secret/keys a VCS. Usar:
  - `local.properties` (no commit) o
  - variables de entorno / secrets en CI.
- Reglas de Realm: en producción, usar auth real (email/password o JWT) y reglas que validen `request.auth != null` y que limiten acceso por `owner_id`.
- Habilitar TLS (Atlas lo hace por defecto).
- Monitorizar logs de Realm y uso de datos.

---

## 📦 Entregables (archivos nuevos y modificados)

Archivos nuevos:
- `app/src/main/kotlin/com/example/simon/SimonApplication.kt`
- `app/src/main/kotlin/com/example/simon/data/model/RecordEntity.kt`
- `app/src/main/kotlin/com/example/simon/data/mongodb/MongoDbClient.kt`
- `app/src/main/kotlin/com/example/simon/data/repository/MongoRepository.kt`
- `app/src/main/kotlin/com/example/simon/ui/RecordViewModel.kt`
- `app/src/test/kotlin/com/example/simon/data/RecordEntityTest.kt`
- `app/src/test/kotlin/com/example/simon/data/MongoRepositoryTest.kt`
- `plan/feature-mongodb-client-1.md` (este archivo)

Archivos modificados:
- `app/build.gradle.kts` (agregar dependencias y plugin)
- `build.gradle.kts` (si el plugin del proyecto lo requiere)
- `app/src/main/AndroidManifest.xml` (agregar permiso INTERNET)
- `Readme.md` (documentación de integración Atlas) — opcional

---

## 📦 Plan de rollout (gitflow + semver)

- Crear rama desde `develop`: `feature/mongodb-client`
- Implementar cambios y commitear incrementalmente.
- Abrir PR: `feature/mongodb-client -> develop` con checklist (CA-1..CA-4).
- Revisiones: corregir; cuando esté listo merge a `develop`.
- Versionado: como es nueva funcionalidad, bump MINOR según SemVer:
  - Ejemplo: si versión actual `x.y.z`, proponer `x.(y+1).0`.
- Para hotfix urgente: usar `hotfix/...` y proceder por gitflow.

---

## 🧾 Notas finales y consideraciones (trade-offs)

- Elección: usar Realm Sync (Device Sync / Flexible Sync) vs solo Realm local:
  - Realm Sync PROS: sincronización automática, conflict resolution, offline-first.
  - Realm Sync CONS: configuración en Atlas, límites de cuota en la capa gratuita, complejidad adicional y reglas de seguridad.
  - Si solo necesitas persistencia local y simplicidad, usar Realm local (sin Sync) y opcionalmente exponer un job/función para replicar datos a Atlas más tarde.
- Recomendación: empezar con persistencia local + login anónimo; luego habilitar Sync en entorno de staging y probar.

---

## 📌 Siguientes pasos recomendados (acción inmediata)
1. Confirmar las asunciones: minSdk, paquete base (ej.: `com.example.simon`), y si se usará Sync.
2. Proveer App ID de Realm por `local.properties` (o crear cuenta Atlas).
3. Implementar TASK-01 y TASK-02 (deps y skeleton) en `feature/mongodb-client`.

Resumen de verificación rápida (Checklist final antes de cerrar la feature):
- [ ] Build: `\.\gradlew.bat assembleDebug` → PASS
- [ ] Tests: `\.\gradlew.bat test` → PASS
- [ ] Run: iniciar app y crear registro (UI) → aparece localmente
- [ ] Ver en Atlas: documento aparece en `simon_db.records` (si Sync ON)

---

Fin del plan.

¿Quieres que adapte las versiones de dependencia a las últimas publicadas (consulto documentación oficial y proporciono snippets concretos con la versión exacta) o prefieres que deje las versiones ejemplo indicadas y continúe con un PR/patch-ready listo para aplicar?
