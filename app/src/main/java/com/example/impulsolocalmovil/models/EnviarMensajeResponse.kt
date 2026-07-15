package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class EnviarMensajeResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("id") val id: Int,
    @SerializedName("conversacion_id") val conversacionId: Int
)