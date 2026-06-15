package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

class ChatConversacionActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvMensajes: RecyclerView
    private lateinit var layoutSinMensajes: LinearLayout
    private lateinit var etMensaje: EditText
    private lateinit var btnEnviar: ImageButton  // Cambiado de Button a ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_conversacion)

        toolbar = findViewById(R.id.toolbar)
        rvMensajes = findViewById(R.id.rvMensajes)
        layoutSinMensajes = findViewById(R.id.layoutSinMensajes)
        etMensaje = findViewById(R.id.etMensaje)
        btnEnviar = findViewById(R.id.btnEnviar)  // ImageButton

        val nombre = intent.getStringExtra("usuario_nombre") ?: "Usuario"
        val username = intent.getStringExtra("usuario_username") ?: "usuario"

        setupToolbar(nombre, username)
        setupClickListeners()
    }

    private fun setupToolbar(nombre: String, username: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = nombre
    }

    private fun setupClickListeners() {
        btnEnviar.setOnClickListener {
            val mensaje = etMensaje.text.toString().trim()
            if (mensaje.isNotEmpty()) {
                Toast.makeText(this, "Mensaje enviado: $mensaje", Toast.LENGTH_SHORT).show()
                etMensaje.text.clear()
            }
        }
    }
}