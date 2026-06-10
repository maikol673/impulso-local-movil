package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.EmprendimientoAdapter
import com.example.impulsolocalmovil.models.Emprendimiento

class MisEmprendimientosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvMisEmprendimientos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_emprendimientos)

        toolbar = findViewById(R.id.toolbar)
        rvMisEmprendimientos = findViewById(R.id.rvMisEmprendimientos)

        setupToolbar()
        cargarMisEmprendimientos()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Emprendimientos"
    }

    private fun cargarMisEmprendimientos() {
        val misEmprendimientos = listOf(
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
}