package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AgregarResenaActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvTitulo: TextView
    private lateinit var tvEmprendimiento: TextView
    private lateinit var llEstrellas: LinearLayout
    private lateinit var star1: TextView
    private lateinit var star2: TextView
    private lateinit var star3: TextView
    private lateinit var star4: TextView
    private lateinit var star5: TextView
    private lateinit var etCalificacion: EditText
    private lateinit var etComentario: EditText
    private lateinit var btnPublicar: Button
    private lateinit var btnCancelar: Button

    private var calificacionSeleccionada = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_resena)

        toolbar = findViewById(R.id.toolbar)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvEmprendimiento = findViewById(R.id.tvEmprendimiento)
        llEstrellas = findViewById(R.id.llEstrellas)
        star1 = findViewById(R.id.star1)
        star2 = findViewById(R.id.star2)
        star3 = findViewById(R.id.star3)
        star4 = findViewById(R.id.star4)
        star5 = findViewById(R.id.star5)
        etCalificacion = findViewById(R.id.etCalificacion)
        etComentario = findViewById(R.id.etComentario)
        btnPublicar = findViewById(R.id.btnPublicar)
        btnCancelar = findViewById(R.id.btnCancelar)

        setupToolbar()
        setupStars()
        setupClickListeners()

        // Recibir datos del intent
        val nombreEmprendimiento = intent.getStringExtra("emprendimiento_nombre") ?: "Este emprendimiento"
        tvEmprendimiento.text = "para $nombreEmprendimiento"
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = ""
    }

    private fun setupStars() {
        val stars = listOf(star1, star2, star3, star4, star5)

        stars.forEachIndexed { index, star ->
            star.setOnClickListener {
                calificacionSeleccionada = index + 1
                actualizarEstrellas(calificacionSeleccionada)
                etCalificacion.setText(calificacionSeleccionada.toString())
            }
        }
    }

    private fun actualizarEstrellas(calificacion: Int) {
        val stars = listOf(star1, star2, star3, star4, star5)
        stars.forEachIndexed { index, star ->
            if (index < calificacion) {
                star.text = "★"
                star.setTextColor(resources.getColor(R.color.warning, null))
            } else {
                star.text = "☆"
                star.setTextColor(resources.getColor(R.color.text_secondary, null))
            }
        }
    }

    private fun setupClickListeners() {
        btnPublicar.setOnClickListener {
            val comentario = etComentario.text.toString().trim()

            when {
                calificacionSeleccionada == 0 -> {
                    Toast.makeText(this, "Selecciona una calificación", Toast.LENGTH_SHORT).show()
                }
                comentario.isEmpty() -> {
                    etComentario.error = "Escribe tu comentario"
                }
                else -> {
                    Toast.makeText(this, "✅ Reseña publicada\nCalificación: $calificacionSeleccionada estrellas", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}