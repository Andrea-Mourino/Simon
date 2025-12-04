package com.example.simon

import android.content.Context

interface InterfazConexion {
    fun obtenerRecord(context: Context):Record
    fun actuaizarRecord(record:Int,context: Context):Record
}