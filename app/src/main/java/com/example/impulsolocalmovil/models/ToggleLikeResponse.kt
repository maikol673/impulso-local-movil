package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class ToggleLikeResponse(
    @SerializedName("liked") val liked: Boolean,
    @SerializedName("total_likes") val totalLikes: Int
)