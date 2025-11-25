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
enum class Colores(val color: Color, val color_suave: Color = Color.Transparent, val txt: String, val color_brillante: Color) {
    CLASE_ROJO(color = Color.Red, txt = "roxo", color_brillante = Color(0xFFFF6B6B)),
    CLASE_VERDE(color = Color.Green, txt = "verde", color_brillante = Color(0xFF6BFF6B)),
    CLASE_AZUL(color = Color.Blue, txt = "azul", color_brillante = Color(0xFF6BC6FF)),
    CLASE_AMARILLO(color = Color.Yellow, txt = "melo", color_brillante = Color(0xFFFFFF6B)),

    CLASE_START(color = Color.Magenta, color_suave = Color.Red, txt = "Start", color_brillante = Color.Magenta)
}

/**
 * Estados del juego
 * INICIO: estado inicial
 * GENERANDO: generando numero random
 * ADIVINANDO: adivinando el numero
 * @param start_activo: Boolean si el boton Start esta activo
 * @param boton_activo: Boolean si los botones de colores estan activos
 */
enum class Estados(val start_activo: Boolean, val boton_activo: Boolean, val boton_secuencia: Boolean,val boton_pulsado: Boolean) {
    INICIO(start_activo = true, boton_activo = false, boton_secuencia = false, boton_pulsado = false),
    GENERANDO(start_activo = false, boton_activo = false, boton_secuencia = false, boton_pulsado = false),
    ADIVINANDO(start_activo = false, boton_activo = true, boton_secuencia = false, boton_pulsado = false),

    REINICIANDO(start_activo = false, boton_activo = false, boton_secuencia = false, boton_pulsado = false),

    MOSTRANDO(start_activo = false, boton_activo = true, boton_secuencia = true, boton_pulsado = false),

    PULSADO(start_activo = false, boton_activo = true, boton_secuencia = false, boton_pulsado = true),


}
