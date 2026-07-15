package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Chat(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("avatar") val avatar: String? = null
) : Serializable



