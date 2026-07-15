package com.example.impulsolocalmovil.models

import com.google.gson.annotations.SerializedName

data class AdminStats(
    @SerializedName("total_emprendimientos") val totalEmprendimientos: Int,
    @SerializedName("total_usuarios") val totalUsuarios: Int,
    @SerializedName("total_ordenes") val totalOrdenes: Int,
    @SerializedName("total_resenas") val totalResenas: Int,
    @SerializedName("usuarios_activos") val usuariosActivos: Int,
    @SerializedName("emprendimientos_activos") val emprendimientosActivos: Int
)