package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class TestimonioRequest(
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("empresa") val empresa: String? = null
)