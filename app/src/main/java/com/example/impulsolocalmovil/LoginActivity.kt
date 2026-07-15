package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.LoginRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    private val apiService = RetrofitClient.instance
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tokenManager = TokenManager.getInstance(this)

        if (tokenManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(email, password)
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = LoginRequest(email, password)
                val response = apiService.login(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.success == true && body.user != null) {
                            val user = body.user

                            // ✅ Guardar en TokenManager
                            tokenManager.saveUser(user)
                            tokenManager.saveToken(body.token ?: "temp_token")

                            // ✅ Guardar en SharedPreferences para MainActivity
                            val sharedPref = getSharedPreferences("impulso_local_prefs", MODE_PRIVATE)
                            sharedPref.edit().apply {
                                putString("user_email", user.email)
                                putString("user_name", user.name)
                                putBoolean("is_admin", user.isAdmin ?: false)
                                apply()
                            }

                            Toast.makeText(
                                this@LoginActivity,
                                "✅ Login exitoso",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }
}