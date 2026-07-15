package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class MensajeRequest(
    @SerializedName("remitente_id") val remitenteId: Int,
    @SerializedName("receptor_id") val receptorId: Int,
    @SerializedName("contenido") val contenido: String
)