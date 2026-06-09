package com.example.impulsolocalmovil.models

import java.io.Serializable

data class Emprendimiento(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val rating: Double,
    val ubicacion: String,
    val estado: String,
    val imagenUrl: String
) : Serializable