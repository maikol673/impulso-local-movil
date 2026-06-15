package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class CrearTestimonioActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var etNombre: EditText
    private lateinit var etEmpresa: EditText
    private lateinit var etContenido: EditText
    private lateinit var btnPublicar: Button
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_testimonio)

        toolbar = findViewById(R.id.toolbar)
        etNombre = findViewById(R.id.etNombre)
        etEmpresa = findViewById(R.id.etEmpresa)
        etContenido = findViewById(R.id.etContenido)
        btnPublicar = findViewById(R.id.btnPublicar)
        btnVolver = findViewById(R.id.btnVolver)

        setupToolbar()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Compartir Experiencia"
    }

    private fun setupClickListeners() {
        btnPublicar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val contenido = etContenido.text.toString().trim()

            when {
                nombre.isEmpty() -> etNombre.error = "Ingresa tu nombre"
                contenido.isEmpty() -> etContenido.error = "Escribe tu testimonio"
                else -> {
                    Toast.makeText(this, "✅ Testimonio publicado. ¡Gracias!", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}