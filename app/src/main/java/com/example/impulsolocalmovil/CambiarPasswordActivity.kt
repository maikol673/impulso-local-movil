package com.example.impulsolocalmovil

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.PasswordRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class CambiarPasswordActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var etContrasenaActual: EditText
    private lateinit var etNuevaContrasena: EditText
    private lateinit var etConfirmarContrasena: EditText
    private lateinit var btnCambiar: Button
    private lateinit var btnCancelar: Button

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_password)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        etContrasenaActual = findViewById(R.id.etContrasenaActual)
        etNuevaContrasena = findViewById(R.id.etNuevaContrasena)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena)
        btnCambiar = findViewById(R.id.btnCambiar)
        btnCancelar = findViewById(R.id.btnCancelar)

        setupToolbar()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Cambiar Contraseña"
    }

    private fun setupClickListeners() {
        btnCambiar.setOnClickListener {
            val actual = etContrasenaActual.text.toString().trim()
            val nueva = etNuevaContrasena.text.toString().trim()
            val confirmar = etConfirmarContrasena.text.toString().trim()

            when {
                actual.isEmpty() -> etContrasenaActual.error = "La contraseña actual es obligatoria"
                nueva.isEmpty() -> etNuevaContrasena.error = "La nueva contraseña es obligatoria"
                nueva.length < 6 -> etNuevaContrasena.error = "La contraseña debe tener al menos 6 caracteres"
                confirmar.isEmpty() -> etConfirmarContrasena.error = "Confirma tu nueva contraseña"
                nueva != confirmar -> etConfirmarContrasena.error = "Las contraseñas no coinciden"
                actual == nueva -> etNuevaContrasena.error = "La nueva debe ser diferente"
                else -> {
                    cambiarPassword(actual, nueva, confirmar)
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun cambiarPassword(
        currentPassword: String,
        newPassword: String,
        newPasswordConfirmation: String
    ) {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        // ✅ Usar Map en lugar de PasswordRequest
        val campos = mapOf(
            "current_password" to currentPassword,
            "new_password" to newPassword,
            "new_password_confirmation" to newPasswordConfirmation
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.changePassword(usuarioId, campos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CambiarPasswordActivity,
                            "✅ Contraseña actualizada",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@CambiarPasswordActivity,
                            "❌ Error: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CambiarPasswordActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}