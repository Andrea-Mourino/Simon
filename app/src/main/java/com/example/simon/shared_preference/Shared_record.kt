package com.example.simon.shared_preference

import java.time.LocalDate

/**
 * Esta clase almacena el record y la fecha
 * despues se puede llamar para conseguir los datos
 *
 * @param record: la puntuacion
 * @param date: la fecha
 */
data class Shared_record(var record: Int, var date: LocalDate)