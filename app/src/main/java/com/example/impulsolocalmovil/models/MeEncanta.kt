package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MeEncanta(
    @SerializedName("id") val id: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("emprendimiento_id") val emprendimientoId: Int,
    @SerializedName("fecha_me_encanta") val fechaMeEncanta: String? = null,
    @SerializedName("emprendimiento") val emprendimiento: Emprendimiento? = null,
    @SerializedName("usuario") val usuario: Usuario? = null
) : Serializable