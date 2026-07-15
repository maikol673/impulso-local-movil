package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


// Resumen de una conversación (para la lista de chats)
data class ConversacionResumen(
    @SerializedName("id") val id: Int,
    @SerializedName("otro_usuario") val otroUsuario: Chat,
    @SerializedName("ultimo_mensaje") val ultimoMensaje: String?,
    @SerializedName("ultima_actualizacion") val ultimaActualizacion: String?,
    @SerializedName("mensajes_no_leidos") val mensajesNoLeidos: Int
) : Serializable
