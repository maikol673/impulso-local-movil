package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvEmprendimiento: TextView
    private lateinit var etNombreProducto: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etStock: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var tvNombreArchivo: TextView
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)

        toolbar = findViewById(R.id.toolbar)
        tvEmprendimiento = findViewById(R.id.tvEmprendimiento)
        etNombreProducto = findViewById(R.id.etNombreProducto)
        etPrecio = findViewById(R.id.etPrecio)
        etDescripcion = findViewById(R.id.etDescripcion)
        etStock = findViewById(R.id.etStock)
        spinnerEstado = findViewById(R.id.spinnerEstado)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        tvNombreArchivo = findViewById(R.id.tvNombreArchivo)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        setupToolbar()
        setupSpinner()
        setupClickListeners()

        // Recibir datos del intent
        val nombreEmprendimiento = intent.getStringExtra("emprendimiento_nombre") ?: "Este emprendimiento"
        tvEmprendimiento.text = "Emprendimiento: $nombreEmprendimiento"
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Agregar Producto"
    }

    private fun setupSpinner() {
        val estados = arrayOf("Activo", "Inactivo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapter
    }

    private fun setupClickListeners() {
        btnSeleccionarImagen.setOnClickListener {
            Toast.makeText(this, "Selector de imagen - Próximamente", Toast.LENGTH_SHORT).show()
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombreProducto.text.toString().trim()
            val precio = etPrecio.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val stock = etStock.text.toString().trim()

            when {
                nombre.isEmpty() -> etNombreProducto.error = "Ingresa el nombre del producto"
                precio.isEmpty() -> etPrecio.error = "Ingresa el precio"
                descripcion.isEmpty() -> etDescripcion.error = "Ingresa la descripción"
                stock.isEmpty() -> etStock.error = "Ingresa el stock"
                else -> {
                    Toast.makeText(this, "✅ Producto '$nombre' agregado", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}