package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.AsistenciaRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class NetworkingActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var btnFacebook: Button
    private lateinit var btnInstagram: Button
    private lateinit var btnTwitter: Button
    private lateinit var btnEvento1: Button
    private lateinit var btnEvento2: Button
    private lateinit var btnEvento3: Button

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_networking)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        btnFacebook = findViewById(R.id.btnFacebook)
        btnInstagram = findViewById(R.id.btnInstagram)
        btnTwitter = findViewById(R.id.btnTwitter)
        btnEvento1 = findViewById(R.id.btnEvento1)
        btnEvento2 = findViewById(R.id.btnEvento2)
        btnEvento3 = findViewById(R.id.btnEvento3)

        setupToolbar()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Networking"
    }

    private fun setupClickListeners() {
        btnFacebook.setOnClickListener {
            Toast.makeText(this, "📘 Abriendo Facebook", Toast.LENGTH_SHORT).show()
        }

        btnInstagram.setOnClickListener {
            Toast.makeText(this, "📷 Abriendo Instagram", Toast.LENGTH_SHORT).show()
        }

        btnTwitter.setOnClickListener {
            Toast.makeText(this, "🐦 Abriendo Twitter", Toast.LENGTH_SHORT).show()
        }

        // ✅ Evento 1 - Ver Detalle
        btnEvento1.setOnClickListener {
            val intent = Intent(this, DetalleEventoActivity::class.java)
            intent.putExtra("evento_id", 1)
            startActivity(intent)
        }

        // ✅ Evento 2 - Ver Detalle
        btnEvento2.setOnClickListener {
            val intent = Intent(this, DetalleEventoActivity::class.java)
            intent.putExtra("evento_id", 2)
            startActivity(intent)
        }

        // ✅ Evento 3 - Ver Detalle
        btnEvento3.setOnClickListener {
            val intent = Intent(this, DetalleEventoActivity::class.java)
            intent.putExtra("evento_id", 3)
            startActivity(intent)
        }
    }
}