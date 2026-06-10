package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.CursoAdapter
import com.example.impulsolocalmovil.models.Curso

class MisCursosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvMisCursos: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var btnExplorar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_cursos)

        toolbar = findViewById(R.id.toolbar)
        rvMisCursos = findViewById(R.id.rvMisCursos)
        layoutVacio = findViewById(R.id.layoutVacio)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        cargarCursos()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Cursos"
    }

    private fun cargarCursos() {
        val cursos = listOf(
            Curso(1, "Marketing Digital", "Aprende a vender en redes sociales", "Juan Pérez", "10 horas", "Principiante"),
            Curso(2, "Finanzas para Emprendedores", "Gestiona tus finanzas", "María Gómez", "8 horas", "Intermedio")
        )

        if (cursos.isEmpty()) {
            rvMisCursos.visibility = android.view.View.GONE
            layoutVacio.visibility = android.view.View.VISIBLE
        } else {
            rvMisCursos.visibility = android.view.View.VISIBLE
            layoutVacio.visibility = android.view.View.GONE

            val adapter = CursoAdapter(cursos) { curso ->
                // TODO: Navegar a detalle del curso
            }

            rvMisCursos.layoutManager = LinearLayoutManager(this)
            rvMisCursos.adapter = adapter
        }
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            // TODO: Navegar a explorar cursos
        }
    }
}