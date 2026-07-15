package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Usuario(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String? = null,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("is_staff") val isAdmin: Boolean? = false,
    @SerializedName("bio") val bio: String? = null,
    @SerializedName("telefono") val telefono: String? = null,
    @SerializedName("ciudad") val ciudad: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("sitio_web") val sitioWeb: String? = null,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String? = null,
    @SerializedName("recibe_notificaciones") val recibeNotificaciones: Boolean? = null,
    @SerializedName("notificaciones_email") val notificacionesEmail: Boolean? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
) : Serializable