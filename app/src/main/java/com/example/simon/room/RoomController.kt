package com.example.simon.room

import android.content.Context
import androidx.room.Room
import com.example.simon.interfaz.InterfazConexion
import java.util.Date

object RoomController: InterfazConexion {

    override fun obtenerRecord(context: Context): Record {

        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "base-record"
        ).allowMainThreadQueries().build()
        val miRecord = db.recordDao().getRecord() ?: return Record(0, Date())
        db.close()
        return Record(miRecord.record, Date(miRecord.fecha))
    }

    override fun actualizarRecord(
        nuevoRecord: Int,
        fecha: Date,
        context: Context
    ): Record {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "base-record"
        ).allowMainThreadQueries().build()
        val miNuevoRecord =db.recordDao().insert(RecordEntity(record = nuevoRecord, fecha = fecha.time))
        db.close()
        return Record(nuevoRecord, fecha)
    }


}