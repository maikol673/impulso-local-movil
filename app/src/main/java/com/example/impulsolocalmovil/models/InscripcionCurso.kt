package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InscripcionCurso(
    @SerializedName("id") val id: Int,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("curso_id") val cursoId: Int,
    @SerializedName("fecha_inscripcion") val fechaInscripcion: String,
    @SerializedName("progreso") val progreso: Int? = 0,
    @SerializedName("curso") val curso: Curso? = null
) : Serializable