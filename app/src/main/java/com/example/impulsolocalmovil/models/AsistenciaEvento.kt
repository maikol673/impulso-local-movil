package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AsistenciaEvento(
    @SerializedName("id") val id: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("evento_id") val eventoId: Int,
    @SerializedName("asistio") val asistio: Boolean? = null,
    @SerializedName("fecha_registro") val fechaRegistro: String? = null,
    @SerializedName("evento") val evento: Evento?
) : Serializable