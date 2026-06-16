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

class MiCarritoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvCarrito: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var layoutResumen: LinearLayout
    private lateinit var tvTotal: TextView
    private lateinit var btnFinalizar: Button
    private lateinit var btnSeguirComprando: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        toolbar = findViewById(R.id.toolbar)
        rvCarrito = findViewById(R.id.rvCarrito)
        layoutVacio = findViewById(R.id.layoutVacio)
        layoutResumen = findViewById(R.id.layoutResumen)
        tvTotal = findViewById(R.id.tvTotal)
        btnFinalizar = findViewById(R.id.btnFinalizar)
        btnSeguirComprando = findViewById(R.id.btnSeguirComprando)

        setupToolbar()
        cargarCarrito()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mi Carrito"
    }

    private fun cargarCarrito() {
        // Por ahora sin datos
        val itemsCarrito = emptyList<Any>()

        if (itemsCarrito.isEmpty()) {
            rvCarrito.visibility = android.view.View.GONE
            layoutResumen.visibility = android.view.View.GONE
            layoutVacio.visibility = android.view.View.VISIBLE
        } else {
            rvCarrito.visibility = android.view.View.VISIBLE
            layoutResumen.visibility = android.view.View.VISIBLE
            layoutVacio.visibility = android.view.View.GONE
        }
    }

    private fun setupClickListeners() {
        val btnExplorar = findViewById<Button>(R.id.btnExplorar)
        btnExplorar.setOnClickListener {
            startActivity(Intent(this, ListadoEmprendimientosActivity::class.java))
        }

        btnSeguirComprando.setOnClickListener {
            startActivity(Intent(this, ListadoEmprendimientosActivity::class.java))
        }

        btnFinalizar.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }
}