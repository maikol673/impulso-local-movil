package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.EmprendimientoAdapter
import com.example.impulsolocalmovil.models.Emprendimiento

class MisFavoritosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvFavoritos: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var btnExplorar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_favoritos)

        toolbar = findViewById(R.id.toolbar)
        rvFavoritos = findViewById(R.id.rvFavoritos)
        layoutVacio = findViewById(R.id.layoutVacio)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        cargarFavoritos()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Favoritos"
    }

    private fun cargarFavoritos() {
        // Datos de prueba
        val favoritos = listOf(
            Emprendimiento(1, "GreenTech", "Tecnología", "Soluciones sostenibles", 4.8, "Bogotá", "destacado", ""),
            Emprendimiento(2, "EduSmart", "Educación", "Plataforma educativa", 4.9, "Medellín", "nuevo", "")
        )

        if (favoritos.isEmpty()) {
            rvFavoritos.visibility = android.view.View.GONE
            layoutVacio.visibility = android.view.View.VISIBLE
        } else {
            rvFavoritos.visibility = android.view.View.VISIBLE
            layoutVacio.visibility = android.view.View.GONE

            val adapter = EmprendimientoAdapter(favoritos) { emprendimiento ->
                val intent = Intent(this, DetalleEmprendimientoActivity::class.java)
                intent.putExtra("emprendimiento", emprendimiento)
                startActivity(intent)
            }

            rvFavoritos.layoutManager = LinearLayoutManager(this)
            rvFavoritos.adapter = adapter
        }
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            startActivity(Intent(this, ListadoEmprendimientosActivity::class.java))
        }
    }
}