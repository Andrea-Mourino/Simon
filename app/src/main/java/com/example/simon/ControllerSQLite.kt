package com.example.simon

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.time.LocalDate

class SimonDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "simon.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                puntuacion INTEGER NOT NULL,
                fecha TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS records")
        onCreate(db)
    }
}

object ControllerSQLite: InterfazConexion {
    private lateinit var dbHelper: SimonDatabaseHelper
    private const val TAG_LOG = "miDebug"

    private fun inicializar(context: Context) {
        if (!::dbHelper.isInitialized) {
            dbHelper = SimonDatabaseHelper(context)
        }
    }

    override fun obtenerRecord(context: Context): Record {
        inicializar(context)
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "records",
            null,
            null,
            null,
            null,
            null,
            "puntuacion DESC LIMIT 1"
        )

        var record = Record(0, LocalDate.now())

        if (cursor.moveToFirst()) {
            val puntuacion = cursor.getInt(cursor.getColumnIndexOrThrow("puntuacion"))
            val fechaString = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
            try {
                val fecha = LocalDate.parse(fechaString)
                record = Record(puntuacion, fecha)
            } catch (_: Exception) {
                record = Record(puntuacion, LocalDate.now())
            }
        }

        cursor.close()
        return record
    }

    override fun actualizarRecord(record: Int, context: Context): Record {
        inicializar(context)
        val db = dbHelper.writableDatabase

        val values = android.content.ContentValues().apply {
            put("puntuacion", record)
            put("fecha", LocalDate.now().toString())
        }

        db.insert("records", null, values)

        //Mantiene solo los 10 mejores records
        mantenerTop10Records(db)
        return obtenerRecord(context)
    }

    /**
     * Mantiene la tabla limitada a solo 10 records
     * Cuando hay empate en puntuación, se mantiene el más antiguo (por fecha)
     */
    private fun mantenerTop10Records(db: SQLiteDatabase) {
        // Obtener los IDs de los registros que se deben eliminar
        val cursor = db.rawQuery(
            """
            SELECT id FROM records 
            ORDER BY puntuacion DESC, fecha ASC 
            LIMIT -1 OFFSET 10
            """.trimIndent(),
            null
        )

        // Eliminar los registros que exceden el límite de 10
        if (cursor.moveToFirst()) {
            do {
                val idToDelete = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                db.delete("records", "id = ?", arrayOf(idToDelete.toString()))
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    /**
     * Verifica si un record está en el top 10
     * Devuelve la posición (1-10) o -1 si no está en el top 10
     */
    fun verificarSiEstáEnTop10(puntuacion: Int, context: Context): Int {
        inicializar(context)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT id FROM (
                SELECT id, puntuacion, fecha FROM records 
                ORDER BY puntuacion DESC, fecha ASC 
                LIMIT 10
            ) 
            WHERE puntuacion = ? 
            ORDER BY fecha ASC
            """.trimIndent(),
            arrayOf(puntuacion.toString())
        )
        var posicion = -1
        if (cursor.moveToFirst()) {
            // Obtener la posición del primer registro con esa puntuación
            val countCursor = db.rawQuery(
                """
                SELECT COUNT(*) as cnt FROM (
                    SELECT id, puntuacion, fecha FROM records 
                    ORDER BY puntuacion DESC, fecha ASC 
                    LIMIT 10
                ) 
                WHERE puntuacion > ?
                """.trimIndent(),
                arrayOf(puntuacion.toString())
            )
            if (countCursor.moveToFirst()) {
                posicion = countCursor.getInt(countCursor.getColumnIndexOrThrow("cnt")) + 1
            }
            countCursor.close()
        }
        cursor.close()
        return if (posicion in 1..10) posicion else -1
    }

    /**
     * Obtiene todos los records en el top 10
     */
    fun obtenerTop10Records(context: Context): List<Record> {
        inicializar(context)
        val db = dbHelper.readableDatabase
        val records = mutableListOf<Record>()
        val cursor = db.query(
            "records",
            null,
            null,
            null,
            null,
            null,
            "puntuacion DESC, fecha ASC",
            "10"
        )
        if (cursor.moveToFirst()) {
            do {
                val puntuacion = cursor.getInt(cursor.getColumnIndexOrThrow("puntuacion"))
                val fechaString = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
                try {
                    val fecha = LocalDate.parse(fechaString)
                    records.add(Record(puntuacion, fecha))
                } catch (_: Exception) {
                    records.add(Record(puntuacion, LocalDate.now()))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return records
    }
}