package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.Evento

class EventoAdapter(
    private var items: List<Evento>,
    private val onItemClick: (Evento) -> Unit
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
        holder.tvNombre.text = item.nombre
        holder.tvDescripcion.text = item.descripcion
        holder.tvFecha.text = "📅 ${item.fecha}"
        holder.tvHora.text = "⏰ ${item.hora}"
        holder.tvModalidad.text = "🎯 ${item.modalidad}"
        holder.tvEstado.text = item.estado

        when (item.estado) {
            "confirmado" -> holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_badge)
            "pendiente" -> holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)
        }

        holder.btnVerDetalle.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<Evento>) {
        items = newItems
        notifyDataSetChanged()
    }
}