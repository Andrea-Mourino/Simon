package com.example.simon;

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.media.AudioManager
import android.media.ToneGenerator
class MyViewModel(): ViewModel() {

    private val TAG_LOG = "miDebug"

    val estadoActual = MutableStateFlow(Estados.INICIO)

    var _listaSecuencia = MutableStateFlow<List<Int>>(emptyList())
    var _numbers = MutableStateFlow(0)
    var _nSecuenciaActual: MutableStateFlow<Int> = MutableStateFlow(0)
    var _ronda = MutableStateFlow(0)
    // Color que se está mostrando en este momento (por defecto -1 significa ninguno)
    var _colorActivo: MutableStateFlow<Int> = MutableStateFlow(-1)

    var _colorPulsado: MutableStateFlow<Int> = MutableStateFlow(-1)

    val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)


    init {
        Log.d(TAG_LOG, "Inicializamos ViewModel - Estado: ${estadoActual.value}")
    }

    fun generarNNuevo() {
        estadoActual.value = Estados.GENERANDO
        _numbers.value = (0..3).random()
        Log.d(TAG_LOG, "creamos random ${_numbers.value} - Estado: ${estadoActual.value}")
        setNNuevo(_numbers.value)
    }

    fun comprobar(numeroAdivinar: Int) {
        _colorPulsado.value = numeroAdivinar
             if (numeroAdivinar == Datos.secuencia[_nSecuenciaActual.value]) {
                 Log.d(TAG_LOG, "adivinaste - Estado: ${estadoActual.value}")
                 viewModelScope.launch {
                     hacerSonido(_colorPulsado.value)
                     botonPulsado()
                 }
             } else {
                 hacerSonido(-1)
                 estadoActual.value = Estados.REINICIANDO
                 reiniciarJuego()
            }
    }

    suspend fun botonPulsado(){
        estadoActual.value = Estados.PULSADO
        delay(200)
        setnSecuencia()
    }

    suspend fun mostrarColores(){
        _colorActivo.value = -1
        delay(200)
        for (color in Datos.secuencia) {
            _colorActivo.value = color
            hacerSonido(_colorActivo.value)
            delay(500)
            _colorActivo.value = -1
            delay(200)
        }
        estadoActual.value = Estados.ADIVINANDO
        Log.d(TAG_LOG, "Tu turno - Estado: ${estadoActual.value}")
    }

    fun hacerSonido(color: Int){
        when (color) {
            0 -> sonidoDo()
            1 -> sonidoMi()
            2 -> sonidoSol()
            3 -> sonidoDoGrave()
            else -> sonidoError()
        }
    }

    fun sonidoDo() {
        Log.d(TAG_LOG, "Pulsado Do agudo")
        tone.startTone(ToneGenerator.TONE_DTMF_1, 200)
    }

    fun sonidoMi() {
        Log.d(TAG_LOG, "Pulsado Mi")
        tone.startTone(ToneGenerator.TONE_DTMF_3, 200)
    }

    fun sonidoSol() {
        Log.d(TAG_LOG, "Pulsado Sol")
        tone.startTone(ToneGenerator.TONE_DTMF_7, 200)
    }

    fun sonidoDoGrave() {
        Log.d(TAG_LOG, "Pulsado Do grave")
        tone.startTone(ToneGenerator.TONE_DTMF_9, 200)
    }

    fun sonidoError() {
        Log.d(TAG_LOG, "Sonido de error")
        tone.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_INTERGROUP, 300)
    }


    fun reiniciarJuego(){
        Log.d(TAG_LOG, "fallaste,has perdido, reiniciando - Estado: ${estadoActual.value}")
        Log.d(TAG_LOG, "nivel alcanzado: ${Datos.ronda}")

        _listaSecuencia.value = emptyList()
        _nSecuenciaActual.value = 0
        _ronda.value = 0
        Datos.ronda = _ronda.value
        estadoActual.value = Estados.INICIO
    }

    fun setNNuevo(numero: Int) {
        Log.d(TAG_LOG, "actualizamos numero en Datos - Estado: ${estadoActual.value}")
        Datos.numero = numero
        _listaSecuencia.value += numero
        setSecuencia(_listaSecuencia.value)
    }

    fun setSecuencia(list: List<Int>){
        Datos.secuencia = list
        Log.d(TAG_LOG, "chuleta: ${_listaSecuencia.value}")
        estadoActual.value = Estados.MOSTRANDO
        Log.d(TAG_LOG, "MOSTRANDO COLORESS - Estado: ${estadoActual.value}")
        viewModelScope.launch {
            mostrarColores()
        }
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
            estadoActual.value = Estados.ADIVINANDO
            Log.d(TAG_LOG, "dime el siguiente numero - Estado: ${estadoActual.value}")
            _nSecuenciaActual.value ++
        }
    }
}