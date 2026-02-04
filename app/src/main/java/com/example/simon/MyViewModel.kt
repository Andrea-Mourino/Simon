package com.example.simon

import android.app.Application
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.simon.room.AppDatabase
import com.example.simon.room.Entity
import com.example.simon.room.ScoreEntity
import com.example.simon.room.UserEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG_LOG = "miDebug"

    // Estado del juego
    val estadoActual = MutableStateFlow(GameState.INICIO)
    var _listaSecuencia = MutableStateFlow<List<Int>>(emptyList())
    var _numbers = MutableStateFlow(0)
    var _nSecuenciaActual: MutableStateFlow<Int> = MutableStateFlow(0)
    var _ronda = MutableStateFlow(0)
    var _colorActivo: MutableStateFlow<Int> = MutableStateFlow(-1)
    var _colorPulsado: MutableStateFlow<Int> = MutableStateFlow(-1)

    var _jugador: MutableStateFlow<String> = MutableStateFlow("Player1")

    var _recordUser: MutableStateFlow<Int> = MutableStateFlow(0)


    // Audio
    private val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    // Room
    private val db: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "simon_records_db"
    ).build()
    private val recordDao = db.Dao()

    init {
        val rnds = (1..2).random()
        if (rnds == 1) {
            _jugador.value = "Player1"

        } else {
            _jugador.value = "Player2"
        }

        Log.d(TAG_LOG, "Inicializamos ViewModel - Estado: ${estadoActual.value}")
    }

    // ----------------------- FUNCIONES DEL JUEGO -----------------------

    fun generarNNuevo() {
        estadoActual.value = GameState.GENERANDO
        _numbers.value = (0..3).random()
        Log.d(TAG_LOG, "creamos random ${_numbers.value} - Estado: ${estadoActual.value}")
        setNNuevo(_numbers.value)
    }

    private suspend fun mostrarColores() {
        _colorActivo.value = -1
        delay(200)
        for (color in _listaSecuencia.value) {
            _colorActivo.value = color
            hacerSonido(_colorActivo.value)
            delay(500)
            _colorActivo.value = -1
            delay(200)
        }
        estadoActual.value = GameState.ADIVINANDO
        Log.d(TAG_LOG, "Tu turno - Estado: ${estadoActual.value}")
    }

    fun setNNuevo(numero: Int) {
        _listaSecuencia.value += numero
        estadoActual.value = GameState.MOSTRANDO
        viewModelScope.launch { mostrarColores() }
    }

    fun comprobar(numeroAdivinar: Int) {
        estadoActual.value = GameState.PULSADO
        _colorPulsado.value = numeroAdivinar

        if (numeroAdivinar == _listaSecuencia.value[_nSecuenciaActual.value]) {
            viewModelScope.launch {
                hacerSonido(_colorPulsado.value)
                delay(200)
                setnSecuencia()
            }
        } else {
            hacerSonido(-1)
            estadoActual.value = GameState.REINICIANDO
            reiniciarJuego()
        }
    }

    fun setnSecuencia() {
        if (_nSecuenciaActual.value == _ronda.value) {
            _nSecuenciaActual.value = 0
            setRonda()
        } else {
            estadoActual.value = GameState.ADIVINANDO
            _nSecuenciaActual.value++
        }
    }

    fun setRonda() {
        estadoActual.value = GameState.GENERANDO
        _ronda.value++
        generarNNuevo()
    }

    // ----------------------- FUNCIONES DE AUDIO -----------------------

    fun hacerSonido(color: Int) {
        when (color) {
            0 -> sonidoDo()
            1 -> sonidoMi()
            2 -> sonidoSol()
            3 -> sonidoDoGrave()
            else -> sonidoError()
        }
    }

    private fun sonidoDo() = tone.startTone(ToneGenerator.TONE_DTMF_1, 200)
    private fun sonidoMi() = tone.startTone(ToneGenerator.TONE_DTMF_3, 200)
    private fun sonidoSol() = tone.startTone(ToneGenerator.TONE_DTMF_7, 200)
    private fun sonidoDoGrave() = tone.startTone(ToneGenerator.TONE_DTMF_9, 200)
    private fun sonidoError() = tone.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_INTERGROUP, 300)

    // ----------------------- FUNCIONES DE RECORD -----------------------

    fun reiniciarJuego() {
        Log.d(TAG_LOG, "fallaste, has perdido - Nivel alcanzado: ${_ronda.value}")

        // Comprobamos record antes de reiniciar
        comprobarGuardarRecord(_ronda.value)

        // Reinicio de variables
        _listaSecuencia.value = emptyList()
        _nSecuenciaActual.value = 0
        _ronda.value = 0
        estadoActual.value = GameState.INICIO
    }

    /**
     * Función que comprueba el record actual y guarda uno nuevo si se supera
     */
    private fun comprobarGuardarRecord(puntuacionActual: Int) {
        viewModelScope.launch {

            try {
                val newUser = UserEntity(
                    id = 0,
                    name = _jugador.value
                )
                recordDao.insertUser( newUser)
                val bestRecord = recordDao.getBestGlobalScore()
                val recordValue = bestRecord?.score
                Log.d(TAG_LOG, "Record actual: $recordValue")
                if (puntuacionActual > (recordValue ?: 0)) {
                    val newRecord = ScoreEntity(
                        id = 0,
                        score = puntuacionActual,
                        time = "00:00", // Placeholder, se puede mejorar para calcular el tiempo real
                        timestamp = System.currentTimeMillis(),
                        userId =newUser.id
                    )
                    recordDao.insertScore(newRecord)
                    val puntuacionMaxUser = recordDao.getBestScoreForUser(newUser.id)?.score ?: 0
                    _recordUser.value = puntuacionMaxUser

                    Log.d(TAG_LOG, "¡Nuevo record guardado! $puntuacionActual")
                } else {
                    Log.d(TAG_LOG, "No superó el record: $recordValue")
                }
            } catch (e: Exception) {
                Log.e(TAG_LOG, "Error accediendo a la base de datos: ${e.message}")
            }
        }
    }
}
