package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class AsistenciaRequest(
    @SerializedName("evento_id") val eventoId: Int,
    @SerializedName("usuario_id") val usuarioId: Int
)