package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class OrdenItemRequest(
    @SerializedName("producto_id") val productoId: Int,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precio") val precio: Double
)