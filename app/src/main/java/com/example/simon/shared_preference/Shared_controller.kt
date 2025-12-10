package com.example.simon.shared_preference

import android.content.Context
import androidx.core.content.edit
import com.example.simon.GameConstants
import java.time.LocalDate

/**
 * Todala lógica sobre el shared, las funciones
 *
 * https://developer.android.com/reference/android/content/SharedPreferences
 */
object Shared_controller : Shared_conexion{
    /**
     * Obtenemos el record como la data class del "Shared_redord"
     *
     * @param context: sirve como intercomunicador entre clases
     * @return devuelve el record
     */
    override fun obtenerRecord(context: Context):Shared_record {
        val sharedPreferences = context.getSharedPreferences(GameConstants.PREFS_NAME, Context.MODE_PRIVATE) //prefs_name es como el idle
        val recordValue:Int = sharedPreferences.getInt(GameConstants.KEY_HIGH_SCORE,0) //recogemos la puntuacion
        val dateValue: String? = sharedPreferences.getString(GameConstants.KEY_HIGH_SCORE_DATE,LocalDate.now().toString()) // recogemos la fecha como string, default la fecha de hoy
        val fechaFormat: LocalDate = LocalDate.parse(dateValue)
        return Shared_record(recordValue,fechaFormat)//en caso de no haber record se nos guardaria lo default, que es la fecha actual con el record de 0
    }

    /**
     * Funcion para actualizar el record
     *
     * @param newRecord: nueva puntuación máxima
     * @return devuelve el record
     */
    override fun actualizarRecord(newRecord: Int, context: Context):Shared_record{
        val sharedPreferences = context.getSharedPreferences(GameConstants.PREFS_NAME,Context.MODE_PRIVATE)
        val fechaActualValue = LocalDate.now()
        var fechaStringValue = fechaActualValue.toString()
        sharedPreferences.edit(){//método para poder editar los values
            putInt(GameConstants.KEY_HIGH_SCORE,newRecord)
            putString(GameConstants.KEY_HIGH_SCORE_DATE, fechaStringValue)
        }
        return Shared_record(newRecord,fechaActualValue)
    }
}