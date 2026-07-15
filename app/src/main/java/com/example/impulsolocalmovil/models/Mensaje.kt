package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// Un mensaje individual
data class Mensaje(
    @SerializedName("id") val id: Int,
    @SerializedName("conversacion_id") val conversacionId: Int,
    @SerializedName("remitente_id") val remitenteId: Int,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("leido") val leido: Boolean,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("remitente") val remitente: Chat?
) : Serializable