package com.example.impulsolocalmovil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.impulsolocalmovil.R
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Producto
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class ProductoAdapter(
    private var items: List<Producto>,
    private val onAgregarCarrito: (Producto) -> Unit,
    private val onProductoEliminado: () -> Unit,
    private val esDueño: Boolean = false  // ✅ Valor por defecto
) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    companion object {
        private const val BASE_URL = "http://172.20.10.6:8000"
    }

    class ViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        val ivImagen: ImageView = itemView.findViewById(R.id.ivImagen)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        val btnAgregarCarrito: Button = itemView.findViewById(R.id.btnAgregarCarrito)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

        holder.tvNombre.text = item.nombre
        holder.tvDescripcion.text = item.descripcion
        holder.tvPrecio.text = formatter.format(item.precio)
        holder.tvStock.text = "Stock: ${item.stock}"

        // Cargar imagen
        val imagenUrl = if (!item.imagen.isNullOrEmpty()) {
            val rutaLimpia = if (item.imagen.startsWith("/")) item.imagen else "/${item.imagen}"
            BASE_URL + rutaLimpia
        } else {
            null
        }

        Glide.with(holder.itemView.context)
            .load(imagenUrl)
            .placeholder(R.drawable.ic_placeholder_producto)
            .error(R.drawable.ic_placeholder_producto)
            .centerCrop()
            .into(holder.ivImagen)

        holder.btnAgregarCarrito.setOnClickListener {
            onAgregarCarrito(item)
        }

        // ✅ Mostrar botón eliminar solo si es dueño
        if (esDueño) {
            holder.btnEliminar.visibility = android.view.View.VISIBLE
            holder.btnEliminar.setOnClickListener {
                eliminarProducto(item, holder)
            }
        } else {
            holder.btnEliminar.visibility = android.view.View.GONE
        }
    }

    private fun eliminarProducto(item: Producto, holder: ViewHolder) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.deleteProduct(item.id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            holder.itemView.context,
                            "✅ Producto eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                        onProductoEliminado()
                    } else {
                        Toast.makeText(
                            holder.itemView.context,
                            "❌ Error al eliminar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        holder.itemView.context,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<Producto>) {
        items = newItems
        notifyDataSetChanged()
    }
}