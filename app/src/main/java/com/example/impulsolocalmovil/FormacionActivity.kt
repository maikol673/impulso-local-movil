package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class FormacionActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var btnInscribirse1: Button
    private lateinit var btnDetalle1: Button
    private lateinit var btnInscribirse2: Button
    private lateinit var btnDetalle2: Button
    private lateinit var btnInscribirse3: Button
    private lateinit var btnDetalle3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formacion)

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
            Toast.makeText(this, "📝 Inscrito en Marketing Digital", Toast.LENGTH_LONG).show()
        }

        btnDetalle1.setOnClickListener {
            Toast.makeText(this, "ℹ️ Detalles de Marketing Digital", Toast.LENGTH_SHORT).show()
        }

        btnInscribirse2.setOnClickListener {
            Toast.makeText(this, "📝 Inscrito en Escalabilidad Empresarial", Toast.LENGTH_LONG).show()
        }

        btnDetalle2.setOnClickListener {
            Toast.makeText(this, "ℹ️ Detalles de Escalabilidad Empresarial", Toast.LENGTH_SHORT).show()
        }

        btnInscribirse3.setOnClickListener {
            Toast.makeText(this, "📝 Inscrito en Finanzas para Emprendedores", Toast.LENGTH_LONG).show()
        }

        btnDetalle3.setOnClickListener {
            Toast.makeText(this, "ℹ️ Detalles de Finanzas para Emprendedores", Toast.LENGTH_SHORT).show()
        }
    }
}