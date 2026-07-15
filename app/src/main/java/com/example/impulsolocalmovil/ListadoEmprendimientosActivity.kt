package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.EmprendimientoAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Categoria
import com.example.impulsolocalmovil.models.Emprendimiento
import kotlinx.coroutines.*

class ListadoEmprendimientosActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var etBuscar: EditText
    private lateinit var btnBuscar: Button
    private lateinit var rvEmprendimientos: RecyclerView
    private lateinit var chipTodos: TextView
    private lateinit var chipTecnologia: TextView
    private lateinit var chipAlimentos: TextView
    private lateinit var chipServicios: TextView
    private lateinit var chipModa: TextView
    private lateinit var chipArtesanias: TextView

    private lateinit var adapter: EmprendimientoAdapter
    private var listaCompleta = listOf<Emprendimiento>()
    private var categoriaActual = "Todos"
    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_emprendimientos)

        toolbar = findViewById(R.id.toolbar)
        etBuscar = findViewById(R.id.etBuscar)
        btnBuscar = findViewById(R.id.btnBuscar)
        rvEmprendimientos = findViewById(R.id.rvEmprendimientos)
        chipTodos = findViewById(R.id.chipTodos)
        chipTecnologia = findViewById(R.id.chipTecnologia)
        chipAlimentos = findViewById(R.id.chipAlimentos)
        chipServicios = findViewById(R.id.chipServicios)
        chipModa = findViewById(R.id.chipModa)
        chipArtesanias = findViewById(R.id.chipArtesanias)

        setupToolbar()
        setupRecyclerView()
        setupListeners()
        cargarEmprendimientos()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Explorar Emprendimientos"
    }

    private fun setupRecyclerView() {
        adapter = EmprendimientoAdapter(emptyList()) { emprendimiento ->
            val intent = Intent(this, DetalleEmprendimientoActivity::class.java)
            intent.putExtra("emprendimiento_id", emprendimiento.id)
            startActivity(intent)
        }
        rvEmprendimientos.layoutManager = LinearLayoutManager(this)
        rvEmprendimientos.adapter = adapter
    }

    private fun setupListeners() {
        btnBuscar.setOnClickListener {
            val query = etBuscar.text.toString().trim()
            if (query.isNotEmpty()) {
                buscarEmprendimientos(query)
            } else {
                cargarEmprendimientos()
            }
        }

        chipTodos.setOnClickListener { filtrarPorCategoria("Todos") }
        chipTecnologia.setOnClickListener { filtrarPorCategoria("Tecnología") }
        chipAlimentos.setOnClickListener { filtrarPorCategoria("Alimentario y bebidas") }
        chipServicios.setOnClickListener { filtrarPorCategoria("Servicios") }
        chipModa.setOnClickListener { filtrarPorCategoria("Moda") }
        chipArtesanias.setOnClickListener { filtrarPorCategoria("Artesanías") }
    }

    // ✅ Cargar emprendimientos desde API
    private fun cargarEmprendimientos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getVentures()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val emprendimientos = response.body() ?: emptyList()
                        listaCompleta = emprendimientos
                        filtrarPorCategoria("Todos")
                    } else {
                        Toast.makeText(
                            this@ListadoEmprendimientosActivity,
                            "Error al cargar emprendimientos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ListadoEmprendimientosActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun buscarEmprendimientos(query: String) {
        val filtrados = listaCompleta.filter {
            it.nombre.contains(query, ignoreCase = true) ||
                    it.descripcion.contains(query, ignoreCase = true)
        }
        adapter.updateList(filtrados)
    }

    private fun filtrarPorCategoria(categoria: String) {
        categoriaActual = categoria
        android.util.Log.d("Filtro", "Filtrando por: $categoria")

        val filtrados = if (categoria == "Todos") {
            listaCompleta
        } else {
            listaCompleta.filter {
                val nombre = it.categoria?.nombre
                android.util.Log.d("Filtro", "Categoría en BD: $nombre")
                nombre == categoria
            }
        }

        android.util.Log.d("Filtro", "Resultados: ${filtrados.size}")
        adapter.updateList(filtrados)
        actualizarEstiloChips()
    }

    private fun actualizarEstiloChips() {
        val chips = listOf(
            chipTodos to "Todos",
            chipTecnologia to "Tecnología",
            chipAlimentos to "Alimentos",
            chipServicios to "Servicios",
            chipModa to "Moda",
            chipArtesanias to "Artesanías"
        )

        chips.forEach { (chip, cat) ->
            if (cat == categoriaActual) {
                chip.background = getDrawable(R.drawable.bg_chip_selected)
                chip.setTextColor(getColor(android.R.color.white))
            } else {
                chip.background = getDrawable(R.drawable.bg_chip)
                chip.setTextColor(getColor(R.color.text_secondary))
            }
        }
    }
}