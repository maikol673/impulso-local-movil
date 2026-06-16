package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

class CheckoutActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvResumenItems: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var etDireccion: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etNotas: EditText
    private lateinit var btnConfirmar: Button
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        toolbar = findViewById(R.id.toolbar)
        rvResumenItems = findViewById(R.id.rvResumenItems)
        tvTotal = findViewById(R.id.tvTotal)
        etDireccion = findViewById(R.id.etDireccion)
        etTelefono = findViewById(R.id.etTelefono)
        etNotas = findViewById(R.id.etNotas)
        btnConfirmar = findViewById(R.id.btnConfirmar)
        btnVolver = findViewById(R.id.btnVolver)

        setupToolbar()
        cargarResumen()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Checkout"
    }

    private fun cargarResumen() {
        // Por ahora, datos de ejemplo
        tvTotal.text = "$1,635.96"
        // TODO: Cargar items reales del carrito
    }

    private fun setupClickListeners() {
        btnConfirmar.setOnClickListener {
            val direccion = etDireccion.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()

            when {
                direccion.isEmpty() -> etDireccion.error = "Ingresa tu dirección"
                telefono.isEmpty() -> etTelefono.error = "Ingresa tu teléfono"
                else -> {
                    Toast.makeText(this, "✅ Compra confirmada", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MisOrdenesActivity::class.java))
                    finish()
                }
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}