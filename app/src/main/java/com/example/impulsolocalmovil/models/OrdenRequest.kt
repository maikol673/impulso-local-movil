package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class OrdenRequest(
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("direccion_envio") val direccionEnvio: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("codigo_postal") val codigoPostal: String,  // ✅ NUEVO
    @SerializedName("telefono_contacto") val telefonoContacto: String,
    @SerializedName("notas") val notas: String? = null,
    @SerializedName("items") val items: List<OrdenItemRequest>  // ✅ NUEVO - Lista de productos
)

