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

class MisFavoritosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvFavoritos: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var btnExplorar: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: EmprendimientoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_favoritos)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        rvFavoritos = findViewById(R.id.rvFavoritos)
        layoutVacio = findViewById(R.id.layoutVacio)
        btnExplorar = findViewById(R.id.btnExplorar)

        setupToolbar()
        setupRecyclerView()
        cargarFavoritos()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mis Favoritos"
    }

    private fun setupRecyclerView() {
        adapter = EmprendimientoAdapter(emptyList()) { emprendimiento ->
            val intent = Intent(this, DetalleEmprendimientoActivity::class.java)
            intent.putExtra("emprendimiento_id", emprendimiento.id)
            startActivity(intent)
        }
        rvFavoritos.layoutManager = LinearLayoutManager(this)
        rvFavoritos.adapter = adapter
    }

    private fun cargarFavoritos() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMyLikes(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val meEncantaList = response.body() ?: emptyList()
                        val emprendimientos = meEncantaList.mapNotNull { it.emprendimiento }

                        if (emprendimientos.isEmpty()) {
                            rvFavoritos.visibility = android.view.View.GONE
                            layoutVacio.visibility = android.view.View.VISIBLE
                        } else {
                            rvFavoritos.visibility = android.view.View.VISIBLE
                            layoutVacio.visibility = android.view.View.GONE
                            adapter.updateList(emprendimientos)
                        }
                    } else {
                        Toast.makeText(
                            this@MisFavoritosActivity,
                            "Error al cargar favoritos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MisFavoritosActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnExplorar.setOnClickListener {
            startActivity(Intent(this, ListadoEmprendimientosActivity::class.java))
        }
    }
}