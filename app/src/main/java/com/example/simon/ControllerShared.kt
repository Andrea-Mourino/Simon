package com.example.simon

import android.content.Context //Necesario para acceder a SharedPreferences
import androidx.core.content.edit //Permite usar la extensión edit { }
import java.time.LocalDate //Para manejar fechas de manera nativa


object ControllerShared : InterfazConexion{ //InterfazConexion permite definir como obtener y actualizar un record, lo que permite modular la persistencia
    private const val PREFS_NAME = "preferencias_app" //Nombre del archivo
    private const val KEY_RECORD = "record" //Clave para almacenar la ronda mas alta
    private const val KEY_FECHA = "date" //Clave para almacenar la fecha que se alcanzo el record
// pla pla
    override fun obtenerRecord(context: Context):Record{ //Obtiene el archivo de SharedPreferences en modo privado
        //Devuelve un objeto Record que contiene la ronda más alta y la fecha correspondiente
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) //Crea o abre el archivo preferencias_app para leer los valores guardados
        val recordValue:Int = sharedPreferences.getInt(KEY_RECORD,0) //Lee el valor entero del record y si no existe devuelve 0 por defecto
        val fechaString: String? = sharedPreferences.getString(KEY_FECHA,null) //Lee la fecha guardada como string
        //Si no existe devuelve null
        var r:Record? = null
        //Intenta convertir la cadena de fecha en un LocalDate, si falla usa la fecha actual y se crea el record con ronda y fecha
        try {
            val fechaFormat: LocalDate = LocalDate.parse(fechaString)
            r = Record(recordValue,fechaFormat)
        }catch (e: Exception){
            r = Record(recordValue, LocalDate.now())

        }
        return r //Devuelve el record leído o creado de forma segura
    }
    override fun actualizarRecord(nuevoRecord: Int, context: Context):Record{ //Permite guardar un nuevo record en SharedPreferences
        //Devuelve un objeto Record actualizado
        //Abre el archivo de preferencias
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
        val fechaActual = LocalDate.now()
        var fechaString = fechaActual.toString()
        //Obtiene la fecha actual y la convierte a string
        sharedPreferences.edit(){
            putInt(KEY_RECORD,nuevoRecord)
            putString(KEY_FECHA, fechaString)
        }
        return Record(nuevoRecord,fechaActual) //Guarda nuevo record y fecha actual en SharedPreferences

    }
}