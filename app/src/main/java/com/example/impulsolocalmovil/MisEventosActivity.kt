package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.EventoAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.AsistenciaEvento
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class MisEventosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvMisEventos: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var btnExplorar: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: EventoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_eventos)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        rvMisEventos = findViewById(R.id.rvMisEventos)
        layoutVacio = findViewById(R.id.layoutVacio)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        setupRecyclerView()
        cargarMisEventos()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Eventos"
    }

    private fun setupRecyclerView() {
        adapter = EventoAdapter(emptyList()) { asistencia ->
            val eventoId = asistencia.eventoId
            if (eventoId > 0) {
                val intent = Intent(this, DetalleEventoActivity::class.java)
                intent.putExtra("evento_id", eventoId)
                intent.putExtra("asistencia_id", asistencia.id) // ✅ ahora sí se envía
                startActivity(intent)
            } else {
                Toast.makeText(this, "No se pudo abrir el detalle del evento", Toast.LENGTH_SHORT).show()
            }
        }
        rvMisEventos.layoutManager = LinearLayoutManager(this)
        rvMisEventos.adapter = adapter
    }

    private fun cargarMisEventos() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMyEvents(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val asistencias = response.body() ?: emptyList()

                        if (asistencias.isEmpty()) {
                            rvMisEventos.visibility = android.view.View.GONE
                            layoutVacio.visibility = android.view.View.VISIBLE
                        } else {
                            rvMisEventos.visibility = android.view.View.VISIBLE
                            layoutVacio.visibility = android.view.View.GONE
                            adapter.updateList(asistencias)
                        }
                    } else {
                        Toast.makeText(
                            this@MisEventosActivity,
                            "Error al cargar eventos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MisEventosActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            startActivity(Intent(this, NetworkingActivity::class.java))
        }
    }
}