package com.example.simon;

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.simon.ui.theme.SimonTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Note 1: Habilita el modo edge-to-edge para aprovechar toda la pantalla en dispositivos modernos.
        // inicializamos ViewModel
        val miViewModel: MyViewModel = MyViewModel(application)

        enableEdgeToEdge()
        setContent {
            SimonTheme {
                // llamamos a la IU pasando el ViewModel
                IU(miViewModel)
            }
        }
    }
}