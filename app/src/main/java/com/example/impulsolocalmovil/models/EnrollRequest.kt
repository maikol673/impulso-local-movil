package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class EnrollRequest(
    @SerializedName("curso_id") val cursoId: Int,
    @SerializedName("usuario_id") val usuarioId: Int
)