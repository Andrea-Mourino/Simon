package com.example.simon;

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.navigationevent.NavigationEventInfo

/**
 * Clase para almacenar los datos del juego
 */
object Datos {
    var numero = 0
    var secuencia = listOf<Int>()
    var ronda = 0
}

/**
 * Colores utilizados
 * color: Color color normal
 * color_suave: Color color suave para el parpadeo, por defecto Transparente
 * txt: String nombre del color
 */
enum class Colores(val color: Color, val color_suave: Color = Color.Transparent, val txt: String) {
    CLASE_ROJO(color = Color.Red, txt = "roxo"),
    CLASE_VERDE(color = Color.Green, txt = "verde"),
    CLASE_AZUL(color = Color.Blue, txt = "azul"),
    CLASE_AMARILLO(color = Color.Yellow, txt = "melo"),
    CLASE_START(color = Color.Magenta, color_suave = Color.Red, txt = "Start")
}

/**
 * Estados del juego
 * INICIO: estado inicial
 * GENERANDO: generando numero random
 * ADIVINANDO: adivinando el numero
 * @param start_activo: Boolean si el boton Start esta activo
 * @param boton_activo: Boolean si los botones de colores estan activos
 */
enum class Estados(val start_activo: Boolean, val boton_activo: Boolean, val boton_secuencia: Boolean) {
    INICIO(start_activo = true, boton_activo = false, boton_secuencia = false),
    GENERANDO(start_activo = false, boton_activo = false, boton_secuencia = false),
    ADIVINANDO(start_activo = false, boton_activo = true, boton_secuencia = false),

    REINICIANDO(start_activo = false, boton_activo = false, boton_secuencia = false),

    MOSTRANDO(start_activo = false, boton_activo = true, boton_secuencia = true),

}
