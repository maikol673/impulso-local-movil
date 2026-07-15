package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Evento(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("hora") val hora: String,
    @SerializedName("ubicacion") val ubicacion: String?,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("enlace_online") val enlaceOnline: String?,
    @SerializedName("estado") val estado: String?,
    @SerializedName("imagen") val imagen: String?
) : Serializable