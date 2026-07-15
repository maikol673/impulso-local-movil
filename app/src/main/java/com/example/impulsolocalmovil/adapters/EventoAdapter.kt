package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.AsistenciaEvento

class EventoAdapter(
    private var items: List<AsistenciaEvento>,
    private val onItemClick: (AsistenciaEvento) -> Unit
) : RecyclerView.Adapter<EventoAdapter.ViewHolder>() {

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvHora: TextView = itemView.findViewById(R.id.tvHora)
        val tvModalidad: TextView = itemView.findViewById(R.id.tvModalidad)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val btnVerDetalle: Button = itemView.findViewById(R.id.btnVerDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val evento = item.evento

        if (evento != null) {
            holder.tvNombre.text = evento.nombre
            holder.tvDescripcion.text = evento.descripcion
            holder.tvFecha.text = "📅 ${evento.fecha}"
            holder.tvHora.text = "⏰ ${evento.hora}"
            holder.tvModalidad.text = "🎯 ${evento.tipo}"

            // ✅ Ahora usamos "asistio" (Boolean) en vez de "estado" (String)
            val asistioConfirmado = item.asistio == true
            holder.tvEstado.text = if (asistioConfirmado) "Confirmado" else "Pendiente"

            if (asistioConfirmado) {
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_badge)
            } else {
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)
            }

            holder.btnVerDetalle.setOnClickListener {
                onItemClick(item)
            }
            holder.itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<AsistenciaEvento>) {
        items = newItems
        notifyDataSetChanged()
    }
}