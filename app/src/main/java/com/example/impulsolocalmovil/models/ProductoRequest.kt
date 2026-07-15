package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class ProductoRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("emprendimiento_id") val emprendimientoId: Int,
    @SerializedName("estado") val estado: String = "activo"
)