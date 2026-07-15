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
import com.example.impulsolocalmovil.adapters.OrdenAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Orden
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class MisOrdenesActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvOrdenes: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var tvTotalOrdenes: TextView
    private lateinit var tvCompletadas: TextView
    private lateinit var tvPendientes: TextView
    private lateinit var btnExplorar: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: OrdenAdapter
    private var ordenes = mutableListOf<Orden>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_ordenes)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        rvOrdenes = findViewById(R.id.rvOrdenes)
        layoutVacio = findViewById(R.id.layoutVacio)
        tvTotalOrdenes = findViewById(R.id.tvTotalOrdenes)
        tvCompletadas = findViewById(R.id.tvCompletadas)
        tvPendientes = findViewById(R.id.tvPendientes)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        setupRecyclerView()
        cargarOrdenes()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Órdenes"
    }

    private fun setupRecyclerView() {
        adapter = OrdenAdapter(ordenes) { orden ->
            val intent = Intent(this, DetalleOrdenActivity::class.java)
            intent.putExtra("orden_id", orden.id)
            startActivity(intent)
        }
        rvOrdenes.layoutManager = LinearLayoutManager(this)
        rvOrdenes.adapter = adapter
    }

    private fun cargarOrdenes() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMyOrders(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        ordenes = response.body()?.toMutableList() ?: mutableListOf()
                        actualizarUI()
                    } else {
                        Toast.makeText(
                            this@MisOrdenesActivity,
                            "Error al cargar órdenes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MisOrdenesActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun actualizarUI() {
        if (ordenes.isEmpty()) {
            rvOrdenes.visibility = android.view.View.GONE
            layoutVacio.visibility = android.view.View.VISIBLE
        } else {
            rvOrdenes.visibility = android.view.View.VISIBLE
            layoutVacio.visibility = android.view.View.GONE
            adapter.updateList(ordenes)
            actualizarEstadisticas()
        }
    }

    private fun actualizarEstadisticas() {
        val total = ordenes.size
        val completadas = ordenes.count { it.estado == "entregada" || it.estado == "completada" }
        val pendientes = ordenes.count { it.estado != "entregada" && it.estado != "completada" && it.estado != "cancelada" }

        tvTotalOrdenes.text = total.toString()
        tvCompletadas.text = completadas.toString()
        tvPendientes.text = pendientes.toString()
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            startActivity(Intent(this, ListadoEmprendimientosActivity::class.java))
        }
    }
}