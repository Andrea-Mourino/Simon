package com.example.simon.shared_preference

import android.content.Context

/**
 * Esta interfaz sirve para futuro para extrapolar el programa e integrar una base de datos
 *
 * https://developer.android.com/reference/android/content/SharedPreferences
 */
interface Shared_conexion {
    fun obtenerRecord(context: Context):Shared_record
    fun actualizarRecord(record:Int,context: Context):Shared_record
}