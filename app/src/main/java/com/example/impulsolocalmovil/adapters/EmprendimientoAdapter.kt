package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.Emprendimiento

class EmprendimientoAdapter(
    private var items: List<Emprendimiento>,
    private val onItemClick: (Emprendimiento) -> Unit
) : RecyclerView.Adapter<EmprendimientoAdapter.ViewHolder>() {

    companion object {
        private const val BASE_URL = "http://172.20.10.6:8000"
    }

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
        holder.tvCategoria.text = item.categoria?.nombre ?: "Sin categoría"
        holder.tvDescripcion.text = item.descripcion
        holder.tvRating.text = "⭐ ${item.calificacion ?: "4.5"}"
        holder.tvUbicacion.text = "📍 ${item.ubicacion ?: "Ubicación no especificada"}"

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

        // ✅ Cargar imagen con la estructura corregida
        val imagenUrl = if (!item.imagen.isNullOrEmpty()) {
            val rutaLimpia = if (item.imagen.startsWith("/")) item.imagen else "/${item.imagen}"
            BASE_URL + rutaLimpia
        } else {
            null
        }

        Glide.with(holder.itemView.context)
            .load(imagenUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .centerCrop()
            .into(holder.ivImagen)

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