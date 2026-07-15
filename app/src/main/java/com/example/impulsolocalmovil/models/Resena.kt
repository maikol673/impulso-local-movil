package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Resena(
    @SerializedName("id") val id: Int,
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("comentario") val comentario: String,
    @SerializedName("fecha_creacion") val fechaCreacion: String? = null,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("emprendimiento_id") val emprendimientoId: Int,
    @SerializedName("usuario") val usuario: Usuario? = null
) : Serializable