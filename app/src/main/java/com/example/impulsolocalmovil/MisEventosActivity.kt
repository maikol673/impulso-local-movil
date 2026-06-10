package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.EventoAdapter
import com.example.impulsolocalmovil.models.Evento

class MisEventosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvMisEventos: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var btnExplorar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_eventos)

        toolbar = findViewById(R.id.toolbar)
        rvMisEventos = findViewById(R.id.rvMisEventos)
        layoutVacio = findViewById(R.id.layoutVacio)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        cargarEventos()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Eventos"
    }

    private fun cargarEventos() {
        val eventos = listOf(
            Evento(1, "Webinar: Marketing Digital", "Aprende a vender en redes sociales", "15/06/2025", "4:00 PM", "Virtual", "confirmado"),
            Evento(2, "Networking: Emprendedores Tech", "Conecta con otros emprendedores", "20/06/2025", "6:00 PM", "Presencial", "pendiente")
        )

        if (eventos.isEmpty()) {
            rvMisEventos.visibility = android.view.View.GONE
            layoutVacio.visibility = android.view.View.VISIBLE
        } else {
            rvMisEventos.visibility = android.view.View.VISIBLE
            layoutVacio.visibility = android.view.View.GONE

            val adapter = EventoAdapter(eventos) { evento ->
                // TODO: Navegar a detalle del evento
            }

            rvMisEventos.layoutManager = LinearLayoutManager(this)
            rvMisEventos.adapter = adapter
        }
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            // TODO: Navegar a explorar eventos
        }
    }
}