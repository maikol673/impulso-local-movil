package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Conversacion(
    @SerializedName("id") val id: Int,
    @SerializedName("usuario1_id") val usuario1Id: Int,
    @SerializedName("usuario2_id") val usuario2Id: Int,
    @SerializedName("ultimo_mensaje") val ultimoMensaje: String? = null,
    @SerializedName("fecha_actualizacion") val fechaActualizacion: String,
    @SerializedName("usuario1") val usuario1: Usuario? = null,
    @SerializedName("usuario2") val usuario2: Usuario? = null
) : Serializable