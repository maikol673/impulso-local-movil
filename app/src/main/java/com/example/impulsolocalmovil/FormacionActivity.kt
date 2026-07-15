package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.EnrollRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class FormacionActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var btnInscribirse1: Button
    private lateinit var btnDetalle1: Button
    private lateinit var btnInscribirse2: Button
    private lateinit var btnDetalle2: Button
    private lateinit var btnInscribirse3: Button
    private lateinit var btnDetalle3: Button

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formacion)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        btnInscribirse1 = findViewById(R.id.btnInscribirse1)
        btnDetalle1 = findViewById(R.id.btnDetalle1)
        btnInscribirse2 = findViewById(R.id.btnInscribirse2)
        btnDetalle2 = findViewById(R.id.btnDetalle2)
        btnInscribirse3 = findViewById(R.id.btnInscribirse3)
        btnDetalle3 = findViewById(R.id.btnDetalle3)

        setupToolbar()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Formación"
    }

    private fun setupClickListeners() {
        btnInscribirse1.setOnClickListener {
            inscribirCurso(1, "Marketing Digital")
        }

        btnDetalle1.setOnClickListener {
            Toast.makeText(this, "ℹ️ Detalles de Marketing Digital", Toast.LENGTH_SHORT).show()
        }

        btnInscribirse2.setOnClickListener {
            inscribirCurso(2, "Escalabilidad Empresarial")
        }

        btnDetalle2.setOnClickListener {
            Toast.makeText(this, "ℹ️ Detalles de Escalabilidad Empresarial", Toast.LENGTH_SHORT).show()
        }

        btnInscribirse3.setOnClickListener {
            inscribirCurso(3, "Finanzas para Emprendedores")
        }

        btnDetalle3.setOnClickListener {
            Toast.makeText(this, "ℹ️ Detalles de Finanzas para Emprendedores", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inscribirCurso(cursoId: Int, cursoNombre: String) {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Inicia sesión para inscribirte", Toast.LENGTH_SHORT).show()
            return
        }

        btnInscribirse1.isEnabled = false
        btnInscribirse2.isEnabled = false
        btnInscribirse3.isEnabled = false

        val request = EnrollRequest(cursoId, usuarioId)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.enrollCourse(request)
                withContext(Dispatchers.Main) {
                    btnInscribirse1.isEnabled = true
                    btnInscribirse2.isEnabled = true
                    btnInscribirse3.isEnabled = true

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@FormacionActivity,
                            "✅ Inscrito en $cursoNombre",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@FormacionActivity,
                            "❌ Error: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    btnInscribirse1.isEnabled = true
                    btnInscribirse2.isEnabled = true
                    btnInscribirse3.isEnabled = true
                    Toast.makeText(
                        this@FormacionActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}