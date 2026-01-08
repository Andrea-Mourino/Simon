package com.example.simon.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao // Marca esta interfaz como el Objeto de Acceso a Datos
interface RecordDao {
    @Query("SELECT * FROM records ORDER BY id DESC LIMIT 1") // Orden SQL: "Dame el último registro insertado"
    fun getRecord(): RercordEntity? // Devuelve la entidad o "null" si la tabla está vacía
    @Insert // Indica que esta función sirve para guardar nuevos datos.
    fun insert(record: RercordEntity)

}