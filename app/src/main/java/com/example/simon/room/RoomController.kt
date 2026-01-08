package com.example.simon.room

import android.content.Context
import androidx.room.Room
import com.example.simon.interfaz.InterfazConexion
import java.time.LocalDate
import java.util.Date
import com.example.simon.app.Record
import java.time.Instant
import java.time.ZoneId


object RoomController: InterfazConexion {

    override fun obtenerRecord(context: Context): Record {

        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "base-record"
        ).allowMainThreadQueries().build()
        val miRecord = db.recordDao().getRecord() ?: return Record(0, LocalDate.now())
        db.close()
        return Record(miRecord.record, Instant.ofEpochSecond(miRecord.fecha).atZone(ZoneId.systemDefault()).toLocalDate())
    }

    override fun actualizarRecord(record:Int,context: Context): Record {
        val fechaActual = LocalDate.now()
        val fechaLong = fechaActual.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "base-record"
        ).allowMainThreadQueries().build()
        val miNuevoRecord =db.recordDao().insert(RercordEntity(record = record, fecha = fechaLong))
        db.close()
        return Record(record, fechaActual)
    }


}