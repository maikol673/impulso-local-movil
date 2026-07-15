package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.Emprendimiento

class AdminEmprendimientoAdapter(
    private var items: List<Emprendimiento>,
    private val onItemClick: (Emprendimiento) -> Unit
) : RecyclerView.Adapter<AdminEmprendimientoAdapter.ViewHolder>() {

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val btnVer: Button = itemView.findViewById(R.id.btnVer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_emprendimiento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvNombre.text = item.nombre
        holder.tvDescripcion.text = if (item.descripcion.length > 80) {
            item.descripcion.take(80) + "..."
        } else {
            item.descripcion
        }

        // ✅ Fecha - usamos el ID como referencia o un texto fijo (no hay campo fecha en Emprendimiento)
        holder.tvFecha.text = "📅 Reciente"

        // Estado
        when (item.estado) {
            "activo", "destacado" -> {
                holder.tvEstado.text = "✅ Activo"
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_badge)
            }
            "nuevo" -> {
                holder.tvEstado.text = "🆕 Nuevo"
                holder.tvEstado.setBackgroundResource(R.drawable.bg_chip_selected)
            }
            else -> {
                holder.tvEstado.text = "⏳ ${item.estado ?: "Pendiente"}"
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)
            }
        }

        holder.btnVer.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<Emprendimiento>) {
        items = newItems
        notifyDataSetChanged()
    }
}