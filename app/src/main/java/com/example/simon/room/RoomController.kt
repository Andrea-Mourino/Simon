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
        // Construye la instancia de la base de datos
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "base-record" // Nombre del archivo de la base de datos
        ).fallbackToDestructiveMigration() // Permite que Room recree la base de datos si cambia el esquema
            .allowMainThreadQueries().build() // PERMITE consultas en el hilo principal (cuidado: puede ralentizar la app)

        // Pide al DAO el último récord. Si es null (primera partida), devuelve un Record de 0
        val miRecord = db.recordDao().getRecord() ?: return Record(0, LocalDate.now(), "Jugador")
        db.close() // Cerramos la conexión para no gastar memoria del dispositivo

        // Convierte el Long de la base de datos de nuevo a un objeto LocalDate para que la IU lo entienda.
        return Record(miRecord.record, Instant.ofEpochSecond(miRecord.fecha).atZone(ZoneId.systemDefault()).toLocalDate(), miRecord.playerName)
    }

    override fun actualizarRecord(record:Int, playerName: String, context: Context): Record {
        val fechaActual = LocalDate.now()
        // Convertimos la fecha de hoy a un número largo (EpochSecond) para poder guardarlo.
        val fechaLong = fechaActual.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()

        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "base-record"
        ).fallbackToDestructiveMigration() // Permite que Room recree la base de datos si cambia el esquema
            .allowMainThreadQueries().build()

        // Creamos la entidad y la insertamos usando el DAO.
        db.recordDao().insert(RercordEntity(record = record, fecha = fechaLong, playerName = playerName))
        db.close()

        return Record(record, fechaActual, playerName)
    }


}