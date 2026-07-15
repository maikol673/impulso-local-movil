package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class AddToCartRequest(
    @SerializedName("producto_id") val productoId: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("cantidad") val cantidad: Int
)