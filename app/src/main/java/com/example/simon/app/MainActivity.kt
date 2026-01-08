package com.example.simon.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.simon.ui.theme.SimonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inicializamos ViewModel
        val miViewModel: MyViewModel = MyViewModel(application) //le paso application para que el AndroidViewModel pueda recibir Context

        enableEdgeToEdge()
        setContent {
            SimonTheme {
                // llamamos a la IU pasando el ViewModel
                IU(miViewModel)
            }
        }
    }
}