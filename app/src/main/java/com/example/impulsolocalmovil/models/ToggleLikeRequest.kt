package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class ToggleLikeRequest(
    @SerializedName("emprendimiento_id") val emprendimientoId: Int,
    @SerializedName("usuario_id") val usuarioId: Int
)