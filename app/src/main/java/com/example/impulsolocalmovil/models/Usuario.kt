package com.example.impulsolocalmovil.models

import java.io.Serializable

data class Usuario(
    val id: Int,
    val nombre: String,
    val username: String,
    val email: String
) : Serializable