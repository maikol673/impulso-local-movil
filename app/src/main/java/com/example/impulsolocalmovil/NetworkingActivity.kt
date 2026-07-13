package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class NetworkingActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var btnFacebook: Button
    private lateinit var btnInstagram: Button
    private lateinit var btnTwitter: Button
    private lateinit var btnEvento1: Button
    private lateinit var btnEvento2: Button
    private lateinit var btnEvento3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_networking)

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

        btnEvento1.setOnClickListener {
            Toast.makeText(this, "✅ Asistencia confirmada a Networking Bogotá", Toast.LENGTH_LONG).show()
        }

        btnEvento2.setOnClickListener {
            Toast.makeText(this, "✅ Asistencia confirmada a Feria Medellín", Toast.LENGTH_LONG).show()
        }

        btnEvento3.setOnClickListener {
            Toast.makeText(this, "🎯 Unido al Webinar de Marketing Digital", Toast.LENGTH_LONG).show()
        }
    }
}