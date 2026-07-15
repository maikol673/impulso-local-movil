package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.Resena

class ResenaAdapter(
    private var items: List<Resena>
) : RecyclerView.Adapter<ResenaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsuario: TextView = itemView.findViewById(R.id.tvUsuario)
        val tvEstrellas: TextView = itemView.findViewById(R.id.tvEstrellas)
        val tvComentario: TextView = itemView.findViewById(R.id.tvComentario)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resena, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // ⚠️ Ajusta "username" / "fullName" según los nombres reales de tu modelo Usuario
        holder.tvUsuario.text = item.usuario?.username ?: "Usuario"

        holder.tvEstrellas.text = "★".repeat(item.calificacion) + "☆".repeat(5 - item.calificacion)
        holder.tvComentario.text = item.comentario
        holder.tvFecha.text = item.fechaCreacion ?: ""
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<Resena>) {
        items = newItems
        notifyDataSetChanged()
    }
}