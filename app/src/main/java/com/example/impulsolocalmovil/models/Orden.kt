package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Orden(
    @SerializedName("id") val id: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("total") val total: Double,
    @SerializedName("estado") val estado: String,
    @SerializedName("direccion_envio") val direccionEnvio: String,
    @SerializedName("telefono_contacto") val telefonoContacto: String,
    @SerializedName("notas") val notas: String? = null,
    @SerializedName("fecha_creacion") val fechaCreacion: String,
    @SerializedName("items") val items: List<OrdenItem>? = null,
    @SerializedName("usuario") val usuario: Usuario? = null
) : Serializable