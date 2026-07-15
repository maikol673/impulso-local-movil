package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.CarritoAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.AddToCartRequest
import com.example.impulsolocalmovil.models.CarritoItem
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class MiCarritoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvCarrito: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var layoutResumen: LinearLayout
    private lateinit var tvTotal: TextView
    private lateinit var tvCantidadItems: TextView
    private lateinit var btnActualizar: Button
    private lateinit var btnFinalizar: Button
    private lateinit var btnSeguirComprando: Button
    private lateinit var btnExplorar: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: CarritoAdapter
    private var itemsCarrito = mutableListOf<CarritoItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        tokenManager = TokenManager.getInstance(this)

        // Inicializar views
        toolbar = findViewById(R.id.toolbar)
        rvCarrito = findViewById(R.id.rvCarrito)
        layoutVacio = findViewById(R.id.layoutVacio)
        layoutResumen = findViewById(R.id.layoutResumen)
        tvTotal = findViewById(R.id.tvTotal)
        tvCantidadItems = findViewById(R.id.tvCantidadItems)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnFinalizar = findViewById(R.id.btnFinalizar)
        btnSeguirComprando = findViewById(R.id.btnSeguirComprando)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        setupRecyclerView()
        cargarCarrito()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mi Carrito"
    }

    private fun setupRecyclerView() {
        adapter = CarritoAdapter(
            items = itemsCarrito,
            onCantidadChanged = { item, nuevaCantidad ->
                actualizarCantidad(item, nuevaCantidad)
            },
            onEliminar = { item ->
                eliminarItem(item)
            }
        )
        rvCarrito.layoutManager = LinearLayoutManager(this)
        rvCarrito.adapter = adapter
    }

    private fun cargarCarrito() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getCart(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val carrito = response.body()
                        if (carrito != null) {
                            itemsCarrito.clear()
                            itemsCarrito.addAll(carrito.items)
                            actualizarUI()
                        } else {
                            itemsCarrito.clear()
                            actualizarUI()
                        }
                    } else {
                        Toast.makeText(
                            this@MiCarritoActivity,
                            "Error al cargar el carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MiCarritoActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun actualizarUI() {
        if (itemsCarrito.isEmpty()) {
            rvCarrito.visibility = android.view.View.GONE
            layoutResumen.visibility = android.view.View.GONE
            layoutVacio.visibility = android.view.View.VISIBLE
        } else {
            rvCarrito.visibility = android.view.View.VISIBLE
            layoutResumen.visibility = android.view.View.VISIBLE
            layoutVacio.visibility = android.view.View.GONE
            adapter.updateList(itemsCarrito)
            actualizarTotal()
        }
    }

    private fun actualizarTotal() {
        var total = 0.0
        for (item in itemsCarrito) {
            val precio = item.precio.toDoubleOrNull() ?: 0.0
            total += precio * item.cantidad
        }
        tvTotal.text = "$ ${"%.2f".format(total)}"
        tvCantidadItems.text = "${itemsCarrito.size} items"
    }

    private fun obtenerTotal(): Double {
        var total = 0.0
        for (item in itemsCarrito) {
            val precio = item.precio.toDoubleOrNull() ?: 0.0
            total += precio * item.cantidad
        }
        return total
    }

    private fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val usuarioId = tokenManager.getUser()?.id ?: 0
                val request = AddToCartRequest(item.productoId, usuarioId, nuevaCantidad)
                val response = RetrofitClient.instance.addToCart(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        cargarCarrito()
                    } else {
                        Toast.makeText(
                            this@MiCarritoActivity,
                            "Error al actualizar cantidad",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MiCarritoActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun eliminarItem(item: CarritoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.removeFromCart(item.id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@MiCarritoActivity,
                            "✅ Producto eliminado",
                            Toast.LENGTH_SHORT
                        ).show()
                        cargarCarrito()
                    } else {
                        Toast.makeText(
                            this@MiCarritoActivity,
                            "❌ Error al eliminar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MiCarritoActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            startActivity(Intent(this, ListadoEmprendimientosActivity::class.java))
        }

        btnSeguirComprando.setOnClickListener {
            startActivity(Intent(this, ListadoEmprendimientosActivity::class.java))
        }

        btnActualizar.setOnClickListener {
            cargarCarrito()
        }

        btnFinalizar.setOnClickListener {
            val total = obtenerTotal()
            if (total > 0) {
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("total", total)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}