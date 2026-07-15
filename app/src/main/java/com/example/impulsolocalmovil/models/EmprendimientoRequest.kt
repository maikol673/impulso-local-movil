package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class EmprendimientoRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("categoria_id") val categoriaId: Int,
    @SerializedName("ubicacion") val ubicacion: String,
    @SerializedName("estado") val estado: String? = "activo"
)