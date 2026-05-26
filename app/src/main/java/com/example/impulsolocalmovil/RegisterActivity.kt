package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            when {
                name.isEmpty() -> etName.error = "Ingresa tu nombre"
                email.isEmpty() -> etEmail.error = "Ingresa tu correo"
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> etEmail.error = "Correo inválido"
                password.isEmpty() -> etPassword.error = "Ingresa una contraseña"
                password.length < 6 -> etPassword.error = "Mínimo 6 caracteres"
                confirmPassword.isEmpty() -> etConfirmPassword.error = "Confirma tu contraseña"
                password != confirmPassword -> etConfirmPassword.error = "Las contraseñas no coinciden"
                else -> {
                    val sharedPref = getSharedPreferences("impulso_local_prefs", MODE_PRIVATE)
                    sharedPref.edit().apply {
                        putString("user_name", name)
                        putString("user_email", email)
                        putString("user_password", password)
                        apply()
                    }
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}