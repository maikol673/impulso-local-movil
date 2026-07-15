package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Testimonio(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("empresa") val empresa: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: String? = null,
    @SerializedName("usuario_id") val usuarioId: Int? = null,
    @SerializedName("usuario") val usuario: Usuario? = null
) : Serializable