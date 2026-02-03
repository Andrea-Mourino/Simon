package com.example.simon

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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
            "puntuacion DESC",
            "1"
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
        return obtenerRecord(context)
    }
}