package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.AdminEmprendimientoAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.AdminStats
import com.example.impulsolocalmovil.models.Emprendimiento
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvTotalEmprendimientos: TextView
    private lateinit var tvTotalUsuarios: TextView
    private lateinit var tvTotalOrdenes: TextView
    private lateinit var tvTotalResenas: TextView
    private lateinit var rvRecentVentures: RecyclerView
    private lateinit var layoutLoading: LinearLayout
    private lateinit var layoutError: LinearLayout
    private lateinit var btnReintentar: Button
    private lateinit var btnVolver: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: AdminEmprendimientoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        tvTotalEmprendimientos = findViewById(R.id.tvTotalEmprendimientos)
        tvTotalUsuarios = findViewById(R.id.tvTotalUsuarios)
        tvTotalOrdenes = findViewById(R.id.tvTotalOrdenes)
        tvTotalResenas = findViewById(R.id.tvTotalResenas)
        rvRecentVentures = findViewById(R.id.rvRecentVentures)
        layoutLoading = findViewById(R.id.layoutLoading)
        layoutError = findViewById(R.id.layoutError)
        btnReintentar = findViewById(R.id.btnReintentar)
        btnVolver = findViewById(R.id.btnVolver)

        setupToolbar()
        setupRecyclerView()
        cargarDatosAdmin()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Admin Panel"
    }

    private fun setupRecyclerView() {
        adapter = AdminEmprendimientoAdapter(emptyList()) { emprendimiento ->
            val intent = Intent(this, DetalleEmprendimientoActivity::class.java)
            intent.putExtra("emprendimiento_id", emprendimiento.id)
            startActivity(intent)
        }
        rvRecentVentures.layoutManager = LinearLayoutManager(this)
        rvRecentVentures.adapter = adapter
    }

    private fun cargarDatosAdmin() {
        layoutLoading.visibility = View.VISIBLE
        layoutError.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Cargar estadísticas
                val statsResponse = RetrofitClient.instance.getAdminStats()
                val venturesResponse = RetrofitClient.instance.getVentures()

                withContext(Dispatchers.Main) {
                    layoutLoading.visibility = View.GONE

                    if (statsResponse.isSuccessful && venturesResponse.isSuccessful) {
                        val stats = statsResponse.body()
                        val ventures = venturesResponse.body() ?: emptyList()

                        if (stats != null) {
                            mostrarEstadisticas(stats)
                        }

                        // Últimos 3 emprendimientos
                        val recentVentures = ventures.sortedByDescending { it.id }.take(3)
                        if (recentVentures.isNotEmpty()) {
                            adapter.updateList(recentVentures)
                        }

                    } else {
                        mostrarError()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    layoutLoading.visibility = View.GONE
                    mostrarError()
                    Toast.makeText(
                        this@AdminPanelActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun mostrarEstadisticas(stats: AdminStats) {
        tvTotalEmprendimientos.text = stats.totalEmprendimientos.toString()
        tvTotalUsuarios.text = stats.totalUsuarios.toString()
        tvTotalOrdenes.text = stats.totalOrdenes.toString()
        tvTotalResenas.text = stats.totalResenas.toString()
    }

    private fun mostrarError() {
        layoutError.visibility = View.VISIBLE
    }

    private fun setupClickListeners() {
        btnReintentar.setOnClickListener {
            cargarDatosAdmin()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}