package com.example.simon.app
import java.time.LocalDate

//Esta clase sirve para almacenar, pasar y mostrar de manera organizada los datos del récord

//Almacena la ronda mas alta alcanzada, la fecha y el nombre del jugador

data class Record(var record: Int, var date: LocalDate, var playerName: String = "Jugador")
