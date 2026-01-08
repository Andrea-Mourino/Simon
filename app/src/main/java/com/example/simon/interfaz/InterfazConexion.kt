package com.example.simon.interfaz

import android.content.Context

//interface sirve para desacoplar la capa de datos del ViewModel en caso de que cambiara el SharePreferences pues el ViewModel no se veria afectado
interface InterfazConexion {
    fun obtenerRecord(context: Context):Record //Esto declara una función que debe devolver un Record dado un Context
    fun actualizarRecord(record:Int,context: Context):Record //Esto declara una función que debe actualizar el record y devolver el record actualizado
    //También recibe Context
}