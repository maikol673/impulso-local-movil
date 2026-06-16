package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvUsername: TextView
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etCiudad: EditText
    private lateinit var etSitioWeb: EditText
    private lateinit var etBio: EditText
    private lateinit var etDireccion: EditText
    private lateinit var switchNotificaciones: SwitchCompat
    private lateinit var switchNotificacionesEmail: SwitchCompat
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        toolbar = findViewById(R.id.toolbar)
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario)
        tvUsername = findViewById(R.id.tvUsername)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etEmail = findViewById(R.id.etEmail)
        etTelefono = findViewById(R.id.etTelefono)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etCiudad = findViewById(R.id.etCiudad)
        etSitioWeb = findViewById(R.id.etSitioWeb)
        etBio = findViewById(R.id.etBio)
        etDireccion = findViewById(R.id.etDireccion)
        switchNotificaciones = findViewById(R.id.switchNotificaciones)
        switchNotificacionesEmail = findViewById(R.id.switchNotificacionesEmail)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        setupToolbar()
        cargarDatosUsuario()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Editar Perfil"
    }

    private fun cargarDatosUsuario() {
        val sharedPref = getSharedPreferences("impulso_local_prefs", MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "Usuario Demo") ?: "Usuario Demo"
        val userEmail = sharedPref.getString("user_email", "usuario@demo.com") ?: "usuario@demo.com"

        tvNombreUsuario.text = userName
        tvUsername.text = "@${userName.lowercase().replace(" ", "")}"

        etNombre.setText(userName)
        etEmail.setText(userEmail)
        // Los demás campos se mantienen vacíos o con valores por defecto
    }

    private fun setupClickListeners() {
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()

            when {
                nombre.isEmpty() -> etNombre.error = "Ingresa tu nombre"
                email.isEmpty() -> etEmail.error = "Ingresa tu email"
                else -> {
                    Toast.makeText(this, "✅ Perfil actualizado correctamente", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}