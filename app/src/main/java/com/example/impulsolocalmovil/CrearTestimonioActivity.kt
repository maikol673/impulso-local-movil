package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.TestimonioRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class CrearTestimonioActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var etNombre: EditText
    private lateinit var etEmpresa: EditText
    private lateinit var etContenido: EditText
    private lateinit var btnPublicar: Button
    private lateinit var btnVolver: Button

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_testimonio)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        etNombre = findViewById(R.id.etNombre)
        etEmpresa = findViewById(R.id.etEmpresa)
        etContenido = findViewById(R.id.etContenido)
        btnPublicar = findViewById(R.id.btnPublicar)
        btnVolver = findViewById(R.id.btnVolver)

        setupToolbar()

        // Cargar nombre del usuario logueado
        val user = tokenManager.getUser()
        if (user != null) {
            etNombre.setText(user.name)
        }

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
            // ✅ Deshabilitar botón para evitar doble clic
            btnPublicar.isEnabled = false
            btnPublicar.text = "Publicando..."

            val nombre = etNombre.text.toString().trim()
            val empresa = etEmpresa.text.toString().trim()
            val contenido = etContenido.text.toString().trim()

            when {
                nombre.isEmpty() -> {
                    etNombre.error = "Ingresa tu nombre"
                    btnPublicar.isEnabled = true
                    btnPublicar.text = "Publicar Testimonio"
                }
                contenido.isEmpty() -> {
                    etContenido.error = "Escribe tu testimonio"
                    btnPublicar.isEnabled = true
                    btnPublicar.text = "Publicar Testimonio"
                }
                else -> {
                    publicarTestimonio(nombre, contenido, empresa)
                }
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun publicarTestimonio(nombre: String, contenido: String, empresa: String) {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            btnPublicar.isEnabled = true
            btnPublicar.text = "Publicar Testimonio"
            return
        }

        val request = TestimonioRequest(
            usuarioId = usuarioId,
            nombre = nombre,
            contenido = contenido,
            empresa = if (empresa.isNotEmpty()) empresa else null
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.createTestimonial(request)
                withContext(Dispatchers.Main) {
                    btnPublicar.isEnabled = true
                    btnPublicar.text = "Publicar Testimonio"
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CrearTestimonioActivity,
                            "✅ Testimonio publicado. ¡Gracias!",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@CrearTestimonioActivity,
                            "❌ Error al publicar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    btnPublicar.isEnabled = true
                    btnPublicar.text = "Publicar Testimonio"
                    Toast.makeText(
                        this@CrearTestimonioActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}