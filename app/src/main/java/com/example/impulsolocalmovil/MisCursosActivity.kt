package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.CursoAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Curso
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class MisCursosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvMisCursos: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var btnExplorar: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: CursoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_cursos)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        rvMisCursos = findViewById(R.id.rvMisCursos)
        layoutVacio = findViewById(R.id.layoutVacio)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        setupRecyclerView()
        cargarMisCursos()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Cursos"
    }

    private fun setupRecyclerView() {
        adapter = CursoAdapter(emptyList()) { inscripcion ->
            val curso = inscripcion.curso
            if (curso != null) {
                val intent = Intent(this, DetalleCursoActivity::class.java)
                intent.putExtra("curso_id", curso.id)
                intent.putExtra("inscripcion_id", inscripcion.id)
                startActivity(intent)
            }
        }
        rvMisCursos.layoutManager = LinearLayoutManager(this)
        rvMisCursos.adapter = adapter
    }

    private fun cargarMisCursos() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMyCourses(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val inscripciones = response.body() ?: emptyList()

                        if (inscripciones.isEmpty()) {
                            rvMisCursos.visibility = android.view.View.GONE
                            layoutVacio.visibility = android.view.View.VISIBLE
                        } else {
                            rvMisCursos.visibility = android.view.View.VISIBLE
                            layoutVacio.visibility = android.view.View.GONE
                            adapter.updateList(inscripciones)
                        }
                    } else {
                        Toast.makeText(
                            this@MisCursosActivity,
                            "Error al cargar cursos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MisCursosActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            startActivity(Intent(this, FormacionActivity::class.java))
        }
    }
}