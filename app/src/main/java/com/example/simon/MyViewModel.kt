package com.example.simon;

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MyViewModel(): ViewModel() {

    private val TAG_LOG = "miDebug"

    val estadoActual = MutableStateFlow(Estados.INICIO)

    var _listaSecuencia = MutableStateFlow<List<Int>>(emptyList())
    var _numbers = MutableStateFlow(0)
    var _nSecuenciaActual: MutableStateFlow<Int> = MutableStateFlow(0)
    var _ronda = MutableStateFlow(0)



    init {
        Log.d(TAG_LOG, "Inicializamos ViewModel - Estado: ${estadoActual.value}")
    }

    fun generarNNuevo() {
        estadoActual.value = Estados.GENERANDO
        _numbers.value = (0..3).random()
        Log.d(TAG_LOG, "creamos random ${_numbers.value} - Estado: ${estadoActual.value}")
        actualizarNNuevo(_numbers.value)
    }

    fun actualizarNNuevo(numero: Int) {
        Log.d(TAG_LOG, "actualizamos numero en Datos - Estado: ${estadoActual.value}")
        Datos.numero = numero
        _listaSecuencia.value += numero
        setSecuencia( _listaSecuencia.value)
    }

    fun comprobar(numeroAdivinar: Int) {
             if (numeroAdivinar == Datos.secuencia[_nSecuenciaActual.value]) {
                 Log.d(TAG_LOG, "adivinaste - Estado: ${estadoActual.value}")
                 setnSecuencia()
             } else {
                 estadoActual.value = Estados.REINICIANDO
                 reiniciarJuego()
            }
    }


    fun reiniciarJuego(){
        Log.d(TAG_LOG, "fallaste, reiniciando - Estado: ${estadoActual.value}")
        _listaSecuencia.value = emptyList()
        _nSecuenciaActual.value = 0
        _ronda.value = 0
        estadoActual.value = Estados.INICIO
    }

    fun setSecuencia(list: List<Int>){
        Datos.secuencia = list
        Log.d(TAG_LOG, "chuleta: ${_listaSecuencia.value}")
        estadoActual.value = Estados.ADIVINANDO
    }

    fun setRonda(){
        estadoActual.value = Estados.GENERANDO
        Log.d(TAG_LOG, "avanzando a la siguiente ronda - Estado: ${estadoActual.value}")
        _ronda.value ++
        Datos.ronda = _ronda.value
        generarNNuevo()
    }

    fun setnSecuencia(){
        if (_nSecuenciaActual.value == Datos.ronda){
            _nSecuenciaActual.value = 0
            setRonda()
        } else {
            Log.d(TAG_LOG, "dime el siguiente numero - Estado: ${estadoActual.value}")
            _nSecuenciaActual.value ++
        }
    }
}