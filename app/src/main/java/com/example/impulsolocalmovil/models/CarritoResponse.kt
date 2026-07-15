package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class CarritoResponse(
    @SerializedName("items") val items: List<CarritoItem>,
    @SerializedName("total") val total: Double
)