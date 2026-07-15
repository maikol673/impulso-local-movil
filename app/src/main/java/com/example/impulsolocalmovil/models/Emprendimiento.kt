package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Emprendimiento(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("ubicacion") val ubicacion: String? = null,
    @SerializedName("imagen") val imagen: String? = null,
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("calificacion") val calificacion: String? = null,
    @SerializedName("num_resenas") val numResenas: Int? = 0,
    @SerializedName("usuario_id") val usuarioId: Int? = null,
    @SerializedName("categoria") val categoria: Categoria? = null,
    @SerializedName("usuario") val usuario: Usuario? = null
) : Serializable