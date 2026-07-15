package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.EmprendimientoAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Emprendimiento
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class MisEmprendimientosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvMisEmprendimientos: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var btnCrearEmprendimiento: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: EmprendimientoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_emprendimientos)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        rvMisEmprendimientos = findViewById(R.id.rvMisEmprendimientos)
        layoutVacio = findViewById(R.id.layoutVacio)
        btnCrearEmprendimiento = findViewById(R.id.btnCrearEmprendimiento)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        cargarMisEmprendimientos()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Emprendimientos"
    }

    private fun setupRecyclerView() {
        adapter = EmprendimientoAdapter(emptyList()) { emprendimiento ->
            val intent = Intent(this, DetalleEmprendimientoActivity::class.java)
            intent.putExtra("emprendimiento_id", emprendimiento.id)
            startActivity(intent)
        }
        rvMisEmprendimientos.layoutManager = LinearLayoutManager(this)
        rvMisEmprendimientos.adapter = adapter
    }

    private fun cargarMisEmprendimientos() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMyVentures(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val emprendimientos = response.body() ?: emptyList()
                        if (emprendimientos.isEmpty()) {
                            rvMisEmprendimientos.visibility = android.view.View.GONE
                            layoutVacio.visibility = android.view.View.VISIBLE
                        } else {
                            rvMisEmprendimientos.visibility = android.view.View.VISIBLE
                            layoutVacio.visibility = android.view.View.GONE
                            adapter.updateList(emprendimientos)
                        }
                    } else {
                        Toast.makeText(
                            this@MisEmprendimientosActivity,
                            "Error al cargar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MisEmprendimientosActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnCrearEmprendimiento.setOnClickListener {
            startActivity(Intent(this, PublicarEmprendimientoActivity::class.java))
        }
    }
}