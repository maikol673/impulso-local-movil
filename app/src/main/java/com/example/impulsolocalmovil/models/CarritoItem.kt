package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CarritoItem(
    @SerializedName("id") val id: Int,
    @SerializedName("producto_id") val productoId: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: String,  // ← Laravel devuelve String
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("imagen") val imagen: String? = null,
    @SerializedName("emprendimiento_nombre") val emprendimientoNombre: String? = null
) : Serializable