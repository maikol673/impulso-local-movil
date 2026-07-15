package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Evento
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class DetalleEventoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvNombre: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvFecha: TextView
    private lateinit var tvHora: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var tvTipo: TextView
    private lateinit var btnCancelarAsistencia: Button
    private lateinit var btnVolver: Button

    private lateinit var tokenManager: TokenManager
    private var eventoId: Int = 0
    private var asistenciaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_evento)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        tvNombre = findViewById(R.id.tvNombre)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvFecha = findViewById(R.id.tvFecha)
        tvHora = findViewById(R.id.tvHora)
        tvUbicacion = findViewById(R.id.tvUbicacion)
        tvTipo = findViewById(R.id.tvTipo)
        btnCancelarAsistencia = findViewById(R.id.btnCancelarAsistencia)
        btnVolver = findViewById(R.id.btnVolver)

        setupToolbar()

        eventoId = intent.getIntExtra("evento_id", 0)
        asistenciaId = intent.getIntExtra("asistencia_id", 0)

        if (eventoId > 0) {
            cargarDetalleEvento()
        }

        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Detalle del Evento"
    }

    private fun cargarDetalleEvento() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getEventById(eventoId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val evento = response.body()
                        if (evento != null) {
                            mostrarDatos(evento)
                        }
                    } else {
                        Toast.makeText(
                            this@DetalleEventoActivity,
                            "Error al cargar el evento",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleEventoActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun mostrarDatos(evento: Evento) {
        tvNombre.text = evento.nombre
        tvDescripcion.text = evento.descripcion
        tvFecha.text = "📅 Fecha: ${evento.fecha}"
        tvHora.text = "⏰ Hora: ${evento.hora}"
        tvUbicacion.text = "📍 Ubicación: ${evento.ubicacion ?: "No especificada"}"
        tvTipo.text = "🎯 Modalidad: ${evento.tipo}"
    }

    private fun setupClickListeners() {
        btnCancelarAsistencia.setOnClickListener {
            cancelarAsistencia()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cancelarAsistencia() {
        if (asistenciaId == 0) {
            Toast.makeText(this, "Error: No se encontró la asistencia", Toast.LENGTH_SHORT).show()
            return
        }

        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Sesión inválida o expirada", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.cancelAttendance(asistenciaId, usuarioId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@DetalleEventoActivity,
                            "✅ Asistencia cancelada",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@DetalleEventoActivity,
                            "❌ Error al cancelar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleEventoActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}