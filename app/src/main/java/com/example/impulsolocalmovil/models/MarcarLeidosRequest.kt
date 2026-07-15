package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class MarcarLeidosRequest(
    @SerializedName("usuario_id") val usuarioId: Int
)