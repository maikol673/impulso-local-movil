package com.example.impulsolocalmovil.models

import java.io.Serializable

data class Curso(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val instructor: String,
    val duracion: String,
    val nivel: String
) : Serializable