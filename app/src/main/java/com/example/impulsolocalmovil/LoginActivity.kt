package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impulsolocalmovil.models.User
import com.example.impulsolocalmovil.utils.SharedPrefManager

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgotPassword: TextView
    private lateinit var btnGoogle: Button
    private lateinit var btnFacebook: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar views con findViewById
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnFacebook = findViewById(R.id.btnFacebook)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            when {
                email.isEmpty() -> {
                    etEmail.error = "Ingresa tu correo electrónico"
                }
                password.isEmpty() -> {
                    etPassword.error = "Ingresa tu contraseña"
                }
                else -> {
                    performLogin(email, password)
                }
            }
        }

        tvRegister.setOnClickListener {
            Toast.makeText(this, "Próximamente: Pantalla de registro", Toast.LENGTH_SHORT).show()
        }

        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Próximamente: Recuperar contraseña", Toast.LENGTH_SHORT).show()
        }

        btnGoogle.setOnClickListener {
            Toast.makeText(this, "Login con Google - Próximamente", Toast.LENGTH_SHORT).show()
        }

        btnFacebook.setOnClickListener {
            Toast.makeText(this, "Login con Facebook - Próximamente", Toast.LENGTH_SHORT).show()
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin(email: String, password: String) {
        val sharedPref = getSharedPreferences("impulso_local_prefs", MODE_PRIVATE)

        // Para pruebas: credenciales admin
        if (email == "admin@test.com" && password == "123456") {
            sharedPref.edit().apply {
                putString("user_email", email)
                putString("user_name", "Administrador")
                putBoolean("is_admin", true)
                apply()
            }
            Toast.makeText(this, "Login Admin exitoso", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        // Credenciales normal
        else if (email == "test@test.com" && password == "123456") {
            sharedPref.edit().apply {
                putString("user_email", email)
                putString("user_name", "Usuario Test")
                putBoolean("is_admin", false)
                apply()
            }
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }
}