package com.example.impulsolocalmovil

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.models.Emprendimiento

class DetalleEmprendimientoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var ivImagen: ImageView
    private lateinit var tvDestacado: TextView
    private lateinit var tvNombre: TextView
    private lateinit var tvCategoria: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvNumResenas: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var tvSitioWeb: TextView
    private lateinit var btnDejarResena: Button
    private lateinit var btnMeGusta: Button
    private lateinit var btnContactar: Button
    private lateinit var btnSeguir: Button
    private lateinit var layoutEdicion: LinearLayout
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_emprendimiento)

        // Inicializar views
        toolbar = findViewById(R.id.toolbar)
        ivImagen = findViewById(R.id.ivImagen)
        tvDestacado = findViewById(R.id.tvDestacado)
        tvNombre = findViewById(R.id.tvNombre)
        tvCategoria = findViewById(R.id.tvCategoria)
        tvRating = findViewById(R.id.tvRating)
        tvNumResenas = findViewById(R.id.tvNumResenas)
        tvUbicacion = findViewById(R.id.tvUbicacion)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvEmail = findViewById(R.id.tvEmail)
        tvTelefono = findViewById(R.id.tvTelefono)
        tvSitioWeb = findViewById(R.id.tvSitioWeb)
        btnDejarResena = findViewById(R.id.btnDejarResena)
        btnMeGusta = findViewById(R.id.btnMeGusta)
        btnContactar = findViewById(R.id.btnContactar)
        btnSeguir = findViewById(R.id.btnSeguir)
        layoutEdicion = findViewById(R.id.layoutEdicion)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)

        setupToolbar()

        // Recibir datos del intent
        val emprendimiento = intent.getSerializableExtra("emprendimiento") as? Emprendimiento

        if (emprendimiento != null) {
            mostrarDatos(emprendimiento)
        }

        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        title = ""
    }

    private fun mostrarDatos(emprendimiento: Emprendimiento) {
        tvNombre.text = emprendimiento.nombre
        tvCategoria.text = emprendimiento.categoria
        tvRating.text = "⭐ ${emprendimiento.rating}"
        tvNumResenas.text = "(120 reseñas)"
        tvUbicacion.text = "📍 ${emprendimiento.ubicacion}"
        tvDescripcion.text = emprendimiento.descripcion

        // Configurar etiqueta destacado
        when (emprendimiento.estado) {
            "destacado" -> {
                tvDestacado.text = "Destacado"
                tvDestacado.visibility = android.view.View.VISIBLE
            }
            "nuevo" -> {
                tvDestacado.text = "Nuevo"
                tvDestacado.visibility = android.view.View.VISIBLE
                tvDestacado.setBackgroundResource(R.drawable.bg_etiqueta_nuevo)
            }
            else -> {
                tvDestacado.visibility = android.view.View.GONE
            }
        }

        // Datos de contacto (ejemplo)
        tvEmail.text = "✉ Email: contacto@${emprendimiento.nombre.lowercase()}.com"
        tvTelefono.text = "📱 Teléfono: +57 300 123 4567"
        tvSitioWeb.text = "🌐 Web: www.${emprendimiento.nombre.lowercase()}.com"
    }

    private fun setupClickListeners() {
        btnDejarResena.setOnClickListener {
            val intent = Intent(this, AgregarResenaActivity::class.java)
            intent.putExtra("emprendimiento_nombre", tvNombre.text.toString())
            startActivity(intent)
        }

        btnMeGusta.setOnClickListener {
            // TODO: Dar me gusta
        }

        btnContactar.setOnClickListener {
            // TODO: Abrir chat
        }

        btnSeguir.setOnClickListener {
            // TODO: Seguir emprendimiento
        }

        btnEditar.setOnClickListener {
            // TODO: Editar emprendimiento
        }

        btnEliminar.setOnClickListener {
            // TODO: Eliminar emprendimiento
        }
    }
}