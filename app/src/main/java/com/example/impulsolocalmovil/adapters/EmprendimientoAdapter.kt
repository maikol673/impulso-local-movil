package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.Emprendimiento

class EmprendimientoAdapter(
    private var items: List<Emprendimiento>,
    private val onItemClick: (Emprendimiento) -> Unit
) : RecyclerView.Adapter<EmprendimientoAdapter.ViewHolder>() {

    val currentList: List<Emprendimiento>
        get() = items

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivImagen)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = itemView.findViewById(R.id.tvCategoria)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvUbicacion: TextView = itemView.findViewById(R.id.tvUbicacion)
        val tvEtiqueta: TextView = itemView.findViewById(R.id.tvEtiqueta)
        val btnVerDetalle: Button = itemView.findViewById(R.id.btnVerDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_emprendimiento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvNombre.text = item.nombre
        holder.tvCategoria.text = item.categoria
        holder.tvDescripcion.text = item.descripcion
        holder.tvRating.text = "⭐ ${item.rating}"
        holder.tvUbicacion.text = "📍 ${item.ubicacion}"

        // Configurar etiqueta según estado
        when (item.estado) {
            "destacado" -> {
                holder.tvEtiqueta.text = "Destacado"
                holder.tvEtiqueta.visibility = android.view.View.VISIBLE
                holder.tvEtiqueta.setBackgroundResource(R.drawable.bg_etiqueta_destacado)
            }
            "nuevo" -> {
                holder.tvEtiqueta.text = "Nuevo"
                holder.tvEtiqueta.visibility = android.view.View.VISIBLE
                holder.tvEtiqueta.setBackgroundResource(R.drawable.bg_etiqueta_nuevo)
            }
            else -> {
                holder.tvEtiqueta.visibility = android.view.View.GONE
            }
        }

        // Placeholder para imagen (luego con Glide)
        holder.ivImagen.setImageResource(R.drawable.ic_placeholder)

        holder.btnVerDetalle.setOnClickListener {
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