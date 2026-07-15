package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.OrdenItem
import java.text.NumberFormat
import java.util.*

class OrdenItemAdapter(
    private var items: List<OrdenItem>
) : RecyclerView.Adapter<OrdenItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orden_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

        holder.tvNombre.text = item.producto?.nombre ?: "Producto"
        holder.tvCantidad.text = "x${item.cantidad}"
        holder.tvPrecio.text = formatter.format(item.precio)
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<OrdenItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}