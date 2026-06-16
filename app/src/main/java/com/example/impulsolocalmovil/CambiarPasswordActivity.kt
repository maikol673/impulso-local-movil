package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class CambiarPasswordActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var etContrasenaActual: EditText
    private lateinit var etNuevaContrasena: EditText
    private lateinit var etConfirmarContrasena: EditText
    private lateinit var btnCambiar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_password)

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
                actual.isEmpty() -> {
                    etContrasenaActual.error = "Ingresa tu contraseña actual"
                }
                nueva.isEmpty() -> {
                    etNuevaContrasena.error = "Ingresa una nueva contraseña"
                }
                nueva.length < 8 -> {
                    etNuevaContrasena.error = "La contraseña debe tener mínimo 8 caracteres"
                }
                confirmar.isEmpty() -> {
                    etConfirmarContrasena.error = "Confirma tu nueva contraseña"
                }
                nueva != confirmar -> {
                    etConfirmarContrasena.error = "Las contraseñas no coinciden"
                }
                actual == nueva -> {
                    etNuevaContrasena.error = "La nueva contraseña debe ser diferente a la actual"
                }
                else -> {
                    // Aquí se guardaría en la base de datos
                    Toast.makeText(this, "✅ Contraseña actualizada correctamente", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}