package com.example.simon;

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.simon.sqlite.SimonDatabaseHelper
import java.util.Date
import java.util.Locale

class MyViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * sqlite
     */
    private val dbHelper = SimonDatabaseHelper(application)
    // FECHA DEL RÉCORD ACTUAL
    var fechaRecord by mutableStateOf("")
    // MOSTRAR LISTA DE USUARIOS DESDE SQLITE
    var listaUsuariosTexto by mutableStateOf("CARGANDO USUARIOS...")
    var recordEnMemoria by mutableStateOf(0)
    /**
     *
     */
    private val TAG_LOG = "miDebug"
    val estadoActual = MutableStateFlow(GameState.INICIO)
    var _listaSecuencia = MutableStateFlow<List<Int>>(emptyList())
    var _numbers = MutableStateFlow(0)
    var _nSecuenciaActual: MutableStateFlow<Int> = MutableStateFlow(0)
    var _ronda = MutableStateFlow(0)
    var _colorActivo: MutableStateFlow<Int> = MutableStateFlow(-1)
    var _colorPulsado: MutableStateFlow<Int> = MutableStateFlow(-1)
    val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)


    /**
     * las cosas del sqlite
     */
    init {
        // AL CARGAR EL VIEWMODEL, BUSCAMOS EL RÉCORD MÁXIMO EN LA BASE DE DATOS SQLITE
        recordEnMemoria = dbHelper.obtenerMaximoRecord()
        fechaRecord = dbHelper.obtenerFechaDelRecord(recordEnMemoria)
        inicializarDatosPrueba()
        actualizarListaUsuariosUI()
        val pruebaId = dbHelper.obtenerRecordPorId(1)
        // Log.d("SQLITE_SIMON", "DATOS CARGADOS AL INICIO: Récord $recordEnMemoria ($fechaRecord)")
        Log.d("SQLITE_SIMON", "Prueba getRecordById(1): $pruebaId")
        Log.d("SQLITE_SIMON", "Prueba getRecordById(1): $recordEnMemoria")

    }
    private fun actualizarRecord() {
        // VERIFICAMOS SI LA RONDA ACTUAL SUPERA EL RÉCORD HISTÓRICO
        if (_ronda.value > recordEnMemoria) {
            recordEnMemoria = _ronda.value

            // GENERAMOS LA FECHA Y HORA DEL MOMENTO ACTUAL
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val fechaActual = sdf.format(Date())
            fechaRecord = fechaActual

            // GUARDAMOS EL NUEVO RÉCORD Y LA FECHA EN LA TABLA SQLITE
            dbHelper.insertarRecord(recordEnMemoria, fechaActual)
        }
    }
    private fun inicializarDatosPrueba() {
        val usuarios = dbHelper.obtenerTodosLosUsuarios()
        if (usuarios.isEmpty()) {
            dbHelper.insertarUsuario("TESTER 1")
            dbHelper.insertarUsuario("PROFE 1")
            Log.d("SQLITE_SIMON", "Datos de prueba insertados en tabla_usuarios")
        }
    }
    fun actualizarListaUsuariosUI() {
        val lista = dbHelper.obtenerTodosLosUsuarios()
        // CONVERTIMOS LA LISTA [Usuario(1, "Pepe"), Usuario(2, "Juan")] EN ["1: Pepe", "2: Juan"] A STRING
        listaUsuariosTexto = if (lista.isNotEmpty()) lista.joinToString("\n") else "Sin usuarios"
    }
    // AGREGA UN USUARIO
    fun registrarUsuarioNuevo(nombre: String) {
        dbHelper.insertarUsuario(nombre)
        actualizarListaUsuariosUI() // REFRESCAMOS LA LISTA
    }

    fun eliminarUsuario(id: Int) {
        val borrados = dbHelper.borrarUsuarioPorId(id)
        if (borrados > 0) {
            actualizarListaUsuariosUI() // REFRESCAMOS PANTALLA
        }
    }
    /**
     *
     *  -----------------------------------------------------------------------------------------------------
     *  En este programa hay varias clases muy simples que a veces incluso solo suman 1 a alguna
     *  variable. Decidí separarlo así en vez de juntarlo para que sea mucho mas visual, legible y ordenado;
     *  además, se pueden detectar errores con una sorprendentemente mayor eficacia
     * -----------------------------------------------------------------------------------------------------
     *
     */


    /**
     * Esta funcion se dedica a crear el siguiente numero/color de la secuencia
     * lo realiza con un random y utilizamos numeros del 0 al 3
     */
    fun generarNNuevo() {
        estadoActual.value = GameState.GENERANDO
        _numbers.value = (0..3).random() //creamos el numero
        Log.d(TAG_LOG, "creamos random ${_numbers.value} - Estado: ${estadoActual.value}")
        setNNuevo(_numbers.value)
    }

    /**
     * En esta función se van mostrando los colores cambiando
     * el valor de la variable _colorActivo y antes cambiando
     * el estado
     */
    suspend fun mostrarColores(){
        _colorActivo.value = -1 // con -1 hacemos que ningun color se vea
        delay(200)
        for (color in _listaSecuencia.value) { //recorremos la secuencia
            //vamos igualando el color activo con el que toca de la secuencia
            //así desde la view sabrá que botón mostar
            _colorActivo.value = color
            hacerSonido(_colorActivo.value) //sonido del boton correspondiente
            delay(500)
            _colorActivo.value = -1 //volvemos a dejar su valor base
            delay(200)
        }
        estadoActual.value = GameState.ADIVINANDO
        Log.d(TAG_LOG, "Tu turno - Estado: ${estadoActual.value}")
    }

    /**
     * En esta funcion actualizamos la secuencia
     * @param numero: metemos el nuevo numero de la secuencia
     */
    fun setNNuevo(numero: Int) {
        Log.d(TAG_LOG, "actualizamos numero en Datos - Estado: ${estadoActual.value}")
        _listaSecuencia.value += numero //añadimos el color a la secuencia
        Log.d(TAG_LOG, "chuleta: ${_listaSecuencia.value}")
        estadoActual.value = GameState.MOSTRANDO
        Log.d(TAG_LOG, "MOSTRANDO COLORESS - Estado: ${estadoActual.value}")
        viewModelScope.launch {
            mostrarColores() //empezamos a mostrar la secuencia
        }
    }

    /**
     * Cuando se pulsa un boton se ejecuta esta funcion
     * esta comprueba si el color que hemos pulsado coincide con el que toca de la secuencia
     * (lo hacemos con una lista y su índice)
     *
     * llamaremos a la funcion de hacer el sonido que corresponde y comprobaremos si es el ultimo
     * color de la secuencia en caso de acertar, o reiniciaremos el juego en caso de fallar
     *
     * @param numeroAdivinar: Es el numero que corresponde el botón pulsado
     */
    fun comprobar(numeroAdivinar: Int) {
        estadoActual.value = GameState.PULSADO
        _colorPulsado.value = numeroAdivinar //color pulsado
        if (numeroAdivinar == _listaSecuencia.value[_nSecuenciaActual.value]) { //vemos si el pulsado es igual al color actual
            Log.d(TAG_LOG, "adivinaste - Estado: ${estadoActual.value}")
            viewModelScope.launch { //en caso de que sea
                hacerSonido(_colorPulsado.value) //hacemos el sonido
                delay(200)
                setnSecuencia() //comprobamos el numero de la secuencia
            }
        } else { //en caso de fallar
            hacerSonido(-1) //sonido de fallo
            actualizarRecord()
            estadoActual.value = GameState.REINICIANDO
            reiniciarJuego() //reiniciamos juego
        }
    }

    /**
     * Esta funcion comprueba si el numero actual de la secuencia ya es el ultimo de esta,
     * yo lo compruebo comparandolo con el numero de ronda ya que coincide siempre.
     *
     * Si el numero es el ultimo, llamamos la funcion setRonda()
     * Si el numero NO es el último, pasamos al siguiente numero de la funcion
     */
    fun setnSecuencia(){
        if (_nSecuenciaActual.value == _ronda.value){ //si es el numero final de la secuencia
            _nSecuenciaActual.value = 0 //volvemos al principio de la secuencia
            setRonda()
        } else {
            estadoActual.value = GameState.ADIVINANDO
            Log.d(TAG_LOG, "dime el siguiente numero - Estado: ${estadoActual.value}")
            _nSecuenciaActual.value ++ //pasamos al siguiente numero
        }
    }

    /**
     * Aumentamos el valor de la ronda y llamamos a la funcion generarNNuevo para
     * que cree el siguiente color aleatorio de la secuencia
     */
    fun setRonda(){
        estadoActual.value = GameState.GENERANDO
        Log.d(TAG_LOG, "avanzando a la siguiente ronda - Estado: ${estadoActual.value}")
        _ronda.value ++ //aumentamos la ronda
        generarNNuevo()
    }

    /**
     * Una funcion para simular que el juego se reinicia, simplemente vuelvo a poner los
     * valores base de las variables y cambio el estado a inicio
     */
    fun reiniciarJuego(){
        Log.d(TAG_LOG, "fallaste,has perdido, reiniciando - Estado: ${estadoActual.value}")
        Log.d(TAG_LOG, "nivel alcanzado: ${_ronda.value}")

        _listaSecuencia.value = emptyList() //vaciamos la secuencia
        _nSecuenciaActual.value = 0
        _ronda.value = 0
        estadoActual.value = GameState.INICIO
    }

    /**
     * Segun el color que elijamos hace un sonido
     * Si no se elige uno valido por defecto suena el de error
     * @param color: Se pasa el numero
     */
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
}