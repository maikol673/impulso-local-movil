package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Curso(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("instructor") val instructor: String,
    @SerializedName("duracion") val duracion: String,
    @SerializedName("nivel") val nivel: String,
    @SerializedName("imagen") val imagen: String? = null,
    @SerializedName("fecha_inicio") val fechaInicio: String? = null,
    @SerializedName("estado") val estado: String? = null
) : Serializable