package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class ResenaUpdateRequest(
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("comentario") val comentario: String
)