package com.example.simon

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState

/**
 * Interfaz de usuario unificada y ajustada
 */

@Composable
fun IU(miViewModel: MyViewModel) {

    val ronda by miViewModel._ronda.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF6A0DAD), Color(0xFF9C27B0))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Boton(miViewModel, Colores.CLASE_ROJO)
                    Boton(miViewModel, Colores.CLASE_VERDE)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Boton(miViewModel, Colores.CLASE_AZUL)
                    Boton(miViewModel, Colores.CLASE_AMARILLO)
                }
            }

            Text(text = "Ronda Nº: $ronda", color = Color.Green)

            Boton_Start(miViewModel, Colores.CLASE_START)
        }
    }
}

@Composable
fun Boton(miViewModel: MyViewModel, enum_color: Colores) {
    val estado = miViewModel.estadoActual.collectAsState().value
    val colorActivo by miViewModel._colorActivo.collectAsState()
    val colorPulsado by miViewModel._colorPulsado.collectAsState()

    val botonColor = when {
        estado.boton_secuencia && colorActivo == enum_color.ordinal -> enum_color.color_brillante
        estado.boton_pulsado && colorPulsado == enum_color.ordinal -> enum_color.color_brillante
        else -> enum_color.color
    }

    Button(
        enabled = estado.boton_activo,
        onClick = { miViewModel.comprobar(enum_color.ordinal) },
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
        colors = ButtonDefaults.buttonColors(botonColor),
        modifier = Modifier.size(100.dp, 60.dp)
    ) {
        Text(text = enum_color.txt.uppercase(), color = Color.White)
    }
}

@Composable
fun Boton_Start(miViewModel: MyViewModel, enum_color: Colores) {
    val _activo = miViewModel.estadoActual.collectAsState().value.start_activo
    var colorActual by remember { mutableStateOf(enum_color.color) }
    val colorAnim = animateColorAsState(targetValue = colorActual)

    LaunchedEffect(_activo) {
        while (_activo) {
            colorActual = enum_color.color_suave
            delay(300)
            colorActual = enum_color.color
            delay(500)
        }
    }

    Button(
        enabled = _activo,
        onClick = {
            miViewModel.generarNNuevo()
        },
        colors = ButtonDefaults.buttonColors(colorAnim.value),
        shape = RoundedCornerShape(20.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp),
        modifier = Modifier.size(140.dp, 70.dp)
    ) {
        Text(text = enum_color.txt.uppercase(), color = Color.White)
    }
}
