package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar

class AjustesActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var switchNotificaciones: SwitchCompat
    private lateinit var switchNotificacionesEmail: SwitchCompat
    private lateinit var switchPerfilPublico: SwitchCompat
    private lateinit var switchMostrarEmail: SwitchCompat
    private lateinit var spinnerTema: Spinner
    private lateinit var spinnerIdioma: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnRestablecer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajustes_generales)

        toolbar = findViewById(R.id.toolbar)
        switchNotificaciones = findViewById(R.id.switchNotificaciones)
        switchNotificacionesEmail = findViewById(R.id.switchNotificacionesEmail)
        switchPerfilPublico = findViewById(R.id.switchPerfilPublico)
        switchMostrarEmail = findViewById(R.id.switchMostrarEmail)
        spinnerTema = findViewById(R.id.spinnerTema)
        spinnerIdioma = findViewById(R.id.spinnerIdioma)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnRestablecer = findViewById(R.id.btnRestablecer)

        setupToolbar()
        cargarPreferencias()
        setupClickListeners()
        setupSpinners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Ajustes"
    }

    private fun setupSpinners() {
        val temas = arrayOf("Claro", "Oscuro", "Automático")
        val temaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, temas)
        temaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTema.adapter = temaAdapter

        val idiomas = arrayOf("Español", "Inglés", "Portugués")
        val idiomaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, idiomas)
        idiomaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIdioma.adapter = idiomaAdapter
    }

    private fun cargarPreferencias() {
        val sharedPref = getSharedPreferences("ajustes_prefs", MODE_PRIVATE)
        switchNotificaciones.isChecked = sharedPref.getBoolean("notificaciones", true)
        switchNotificacionesEmail.isChecked = sharedPref.getBoolean("notificaciones_email", true)
        switchPerfilPublico.isChecked = sharedPref.getBoolean("perfil_publico", true)
        switchMostrarEmail.isChecked = sharedPref.getBoolean("mostrar_email", false)

        spinnerTema.setSelection(sharedPref.getInt("tema", 0))
        spinnerIdioma.setSelection(sharedPref.getInt("idioma", 0))
    }

    private fun setupClickListeners() {
        btnGuardar.setOnClickListener {
            val sharedPref = getSharedPreferences("ajustes_prefs", MODE_PRIVATE)
            sharedPref.edit().apply {
                putBoolean("notificaciones", switchNotificaciones.isChecked)
                putBoolean("notificaciones_email", switchNotificacionesEmail.isChecked)
                putBoolean("perfil_publico", switchPerfilPublico.isChecked)
                putBoolean("mostrar_email", switchMostrarEmail.isChecked)
                putInt("tema", spinnerTema.selectedItemPosition)
                putInt("idioma", spinnerIdioma.selectedItemPosition)
                apply()
            }
            Toast.makeText(this, "✅ Ajustes guardados", Toast.LENGTH_SHORT).show()
        }

        btnRestablecer.setOnClickListener {
            switchNotificaciones.isChecked = true
            switchNotificacionesEmail.isChecked = true
            switchPerfilPublico.isChecked = true
            switchMostrarEmail.isChecked = false
            spinnerTema.setSelection(0)
            spinnerIdioma.setSelection(0)
            Toast.makeText(this, "Ajustes restablecidos", Toast.LENGTH_SHORT).show()
        }
    }
}