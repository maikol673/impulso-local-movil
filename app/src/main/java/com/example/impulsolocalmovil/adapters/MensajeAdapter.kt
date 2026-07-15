package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.Mensaje

class MensajeAdapter(
    private var items: List<Mensaje>,
    private val usuarioActualId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TIPO_ENVIADO = 1
        private const val TIPO_RECIBIDO = 2
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvContenido: TextView = itemView.findViewById(R.id.tvContenido)
        val tvHora: TextView = itemView.findViewById(R.id.tvHora)
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].remitenteId == usuarioActualId) TIPO_ENVIADO else TIPO_RECIBIDO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = if (viewType == TIPO_ENVIADO) {
            R.layout.item_mensaje_enviado
        } else {
            R.layout.item_mensaje_recibido
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val vh = holder as ViewHolder
        vh.tvContenido.text = item.contenido
        vh.tvHora.text = formatearHora(item.createdAt)
    }

    private fun formatearHora(fecha: String?): String {
        if (fecha.isNullOrEmpty()) return ""
        return try {
            // Formato típico Laravel: "2026-07-14T22:56:15.000000Z" o "2026-07-14 22:56:15"
            val horaParte = fecha.substringAfter("T").substringAfter(" ")
            horaParte.take(5) // HH:mm
        } catch (e: Exception) {
            ""
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<Mensaje>) {
        items = newItems
        notifyDataSetChanged()
    }
}