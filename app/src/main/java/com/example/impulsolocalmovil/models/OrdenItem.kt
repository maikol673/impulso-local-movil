package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrdenItem(
    @SerializedName("id") val id: Int,
    @SerializedName("orden_id") val ordenId: Int,
    @SerializedName("producto_id") val productoId: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precio") val precio: Double,
    @SerializedName("subtotal") val subtotal: Double,
    @SerializedName("producto") val producto: Producto? = null
) : Serializable