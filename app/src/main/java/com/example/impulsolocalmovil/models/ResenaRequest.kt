package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class ResenaRequest(
    @SerializedName("emprendimiento_id") val emprendimientoId: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("comentario") val comentario: String
)

