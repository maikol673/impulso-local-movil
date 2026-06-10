package com.example.impulsolocalmovil.models

import java.io.Serializable

data class Evento(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val hora: String,
    val modalidad: String,
    val estado: String
) : Serializable