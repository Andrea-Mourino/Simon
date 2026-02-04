---
tipo: "feature"
nombre: "persistence-layer"
version: 1
estado: "Planificado (🔵)"
agente: 'agente'
descripción: 'Plan paso a paso para implementar persistencia de datos con SQLite (records top-10) y Room (nombre del jugador) en la app Android/Kotlin Simon Game.'
herramientas: ['changes','search/codebase','edit/editFiles','runCommands/terminalLastCommand']
---

# 🚀 Plan de Desarrollo: persistence-layer

![Estado: Planificado](https://img.shields.io/badge/estado-Planificado-1f78ff)
![Prioridad: ALTA](https://img.shields.io/badge/prioridad-🚨_ALTA-red)
![Esfuerzo: MEDIO-ALTO](https://img.shields.io/badge/esfuerzo-⚖️_MEDIO_ALTO-yellow)

**Versión**: 1  
**Última actualización**: 2026-02-04  
**Creador/Responsable**: Equipo Simon ✨

## 🎯 Objetivo Principal

Implementar una capa de persistencia robusta en la app Simon Game que permita:

1. **SQLite Records [4 puntos]**: Guardar resultados de partidas en tabla `records` limitada a los 10 mejores, ordenados por puntuación (descendente) y fecha (ascendente en caso de empate). Mostrar máximo record en UI y loguear cuando se alcance top-10.

2. **Room Player Name [4 puntos]**: Guardar el nombre del jugador en tabla `player` usando Room. El nombre debe ser configurable mediante variable inicial en ViewModel y mostrarse junto al record en la UI.

---

## 🧐 Contexto y Motivación

### ¿Por qué me apetece hacer este proyecto?

- **Necesidad funcional**: Persistencia local de records y datos del jugador para gamificación.
- **Aprendizaje**: Dominar SQLite con Room, manejo de transacciones, ordenamiento y limitación de datos.
- **Mejora UX**: Mostrar progreso del jugador y records históricos.
- **Portafolio**: Demostrar buenas prácticas en persistencia de datos en Android.

### ¿Qué problema soluciona?

1. **Antes**: App sin persistencia — cada vez que se cierra, se pierden todos los datos.
2. **Después**: Records persistentes + nombre del jugador → experiencia completa de gamificación con historial.

---

## 🗺️ Alcance (Qué SÍ y qué NO)

### ✅ SÍ va a incluir

- [ ] Integración de Room en la app `app/` (DAO, Entity, Database).
- [ ] Modelo `RecordEntity` con `id`, `puntuacion` (ronda alcanzada), `fecha`.
- [ ] Modelo `PlayerEntity` con `id`, `nombre`.
- [ ] Implementación de lógica top-10 con filtrado por fecha en caso de empate.
- [ ] Repositorio de datos con operaciones CRUD.
- [ ] Integración en `MyViewModel` con `StateFlow` para reactividad.
- [ ] Actualización de UI en Compose para mostrar nombre + record máximo.
- [ ] Logcat logging cuando se alcanza top-10.
- [ ] Tests unitarios e instrumentalizados.
- [ ] Snippets de Gradle (KTS).
- [ ] Gitflow: ramas `feature/sqlite-records` y `feature/room-player-name`, merge a `develop`, release en `release/1.1`.

### ❌ NO va a incluir (por ahora)

- [ ] Sincronización cloud (futura: MongoDB/Realm).
- [ ] Exportación/importación de records.
- [ ] UI avanzada de leaderboard (solo mostrar máximo record).
- [ ] Migraciones de esquema complejas (solo versión inicial).
- [ ] Autenticación de usuario (nombre solo local).

---

## 🛠️ Kit de Herramientas (Stack Tecnológico)

| Categoría | Tecnología Elegida | ¿Por qué esta? |
|-----------|-------------------|----------------|
| Mobile | Android (Kotlin) | Proyecto existente en Kotlin/Gradle |
| DB local | Room (SQLite wrapper) | ORM tipado, DAO simplificado, recomendado por Google |
| Reactividad | Flow<> (coroutines) | Integración nativa con Compose, mejor que LiveData |
| Testing | JUnit 4/5, Espresso, Room in-memory | Testing de DB local sin emulador |
| Serialización | Room annotations (@Entity, @Dao) | Nativa de Room |

---

## 📅 Plan de Acción (Pasito a Pasito)

### Requerimientos funcionales (REQ-)

- **REQ-01**: Persistencia de records con puntuación (ronda alcanzada) y fecha.
- **REQ-02**: Tabla limitada a 10 mejores registros (DESC por puntuación, ASC por fecha en empate).
- **REQ-03**: Mostrar record máximo en UI.
- **REQ-04**: Loguear en logcat cuando se alcanza top-10.
- **REQ-05**: Persistencia de nombre del jugador.
- **REQ-06**: Nombre del jugador visible junto al record en UI.
- **REQ-07**: Nombre del jugador configurable mediante variable inicial en ViewModel.

### Fase 0 — Asunciones iniciales

- ✅ Proyecto Android Kotlin con Gradle (KTS), minSdk >= 21.
- ✅ Uso de Jetpack Compose para UI.
- ✅ MVVM con ViewModel existente (`MyViewModel`).
- ✅ App tiene acceso a almacenamiento local.
- ⚠️ **No hay autenticación de usuario**: nombre se cambia manualmente en código/variable.

---

## 🔧 Cambios exactos en archivos y rutas (lista detallada)

### A. Dependencias y configuración

#### TASK-A01: Actualizar `app/build.gradle.kts`

Archivo: `app/build.gradle.kts`

Agregar dependencias de Room en bloque `dependencies`:

```kotlin
// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
```

Agregar plugin `ksp` en bloque `plugins` si no existe:

```kotlin
id("com.google.devtools.ksp") version "1.9.20-1.0.13"
```

---

### B. Modelos de datos (Entities)

#### TASK-B01: Crear `app/src/main/kotlin/com/example/simon/data/model/RecordEntity.kt`

```kotlin
package com.example.simon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val puntuacion: Int,          // Ronda alcanzada (ej: 5)
    val fecha: Long = System.currentTimeMillis()  // Timestamp en ms
)
```

#### TASK-B02: Crear `app/src/main/kotlin/com/example/simon/data/model/PlayerEntity.kt`

```kotlin
package com.example.simon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player")
data class PlayerEntity(
    @PrimaryKey
    val id: Int = 1,  // Solo un jugador por app
    val nombre: String = "Jugador Anónimo"
)
```

---

### C. DAOs (Data Access Objects)

#### TASK-C01: Crear `app/src/main/kotlin/com/example/simon/data/dao/RecordDao.kt`

```kotlin
package com.example.simon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.simon.data.model.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    
    @Insert
    suspend fun insertRecord(record: RecordEntity)
    
    @Query("""
        SELECT * FROM records 
        ORDER BY puntuacion DESC, fecha ASC
        LIMIT 10
    """)
    fun getTop10Records(): Flow<List<RecordEntity>>
    
    @Query("SELECT MAX(puntuacion) FROM records")
    fun getMaxRecord(): Flow<Int?>
    
    @Query("DELETE FROM records WHERE id NOT IN (SELECT id FROM records ORDER BY puntuacion DESC, fecha ASC LIMIT 10)")
    suspend fun deleteOldRecords()
}
```

#### TASK-C02: Crear `app/src/main/kotlin/com/example/simon/data/dao/PlayerDao.kt`

```kotlin
package com.example.simon.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.simon.data.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    
    @Query("SELECT * FROM player WHERE id = 1")
    fun getPlayer(): Flow<PlayerEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePlayer(player: PlayerEntity)
}
```

---

### D. Database

#### TASK-D01: Crear `app/src/main/kotlin/com/example/simon/data/database/SimonDatabase.kt`

```kotlin
package com.example.simon.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simon.data.dao.RecordDao
import com.example.simon.data.dao.PlayerDao
import com.example.simon.data.model.RecordEntity
import com.example.simon.data.model.PlayerEntity

@Database(
    entities = [RecordEntity::class, PlayerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SimonDatabase : RoomDatabase() {
    
    abstract fun recordDao(): RecordDao
    abstract fun playerDao(): PlayerDao
    
    companion object {
        @Volatile
        private var instance: SimonDatabase? = null
        
        fun getInstance(context: Context): SimonDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SimonDatabase::class.java,
                    "simon_database"
                ).build().also { instance = it }
            }
        }
    }
}
```

---

### E. Repositorio

#### TASK-E01: Crear `app/src/main/kotlin/com/example/simon/data/repository/RecordRepository.kt`

```kotlin
package com.example.simon.data.repository

import com.example.simon.data.dao.RecordDao
import com.example.simon.data.model.RecordEntity
import kotlinx.coroutines.flow.Flow

class RecordRepository(private val recordDao: RecordDao) {
    
    suspend fun guardarRecord(puntuacion: Int) {
        val record = RecordEntity(puntuacion = puntuacion)
        recordDao.insertRecord(record)
        recordDao.deleteOldRecords()  // Limpiar si hay más de 10
    }
    
    fun obtenerTop10Records(): Flow<List<RecordEntity>> {
        return recordDao.getTop10Records()
    }
    
    fun obtenerMaxRecord(): Flow<Int?> {
        return recordDao.getMaxRecord()
    }
}
```

#### TASK-E02: Crear `app/src/main/kotlin/com/example/simon/data/repository/PlayerRepository.kt`

```kotlin
package com.example.simon.data.repository

import com.example.simon.data.dao.PlayerDao
import com.example.simon.data.model.PlayerEntity
import kotlinx.coroutines.flow.Flow

class PlayerRepository(private val playerDao: PlayerDao) {
    
    fun obtenerJugador(): Flow<PlayerEntity> {
        return playerDao.getPlayer()
    }
    
    suspend fun actualizarNombre(nombre: String) {
        playerDao.insertOrUpdatePlayer(PlayerEntity(id = 1, nombre = nombre))
    }
}
```

---

### F. ViewModel

#### TASK-F01: Modificar `app/src/main/java/com/example/simon/MyViewModel.kt`

Agregar campos y métodos:

```kotlin
// En clase MyViewModel
private val recordRepository: RecordRepository
private val playerRepository: PlayerRepository

// StateFlows
val maxRecord: StateFlow<Int?> = ...  // Observar desde recordRepository
val nombreJugador: StateFlow<String> = ...  // Observar desde playerRepository

// Variable mutable para cambiar nombre al iniciar
var playerNameInitial: String = "Jugador Anónimo"

// Al inicializar ViewModel (init block o constructor)
init {
    // Obtener máximo record
    recordRepository.obtenerMaxRecord()
        .onEach { max -> /* actualizar maxRecord */ }
        .launchIn(viewModelScope)
    
    // Obtener nombre del jugador
    playerRepository.obtenerJugador()
        .onEach { player -> /* actualizar nombreJugador */ }
        .launchIn(viewModelScope)
}

// Método para guardar record al finalizar partida
fun guardarRecordAlFinalizarPartida(rondasAlcanzadas: Int) {
    viewModelScope.launch {
        recordRepository.guardarRecord(rondasAlcanzadas)
        
        // Comprobar si entra en top 10 y loguear
        val top10 = recordRepository.obtenerTop10Records().first()
        val posicion = top10.indexOfFirst { it.puntuacion == rondasAlcanzadas } + 1
        if (posicion in 1..10) {
            Log.d("SimonGame", "🏆 ¡Record en top 10! Posición: $posicion con $rondasAlcanzadas puntos")
        }
    }
}

// Método para actualizar nombre del jugador
fun actualizarNombreJugador(nombre: String) {
    viewModelScope.launch {
        playerRepository.actualizarNombre(nombre)
    }
}
```

---

### G. UI (Compose)

#### TASK-G01: Modificar `app/src/main/java/com/example/simon/MainActivity.kt` o archivo con `IU()`

Actualizar la UI para mostrar nombre + record máximo:

```kotlin
@Composable
fun IU(viewModel: MyViewModel = viewModel()) {
    val maxRecord by viewModel.maxRecord.collectAsState(initial = null)
    val nombreJugador by viewModel.nombreJugador.collectAsState(initial = "Jugador Anónimo")
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar nombre + record
        Text(
            text = "$nombreJugador",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        if (maxRecord != null) {
            Text(
                text = "Record: $maxRecord",
                fontSize = 18.sp,
                color = Color.Green
            )
        }
        
        // ... resto del UI (botones de colores, etc.)
    }
}
```

---

### H. Tests

#### TASK-H01: Crear `app/src/test/kotlin/com/example/simon/data/RecordEntityTest.kt`

```kotlin
package com.example.simon.data

import com.example.simon.data.model.RecordEntity
import org.junit.Test
import org.junit.Assert.*

class RecordEntityTest {
    
    @Test
    fun `RecordEntity se crea correctamente`() {
        val record = RecordEntity(puntuacion = 10)
        assertEquals(10, record.puntuacion)
        assertEquals(1, record.id)  // Default
    }
    
    @Test
    fun `Múltiples records con misma puntuación se ordenan por fecha`() {
        val record1 = RecordEntity(id = 1, puntuacion = 10, fecha = 1000L)
        val record2 = RecordEntity(id = 2, puntuacion = 10, fecha = 2000L)
        
        val sorted = listOf(record2, record1).sortedWith(
            compareBy<RecordEntity> { -it.puntuacion }.thenBy { it.fecha }
        )
        
        assertEquals(record1.id, sorted[0].id)  // Más antiguo primero
    }
}
```

#### TASK-H02: Crear test instrumentalizado `app/src/androidTest/kotlin/com/example/simon/data/RecordDaoTest.kt`

```kotlin
package com.example.simon.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simon.data.database.SimonDatabase
import com.example.simon.data.dao.RecordDao
import com.example.simon.data.model.RecordEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class RecordDaoTest {
    
    private lateinit var database: SimonDatabase
    private lateinit var recordDao: RecordDao
    
    @Before
    fun setupDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SimonDatabase::class.java).build()
        recordDao = database.recordDao()
    }
    
    @After
    fun closeDatabase() {
        database.close()
    }
    
    @Test
    fun insertRecordAndRetrieve() = runBlocking {
        val record = RecordEntity(puntuacion = 5)
        recordDao.insertRecord(record)
        
        val records = recordDao.getTop10Records().first()
        assertEquals(1, records.size)
        assertEquals(5, records[0].puntuacion)
    }
    
    @Test
    fun limitTo10Records() = runBlocking {
        repeat(15) { i ->
            recordDao.insertRecord(RecordEntity(puntuacion = i + 1))
        }
        recordDao.deleteOldRecords()
        
        val records = recordDao.getTop10Records().first()
        assertEquals(10, records.size)
    }
}
```

---

### I. Manifest

#### TASK-I01: Verificar permisos en `app/src/main/AndroidManifest.xml`

Debe incluir permiso de acceso a almacenamiento local (ya debería estar):

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 📊 Tabla resumen de cambios

| Fase | Archivo/Ruta | Acción | Líneas aprox. |
|------|--------------|--------|--------------|
| A01 | `app/build.gradle.kts` | Añadir deps Room + ksp | +10 |
| B01 | `data/model/RecordEntity.kt` | Crear (NEW) | 15 |
| B02 | `data/model/PlayerEntity.kt` | Crear (NEW) | 12 |
| C01 | `data/dao/RecordDao.kt` | Crear (NEW) | 25 |
| C02 | `data/dao/PlayerDao.kt` | Crear (NEW) | 20 |
| D01 | `data/database/SimonDatabase.kt` | Crear (NEW) | 35 |
| E01 | `data/repository/RecordRepository.kt` | Crear (NEW) | 25 |
| E02 | `data/repository/PlayerRepository.kt` | Crear (NEW) | 20 |
| F01 | `MyViewModel.kt` | Modificar | +60 |
| G01 | `MainActivity.kt` / `IU.kt` | Modificar | +20 |
| H01 | `RecordEntityTest.kt` | Crear (NEW) | 30 |
| H02 | `RecordDaoTest.kt` | Crear (NEW) | 50 |

**Total de líneas nuevas**: ~322 (modelos, DAOs, repo, tests)  
**Total de líneas modificadas**: ~80 (ViewModel, UI)

---

## 🔄 Gitflow: Ramas y Comandos

### Rama 1: Feature SQLite Records

```bash
# Crear rama desde develop
git checkout develop
git pull origin develop
git checkout -b feature/sqlite-records

# ... Implementar TASK-A01 a E01, F01 (parcial), G01, H01, H02 ...

# Commit
git add .
git commit -m "feat: Add SQLite records persistence with top-10 filtering

- Create RecordEntity with puntuacion and fecha
- Implement RecordDao with top-10 query and auto-cleanup
- Create RecordRepository for CRUD operations
- Extend MyViewModel with guardarRecordAlFinalizarPartida()
- Add logcat logging for top-10 achievements
- Add tests for record entity and dao operations"

# Push
git push origin feature/sqlite-records
```

### Rama 2: Feature Room Player Name

```bash
# Crear rama desde develop
git checkout develop
git pull origin develop
git checkout -b feature/room-player-name

# ... Implementar TASK-B02, C02, D01 (actualizar), E02, F01 (parcial), G01, H01, H02 ...

# Commit
git add .
git commit -m "feat: Add Room persistence for player name

- Create PlayerEntity with nombre field
- Implement PlayerDao for get/update operations
- Extend SimonDatabase with PlayerEntity
- Create PlayerRepository for data access
- Add player name display in UI next to record
- Add configurable player name via ViewModel variable"

# Push
git push origin feature/room-player-name
```

### Merge a develop y Release

```bash
# Mergear feature/sqlite-records
git checkout develop
git pull origin develop
git merge --no-ff feature/sqlite-records -m "Merge branch 'feature/sqlite-records'"

# Mergear feature/room-player-name
git merge --no-ff feature/room-player-name -m "Merge branch 'feature/room-player-name'"

# Push a develop
git push origin develop

# Crear rama release
git checkout -b release/1.1
# ... Actualizar versionCode/versionName en build.gradle.kts a 1.1 ...
git add build.gradle.kts
git commit -m "chore: Bump version to 1.1 for persistence layer release"

# Mergear a main
git checkout main
git pull origin main
git merge --no-ff release/1.1 -m "Merge branch 'release/1.1'"

# Taggear
git tag -a v1.1 -m "Release v1.1 - SQLite Records Top-10 and Room Player Name Persistence"

# Mergear de vuelta a develop
git checkout develop
git merge --no-ff release/1.1

# Push
git push origin main
git push origin develop
git push origin --tags
```

---

## 🧪 Testing y Verificación

### Comandos de compilación y testing

```bash
# Compilar
./gradlew assembleDebug

# Tests unitarios
./gradlew test

# Tests instrumentalizados (requiere emulador/dispositivo)
./gradlew connectedAndroidTest

# Lint y análisis de código
./gradlew lint
```

### Verificaciones manuales

1. **Records persistencia:**
   - Jugar 3+ partidas con rondas 5, 8, 3.
   - Cerrar y reabrir app.
   - ✅ Verificar que records persisten.
   - ✅ Verificar logcat: `🏆 ¡Record en top 10! Posición: X`

2. **Player name:**
   - Cambiar `playerNameInitial` en ViewModel.
   - ✅ Verificar que aparece en UI.

3. **Top-10 límite:**
   - Jugar 15+ partidas.
   - ✅ Verificar BD solo tiene 10 registros máximo.

---

## 🚀 Orden de implementación recomendado

1. **TASK-A01**: Setup Gradle (deps, ksp).
2. **TASK-B01, B02**: Crear entidades (RecordEntity, PlayerEntity).
3. **TASK-C01, C02**: Crear DAOs.
4. **TASK-D01**: Crear Database.
5. **TASK-E01, E02**: Crear Repositorios.
6. **TASK-F01**: Extender ViewModel.
7. **TASK-G01**: Actualizar UI.
8. **TASK-H01, H02**: Escribir tests.
9. **Gitflow**: Merge y release.

---

## 📝 Notas adicionales

### Configuración inicial del nombre del jugador

En `MyViewModel`, al inicializar:

```kotlin
init {
    playerNameInitial = "Tu Nombre Aquí"  // Cambiar para diferentes tests
    viewModelScope.launch {
        playerRepository.actualizarNombre(playerNameInitial)
    }
}
```

### Migraciones futuras

Si en futuro se necesita cambiar schema (agregar campos a RecordEntity), incrementar `version` en `SimonDatabase`:

```kotlin
@Database(version = 2, ...)
abstract class SimonDatabase : RoomDatabase() {
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // SQL para migración
            }
        }
    }
}
```

---

## 📚 Referencias y recursos

- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Kotlin Coroutines + Flow](https://developer.android.com/kotlin/flow)
- [Jetpack Compose StateFlow](https://developer.android.com/jetpack/compose/state-management)
- [SQLite Best Practices](https://www.sqlite.org/bestpractice.html)
- [Android Testing Guide](https://developer.android.com/training/testing)

---

**¿Preguntas o ajustes necesarios?** Contacta al equipo Simon. ✨


