package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

class MisOrdenesActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvOrdenes: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var tvTotalOrdenes: TextView
    private lateinit var tvCompletadas: TextView
    private lateinit var tvPendientes: TextView
    private lateinit var btnExplorar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_ordenes)

        toolbar = findViewById(R.id.toolbar)
        rvOrdenes = findViewById(R.id.rvOrdenes)
        layoutVacio = findViewById(R.id.layoutVacio)
        tvTotalOrdenes = findViewById(R.id.tvTotalOrdenes)
        tvCompletadas = findViewById(R.id.tvCompletadas)
        tvPendientes = findViewById(R.id.tvPendientes)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        cargarEstadisticas()
        cargarOrdenes()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Órdenes"
    }

    private fun cargarEstadisticas() {
        tvTotalOrdenes.text = "3"
        tvCompletadas.text = "2"
        tvPendientes.text = "1"
    }

    private fun cargarOrdenes() {
        rvOrdenes.visibility = android.view.View.GONE
        layoutVacio.visibility = android.view.View.VISIBLE
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            startActivity(Intent(this, ListadoEmprendimientosActivity::class.java))
        }
    }
}