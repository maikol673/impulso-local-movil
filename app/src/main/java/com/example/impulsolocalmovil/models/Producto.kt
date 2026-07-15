package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Producto(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("imagen") val imagen: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("emprendimiento_id") val emprendimientoId: Int? = null,
    @SerializedName("emprendimiento") val emprendimiento: Emprendimiento? = null
) : Serializable