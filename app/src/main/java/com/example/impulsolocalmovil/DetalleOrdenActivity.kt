package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.OrdenItemAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Orden
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class DetalleOrdenActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvNumero: TextView
    private lateinit var tvFecha: TextView
    private lateinit var tvEstado: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvDireccion: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var rvItems: RecyclerView
    private lateinit var btnCancelar: Button
    private lateinit var btnVolver: Button

    private lateinit var tokenManager: TokenManager
    private var ordenId: Int = 0
    private var orden: Orden? = null
    private lateinit var adapter: OrdenItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_orden)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        tvNumero = findViewById(R.id.tvNumero)
        tvFecha = findViewById(R.id.tvFecha)
        tvEstado = findViewById(R.id.tvEstado)
        tvTotal = findViewById(R.id.tvTotal)
        tvDireccion = findViewById(R.id.tvDireccion)
        tvTelefono = findViewById(R.id.tvTelefono)
        rvItems = findViewById(R.id.rvItems)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnVolver = findViewById(R.id.btnVolver)

        setupToolbar()

        ordenId = intent.getIntExtra("orden_id", 0)
        if (ordenId > 0) {
            cargarDetalleOrden()
        }

        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Detalle de Orden"
    }

    private fun cargarDetalleOrden() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getOrderById(ordenId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        orden = response.body()
                        if (orden != null) {
                            mostrarDatos(orden!!)
                        }
                    } else {
                        Toast.makeText(
                            this@DetalleOrdenActivity,
                            "Error al cargar la orden",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleOrdenActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun mostrarDatos(orden: Orden) {
        val formatter = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

        tvNumero.text = "Orden #${orden.id}"
        tvFecha.text = "📅 ${orden.fechaCreacion}"
        tvTotal.text = formatter.format(orden.total)
        tvDireccion.text = "📍 ${orden.direccionEnvio}"
        tvTelefono.text = "📞 ${orden.telefonoContacto}"

        when (orden.estado) {
            "entregada", "completada" -> {
                tvEstado.text = "✅ Completada"
                tvEstado.setBackgroundResource(R.drawable.bg_estado_badge)
                btnCancelar.isEnabled = false
            }
            "cancelada" -> {
                tvEstado.text = "❌ Cancelada"
                tvEstado.setBackgroundResource(R.drawable.bg_estado_cancelada)
                btnCancelar.isEnabled = false
            }
            else -> {
                tvEstado.text = "⏳ Pendiente"
                tvEstado.setBackgroundResource(R.drawable.bg_estado_pendiente)
                btnCancelar.isEnabled = true
            }
        }

        adapter = OrdenItemAdapter(orden.items ?: emptyList())
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapter
    }

    private fun setupClickListeners() {
        btnCancelar.setOnClickListener {
            cancelarOrden()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cancelarOrden() {
        if (ordenId == 0) {
            Toast.makeText(this, "Error: Orden no encontrada", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.cancelOrder(ordenId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@DetalleOrdenActivity,
                            "✅ Orden cancelada",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@DetalleOrdenActivity,
                            "❌ Error al cancelar la orden",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleOrdenActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}