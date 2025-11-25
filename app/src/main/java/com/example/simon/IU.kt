package com.example.simon;

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color

/**
 * Interfaz de usuario
 * Modificado desde Code
 */

@Composable
fun IU(miViewModel: MyViewModel) {
    // para que sea mas facil la etiqueta del log
    // val TAG_LOG = "miDebug"

    // botones en horizontal
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF6A0DAD)), // Fondo morado simple
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Boton(miViewModel, Colores.CLASE_ROJO)
                    Boton(miViewModel, Colores.CLASE_VERDE)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Boton(miViewModel, Colores.CLASE_AZUL)
                    Boton(miViewModel, Colores.CLASE_AMARILLO)
                }
            }

            Boton_Start(miViewModel, Colores.CLASE_START)
        }
    }
}

@Composable
fun Boton(miViewModel: MyViewModel, enum_color: Colores) {
    val _activo = miViewModel.estadoActual.collectAsState().value.boton_activo

    Button(
        enabled = _activo,
        onClick = { miViewModel.comprobar(enum_color.ordinal) },
        colors = ButtonDefaults.buttonColors(enum_color.color),
        modifier = Modifier.size(100.dp, 60.dp)
    ) {
        Text(text = enum_color.txt.uppercase(), color = Color.White)
    }
}

@Composable
fun Boton_Start(miViewModel: MyViewModel, enum_color: Colores) {
    val _activo = miViewModel.estadoActual.collectAsState().value.start_activo
    Button(
        enabled = _activo,
        onClick = {
            miViewModel.crearRandom()
            miViewModel.sumarBoton()
        },
        colors = ButtonDefaults.buttonColors(enum_color.color),
        modifier = Modifier.size(140.dp, 70.dp)
    ) {
        Text(text = enum_color.txt.uppercase(), color = Color.White)
    }
}