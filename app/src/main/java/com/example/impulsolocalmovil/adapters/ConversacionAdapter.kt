package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.ConversacionResumen
import com.example.impulsolocalmovil.models.Chat


class ConversacionAdapter(
    private var items: List<ConversacionResumen>,
    private val onClick: (ConversacionResumen) -> Unit
) : RecyclerView.Adapter<ConversacionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvUltimoMensaje: TextView = itemView.findViewById(R.id.tvUltimoMensaje)
        val tvNoLeidos: TextView = itemView.findViewById(R.id.tvNoLeidos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conversacion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvNombre.text = item.otroUsuario.fullName ?: item.otroUsuario.username ?: "Usuario"
        holder.tvUltimoMensaje.text = item.ultimoMensaje ?: "Sin mensajes aún"

        if (item.mensajesNoLeidos > 0) {
            holder.tvNoLeidos.visibility = View.VISIBLE
            holder.tvNoLeidos.text = item.mensajesNoLeidos.toString()
        } else {
            holder.tvNoLeidos.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<ConversacionResumen>) {
        items = newItems
        notifyDataSetChanged()
    }
}