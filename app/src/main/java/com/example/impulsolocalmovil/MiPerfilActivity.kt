package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.EmprendimientoAdapter
import com.example.impulsolocalmovil.models.Emprendimiento

class MiPerfilActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvNombrePerfil: TextView
    private lateinit var tvEmailPerfil: TextView
    private lateinit var tvTotalEmprendimientos: TextView
    private lateinit var tvTotalMeEncanta: TextView
    private lateinit var tvTotalVentas: TextView
    private lateinit var rvMisEmprendimientos: RecyclerView
    private lateinit var btnEditarPerfil: Button
    private lateinit var btnCambiarPassword: Button
    private lateinit var btnNuevoEmprendimiento: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_perfil)

        toolbar = findViewById(R.id.toolbar)
        tvNombrePerfil = findViewById(R.id.tvNombrePerfil)
        tvEmailPerfil = findViewById(R.id.tvEmailPerfil)
        tvTotalEmprendimientos = findViewById(R.id.tvTotalEmprendimientos)
        tvTotalMeEncanta = findViewById(R.id.tvTotalMeEncanta)
        tvTotalVentas = findViewById(R.id.tvTotalVentas)
        rvMisEmprendimientos = findViewById(R.id.rvMisEmprendimientos)
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil)
        btnCambiarPassword = findViewById(R.id.btnCambiarPassword)
        btnNuevoEmprendimiento = findViewById(R.id.btnNuevoEmprendimiento)

        setupToolbar()
        cargarDatosUsuario()
        cargarMisEmprendimientos()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        title = "Mi Perfil"
    }

    private fun cargarDatosUsuario() {
        val sharedPref = getSharedPreferences("impulso_local_prefs", MODE_PRIVATE)
        val userName = sharedPref.getString("user_name", "Usuario Demo")
        val userEmail = sharedPref.getString("user_email", "usuario@demo.com")

        tvNombrePerfil.text = userName
        tvEmailPerfil.text = userEmail
        tvTotalEmprendimientos.text = "3"
        tvTotalMeEncanta.text = "12"
        tvTotalVentas.text = "5"
    }

    private fun cargarMisEmprendimientos() {
        val misEmprendimientos: List<Emprendimiento> = listOf(
            Emprendimiento(1, "GreenTech", "Tecnología", "Soluciones sostenibles", 4.8, "Bogotá", "destacado", ""),
            Emprendimiento(2, "EduSmart", "Educación", "Plataforma educativa", 4.9, "Medellín", "nuevo", "")
        )

        val adapter = EmprendimientoAdapter(misEmprendimientos) { emprendimiento ->
            val intent = Intent(this, DetalleEmprendimientoActivity::class.java)
            intent.putExtra("emprendimiento", emprendimiento)
            startActivity(intent)
        }

        rvMisEmprendimientos.layoutManager = LinearLayoutManager(this)
        rvMisEmprendimientos.adapter = adapter
    }

    private fun setupClickListeners() {
        btnEditarPerfil.setOnClickListener {
            Toast.makeText(this, "Editar Perfil - Próximamente", Toast.LENGTH_SHORT).show()
        }

        btnCambiarPassword.setOnClickListener {
            Toast.makeText(this, "Cambiar Contraseña - Próximamente", Toast.LENGTH_SHORT).show()
        }

        btnNuevoEmprendimiento.setOnClickListener {
            startActivity(Intent(this, PublicarEmprendimientoActivity::class.java))
        }
    }
}