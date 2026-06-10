package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class PublicarEmprendimientoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var etUbicacion: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnPublicar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var tvNombreArchivo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicar_emprendimiento)

        toolbar = findViewById(R.id.toolbar)
        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        etUbicacion = findViewById(R.id.etUbicacion)
        spinnerEstado = findViewById(R.id.spinnerEstado)
        btnPublicar = findViewById(R.id.btnPublicar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        tvNombreArchivo = findViewById(R.id.tvNombreArchivo)

        setupToolbar()
        setupSpinners()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Publicar Emprendimiento"
    }

    private fun setupSpinners() {
        val categorias = arrayOf("Tecnología", "Alimentos", "Servicios", "Moda", "Artesanías")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = adapter

        val estados = arrayOf("Activo", "Destacado", "Nuevo")
        val estadoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = estadoAdapter
    }

    private fun setupClickListeners() {
        btnSeleccionarImagen.setOnClickListener {
            Toast.makeText(this, "Selector de imagen - Próximamente", Toast.LENGTH_SHORT).show()
        }

        btnPublicar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val ubicacion = etUbicacion.text.toString().trim()

            when {
                nombre.isEmpty() -> etNombre.error = "Ingresa el nombre"
                descripcion.isEmpty() -> etDescripcion.error = "Ingresa la descripción"
                ubicacion.isEmpty() -> etUbicacion.error = "Ingresa la ubicación"
                else -> {
                    Toast.makeText(this, "✅ Emprendimiento '$nombre' publicado", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}