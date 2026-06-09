package com.example.impulsolocalmovil.models

data class Emprendimiento(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val rating: Double,
    val ubicacion: String,
    val estado: String, // "destacado", "nuevo", "normal"
    val imagenUrl: String
)