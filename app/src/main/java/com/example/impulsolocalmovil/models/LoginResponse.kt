package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("token") val token: String? = null,  // ← DEBE EXISTIR
    @SerializedName("user") val user: Usuario? = null,
    @SerializedName("error") val error: String? = null
)