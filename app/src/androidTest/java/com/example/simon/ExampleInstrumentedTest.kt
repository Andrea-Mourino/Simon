package com.example.simon

import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import android.content.Context
import com.example.simon.shared_preference.Shared_controller

class ExampleInstrumentedTest {

    private lateinit var viewModel: MyViewModel
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        viewModel = MyViewModel(context as android.app.Application)
    }

    @Test
    fun `estado inicial correcto`() {
        assertEquals(GameState.INICIO, viewModel.estadoActual.value)
        assertTrue(viewModel._listaSecuencia.value.isEmpty())
        assertEquals(0, viewModel._ronda.value)
        assertEquals(0, viewModel._nSecuenciaActual.value)
    }

    @Test
    fun `generar nuevo numero añade a la secuencia y cambia estado`() = runBlocking {
        val secuenciaAntes = viewModel._listaSecuencia.value.size

        viewModel.generarNNuevo()
        Thread.sleep(1200)

        assertTrue(viewModel._listaSecuencia.value.size > secuenciaAntes)
        assertTrue(viewModel.estadoActual.value == GameState.MOSTRANDO || viewModel.estadoActual.value == GameState.ADIVINANDO)
        assertTrue(viewModel._numbers.value in 0..3)
    }

    @Test
    fun `acertar boton cambia color pulsado y avanza secuencia`() = runBlocking {
        viewModel._listaSecuencia.value = listOf(0, 1)
        viewModel._ronda.value = 1
        viewModel._nSecuenciaActual.value = 0

        viewModel.comprobar(0)
        Thread.sleep(500)

        assertEquals(1, viewModel._nSecuenciaActual.value)
        assertEquals(GameState.ADIVINANDO, viewModel.estadoActual.value)
        assertEquals(0, viewModel._colorPulsado.value)
    }

    @Test
    fun `fallar boton reinicia el juego`() = runBlocking {
        viewModel._listaSecuencia.value = listOf(0)
        viewModel._ronda.value = 1
        viewModel.comprobar(2) // fallo
        Thread.sleep(200)

        assertEquals(GameState.INICIO, viewModel.estadoActual.value)
        assertTrue(viewModel._listaSecuencia.value.isEmpty())
        assertEquals(0, viewModel._ronda.value)
        assertEquals(0, viewModel._nSecuenciaActual.value)
    }

    @Test
    fun `completar secuencia correctamente incrementa ronda y genera siguiente`() = runBlocking {
        viewModel._listaSecuencia.value = listOf(0)
        viewModel._ronda.value = 0
        viewModel._nSecuenciaActual.value = 0

        val rondaAntes = viewModel._ronda.value
        val secuenciaAntes = viewModel._listaSecuencia.value.size

        viewModel.comprobar(0)
        Thread.sleep(1200)

        assertTrue(viewModel._ronda.value > rondaAntes) // la ronda aumentó
        assertTrue(viewModel._listaSecuencia.value.size > secuenciaAntes) // la secuencia aumentó
        assertEquals(GameState.MOSTRANDO, viewModel.estadoActual.value) // estado correcto
    }

    @Test
    fun `reiniciar juego restablece todas las variables`() {
        viewModel._listaSecuencia.value = listOf(0, 1)
        viewModel._ronda.value = 2
        viewModel._nSecuenciaActual.value = 1
        viewModel.estadoActual.value = GameState.PULSADO

        viewModel.reiniciarJuego()

        assertEquals(GameState.INICIO, viewModel.estadoActual.value)
        assertTrue(viewModel._listaSecuencia.value.isEmpty())
        assertEquals(0, viewModel._ronda.value)
        assertEquals(0, viewModel._nSecuenciaActual.value)
    }

    @Test
    fun `hacerSonido llama al tono correcto`() {
        viewModel.hacerSonido(0)
        viewModel.hacerSonido(1)
        viewModel.hacerSonido(2)
        viewModel.hacerSonido(3)
        viewModel.hacerSonido(99)
    }

    @Test
    fun `setnSecuencia avanza correctamente o llama a setRonda`() = runBlocking {
        viewModel._listaSecuencia.value = listOf(0, 1)
        viewModel._ronda.value = 1
        viewModel._nSecuenciaActual.value = 0

        viewModel.setnSecuencia()
        assertEquals(1, viewModel._nSecuenciaActual.value)
        assertEquals(GameState.ADIVINANDO, viewModel.estadoActual.value)

        viewModel._nSecuenciaActual.value = 1
        viewModel.setnSecuencia()
        Thread.sleep(1000)
        assertTrue(viewModel._ronda.value >= 1)
    }

    @Test
    fun `completar ronda larga genera siguiente correctamente`() = runBlocking {
        viewModel._listaSecuencia.value = listOf(0, 1, 2, 3)
        viewModel._ronda.value = 3
        viewModel._nSecuenciaActual.value = 0
        viewModel._nSecuenciaActual.value = 3
        viewModel.comprobar(3)

        Thread.sleep(1200)

        assertTrue(viewModel._listaSecuencia.value.size >= 4)
        assertTrue(viewModel._ronda.value >= 4)
        assertEquals(GameState.MOSTRANDO, viewModel.estadoActual.value)
    }

    @Test
    fun `comprobar numero invalido no rompe el juego`() = runBlocking {
        viewModel._listaSecuencia.value = listOf(0, 1)
        viewModel._ronda.value = 1
        viewModel._nSecuenciaActual.value = 0

        viewModel.comprobar(99) // invalido
        Thread.sleep(200)

        assertEquals(GameState.INICIO, viewModel.estadoActual.value) // el juego reinicia
        assertTrue(viewModel._listaSecuencia.value.isEmpty())
        assertEquals(0, viewModel._ronda.value)
    }

    @Test
    fun `hacerSonido con todos numeros validos no lanza error`() {
        for (i in 0..3) {
            viewModel.hacerSonido(i)
        }
    }

    @Test
    fun `comprobacion de get del record inicial`() {
        val inicialRecord = viewModel._record.value
        val getRecord = Shared_controller.obtenerRecord(context).record

        assertEquals(getRecord, inicialRecord)
    }


    @Test
    fun `comprobación en la actualizacion del record`() = runBlocking {
        val actualRecord = viewModel._record.value
        val newRecord = actualRecord + 67

        viewModel.comprobarRecord(newRecord)

        val actualNewRecord = viewModel._record.value
        val newGetRecord = Shared_controller.obtenerRecord(context).record

        assertEquals(newRecord, actualNewRecord)
        assertEquals(newRecord, newGetRecord)
    }

    @Test
    fun `comprobación en la actualizacion de la fecha`() = runBlocking {
        val newRecord = viewModel._record.value + 67
        val actualDate = Shared_controller.obtenerRecord(context).date

        viewModel.comprobarRecord(newRecord)

        val newDate = Shared_controller.obtenerRecord(context).date

        assertTrue(newDate.isAfter(actualDate) || newDate.isEqual(actualDate))
    }

    @Test
    fun `comprobacion de una puntuacion menor al record`() = runBlocking {
        val actualRecord = viewModel._record.value
        val newPuntuacion = actualRecord - 67

        viewModel.comprobarRecord(newPuntuacion)

        val actualNewRecord = viewModel._record.value
        val newGetRecord = Shared_controller.obtenerRecord(context).record

        assertEquals(actualRecord, actualNewRecord)
        assertEquals(actualRecord, newGetRecord)
    }

}
