package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.Orden
import java.text.NumberFormat
import java.util.*

class OrdenAdapter(
    private var items: List<Orden>,
    private val onItemClick: (Orden) -> Unit
) : RecyclerView.Adapter<OrdenAdapter.ViewHolder>() {

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvNumero: TextView = itemView.findViewById(R.id.tvNumero)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val tvProductos: TextView = itemView.findViewById(R.id.tvProductos)
        val btnVerDetalle: Button = itemView.findViewById(R.id.btnVerDetalle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orden, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

        holder.tvNumero.text = "Orden #${item.id}"
        holder.tvFecha.text = item.fechaCreacion.substring(0, 10)
        holder.tvTotal.text = formatter.format(item.total)

        // Estado
        when (item.estado) {
            "entregada", "completada" -> {
                holder.tvEstado.text = "✅ Completada"
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_badge)
            }
            "cancelada" -> {
                holder.tvEstado.text = "❌ Cancelada"
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_cancelada)
            }
            else -> {
                holder.tvEstado.text = "⏳ Pendiente"
                holder.tvEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)
            }
        }

        // Productos
        val nombres = item.items?.take(2)?.joinToString { it.producto?.nombre ?: "Producto" }
        holder.tvProductos.text = nombres ?: "Sin productos"
        if (item.items?.size ?: 0 > 2) {
            holder.tvProductos.text = "${holder.tvProductos.text} +${item.items!!.size - 2} más"
        }

        holder.btnVerDetalle.setOnClickListener {
            onItemClick(item)
        }
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<Orden>) {
        items = newItems
        notifyDataSetChanged()
    }
}