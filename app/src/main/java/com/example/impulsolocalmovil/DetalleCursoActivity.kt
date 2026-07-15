package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Curso
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class DetalleCursoActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvNombre: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvInstructor: TextView
    private lateinit var tvDuracion: TextView
    private lateinit var tvNivel: TextView
    private lateinit var tvFechaInicio: TextView
    private lateinit var btnCancelarInscripcion: Button
    private lateinit var btnVolver: Button

    private lateinit var tokenManager: TokenManager
    private var cursoId: Int = 0
    private var inscripcionId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_curso)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        tvNombre = findViewById(R.id.tvNombre)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvInstructor = findViewById(R.id.tvInstructor)
        tvDuracion = findViewById(R.id.tvDuracion)
        tvNivel = findViewById(R.id.tvNivel)
        tvFechaInicio = findViewById(R.id.tvFechaInicio)
        btnCancelarInscripcion = findViewById(R.id.btnCancelarInscripcion)
        btnVolver = findViewById(R.id.btnVolver)

        setupToolbar()

        cursoId = intent.getIntExtra("curso_id", 0)
        inscripcionId = intent.getIntExtra("inscripcion_id", 0)

        if (cursoId > 0) {
            cargarDetalleCurso()
        }

        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Detalle del Curso"
    }

    private fun cargarDetalleCurso() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getCursoById(cursoId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val curso = response.body()
                        if (curso != null) {
                            mostrarDatos(curso)
                        }
                    } else {
                        Toast.makeText(
                            this@DetalleCursoActivity,
                            "Error al cargar el curso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleCursoActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun mostrarDatos(curso: Curso) {
        tvNombre.text = curso.nombre
        tvDescripcion.text = curso.descripcion
        tvInstructor.text = "👨‍🏫 Instructor: ${curso.instructor}"
        tvDuracion.text = "⏱️ Duración: ${curso.duracion}"
        tvNivel.text = "🎯 Nivel: ${curso.nivel}"
        tvFechaInicio.text = "📅 Inicio: ${curso.fechaInicio ?: "Próximamente"}"
    }

    private fun setupClickListeners() {
        btnCancelarInscripcion.setOnClickListener {
            cancelarInscripcion()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cancelarInscripcion() {
        if (inscripcionId == 0) {
            Toast.makeText(this, "Error: No se encontró la inscripción", Toast.LENGTH_SHORT).show()
            return
        }

        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Sesión inválida o expirada", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.cancelEnrollment(inscripcionId, usuarioId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@DetalleCursoActivity,
                            "✅ Inscripción cancelada con éxito",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        android.util.Log.e("API_ERROR", "Respuesta del servidor: $errorBody")
                        Toast.makeText(
                            this@DetalleCursoActivity,
                            "❌ Error: ${response.code()} - $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleCursoActivity,
                        "❌ Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}