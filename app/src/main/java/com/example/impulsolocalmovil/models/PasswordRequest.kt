package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class PasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("new_password_confirmation") val newPasswordConfirmation: String
)