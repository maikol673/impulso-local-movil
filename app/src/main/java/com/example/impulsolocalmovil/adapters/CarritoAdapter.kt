package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.models.CarritoItem
import java.text.NumberFormat
import java.util.*

class CarritoAdapter(
    private var items: List<CarritoItem>,
    private val onCantidadChanged: (CarritoItem, Int) -> Unit,
    private val onEliminar: (CarritoItem) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvEmprendimiento: TextView = itemView.findViewById(R.id.tvEmprendimiento)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val tvSubtotal: TextView = itemView.findViewById(R.id.tvSubtotal)
        val btnRestar: Button = itemView.findViewById(R.id.btnRestar)
        val btnSumar: Button = itemView.findViewById(R.id.btnSumar)
        val btnEliminar: TextView = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvNombre.text = item.nombre
        holder.tvEmprendimiento.text = item.emprendimientoNombre ?: "Emprendimiento"

        // Formatear precio
        val precioDouble = item.precio.toDoubleOrNull() ?: 0.0
        val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        holder.tvPrecio.text = formatter.format(precioDouble)

        holder.tvCantidad.text = item.cantidad.toString()

        val subtotal = precioDouble * item.cantidad
        holder.tvSubtotal.text = formatter.format(subtotal)

        holder.btnRestar.setOnClickListener {
            if (item.cantidad > 1) {
                onCantidadChanged(item, item.cantidad - 1)
            }
        }

        holder.btnSumar.setOnClickListener {
            onCantidadChanged(item, item.cantidad + 1)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminar(item)
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<CarritoItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}