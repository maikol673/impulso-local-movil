package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.impulsolocalmovil.adapters.ProductoAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.AddToCartRequest
import com.example.impulsolocalmovil.models.Emprendimiento
import com.example.impulsolocalmovil.models.Producto
import com.example.impulsolocalmovil.models.ToggleLikeRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class DetalleEmprendimientoActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "http://172.20.10.6:8000"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var ivImagen: ImageView
    private lateinit var tvDestacado: TextView
    private lateinit var tvNombre: TextView
    private lateinit var tvCategoria: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvNumResenas: TextView
    private lateinit var tvUbicacion: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var tvSitioWeb: TextView
    private lateinit var btnDejarResena: Button
    private lateinit var btnMeGusta: Button
    private lateinit var btnContactar: Button
    private lateinit var btnSeguir: Button
    private lateinit var layoutEdicion: LinearLayout
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnAgregarProducto: Button
    private lateinit var rvProductos: RecyclerView

    private lateinit var tokenManager: TokenManager
    private var emprendimientoId: Int = 0
    private var emprendimiento: Emprendimiento? = null
    private lateinit var productoAdapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_emprendimiento)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        ivImagen = findViewById(R.id.ivImagen)
        tvDestacado = findViewById(R.id.tvDestacado)
        tvNombre = findViewById(R.id.tvNombre)
        tvCategoria = findViewById(R.id.tvCategoria)
        tvRating = findViewById(R.id.tvRating)
        tvNumResenas = findViewById(R.id.tvNumResenas)
        tvUbicacion = findViewById(R.id.tvUbicacion)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvEmail = findViewById(R.id.tvEmail)
        tvTelefono = findViewById(R.id.tvTelefono)
        tvSitioWeb = findViewById(R.id.tvSitioWeb)
        btnDejarResena = findViewById(R.id.btnDejarResena)
        btnMeGusta = findViewById(R.id.btnMeGusta)
        btnContactar = findViewById(R.id.btnContactar)
        btnSeguir = findViewById(R.id.btnSeguir)
        layoutEdicion = findViewById(R.id.layoutEdicion)
        btnEditar = findViewById(R.id.btnEditar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto)
        rvProductos = findViewById(R.id.rvProductos)

        setupToolbar()

        emprendimientoId = intent.getIntExtra("emprendimiento_id", 0)
        if (emprendimientoId > 0) {
            cargarDetalle()
            cargarProductos()
        }

        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = ""
    }

    private fun cargarDetalle() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getVentureById(emprendimientoId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        emprendimiento = response.body()
                        if (emprendimiento != null) {
                            mostrarDatos(emprendimiento!!)
                            verificarMeGusta()
                        }
                    } else {
                        Toast.makeText(
                            this@DetalleEmprendimientoActivity,
                            "Error al cargar detalle",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleEmprendimientoActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun cargarProductos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getProductsByVenture(emprendimientoId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val productos = response.body() ?: emptyList()
                        if (productos.isNotEmpty()) {
                            productoAdapter = ProductoAdapter(
                                productos,
                                onAgregarCarrito = { producto ->
                                    agregarAlCarrito(producto)
                                },
                                onProductoEliminado = {
                                    cargarProductos()
                                }
                            )
                            rvProductos.layoutManager = LinearLayoutManager(this@DetalleEmprendimientoActivity)
                            rvProductos.adapter = productoAdapter
                        }
                    }
                }
            } catch (e: Exception) {
                // Error silencioso
            }
        }
    }

    private fun agregarAlCarrito(producto: Producto) {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Inicia sesión para agregar al carrito", Toast.LENGTH_SHORT).show()
            return
        }

        val request = AddToCartRequest(producto.id, usuarioId, 1)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.addToCart(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@DetalleEmprendimientoActivity,
                            "✅ Producto agregado al carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@DetalleEmprendimientoActivity,
                            "❌ Error al agregar al carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleEmprendimientoActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun mostrarDatos(emprendimiento: Emprendimiento) {
        tvNombre.text = emprendimiento.nombre
        tvCategoria.text = emprendimiento.categoria?.nombre ?: "Sin categoría"
        tvRating.text = "⭐ ${emprendimiento.calificacion ?: "4.5"}"
        tvNumResenas.text = "(${emprendimiento.numResenas ?: 0} reseñas)"
        tvUbicacion.text = "📍 ${emprendimiento.ubicacion ?: "Ubicación no especificada"}"
        tvDescripcion.text = emprendimiento.descripcion

        // ✅ Cargar imagen con la estructura corregida
        val imagenUrl = if (!emprendimiento.imagen.isNullOrEmpty()) {
            val rutaLimpia = if (emprendimiento.imagen.startsWith("/")) emprendimiento.imagen else "/${emprendimiento.imagen}"
            BASE_URL + rutaLimpia
        } else {
            null
        }

        Glide.with(this)
            .load(imagenUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .centerCrop()
            .into(ivImagen)

        when (emprendimiento.estado) {
            "destacado" -> {
                tvDestacado.text = "Destacado"
                tvDestacado.visibility = android.view.View.VISIBLE
                tvDestacado.setBackgroundResource(R.drawable.bg_etiqueta_destacado)
            }
            "nuevo" -> {
                tvDestacado.text = "Nuevo"
                tvDestacado.visibility = android.view.View.VISIBLE
                tvDestacado.setBackgroundResource(R.drawable.bg_etiqueta_nuevo)
            }
            else -> {
                tvDestacado.visibility = android.view.View.GONE
            }
        }

        tvEmail.text = "✉ Email: contacto@${emprendimiento.nombre.lowercase()}.com"
        tvTelefono.text = "📱 Teléfono: +57 300 123 4567"
        tvSitioWeb.text = "🌐 Web: www.${emprendimiento.nombre.lowercase()}.com"
    }

    private fun verificarMeGusta() {
        val usuarioId = tokenManager.getUser()?.id ?: 0
        if (usuarioId == 0 || emprendimientoId == 0) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMyLikes(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val likes = response.body() ?: emptyList()
                        val existe = likes.any { it.emprendimientoId == emprendimientoId }
                        if (existe) {
                            btnMeGusta.text = "❤️ Me Gusta"
                            btnMeGusta.setBackgroundColor(getColor(android.R.color.holo_red_light))
                        } else {
                            btnMeGusta.text = "🤍 Me Gusta"
                            btnMeGusta.setBackgroundColor(getColor(R.color.gray_light))
                        }
                    }
                }
            } catch (e: Exception) {
                // Error silencioso
            }
        }
    }

    private fun setupClickListeners() {
        btnDejarResena.setOnClickListener {
            Toast.makeText(this, "Dejar reseña", Toast.LENGTH_SHORT).show()
        }

        btnAgregarProducto.setOnClickListener {
            val intent = Intent(this, AgregarProductoActivity::class.java)
            intent.putExtra("emprendimiento_nombre", tvNombre.text.toString())
            intent.putExtra("emprendimiento_id", emprendimientoId)
            startActivity(intent)
        }

        btnMeGusta.setOnClickListener {
            darMeGusta()
        }

        btnContactar.setOnClickListener {
            Toast.makeText(this, "📞 Contactar", Toast.LENGTH_SHORT).show()
        }

        btnSeguir.setOnClickListener {
            Toast.makeText(this, "👤 Siguiendo", Toast.LENGTH_SHORT).show()
        }

        btnEditar.setOnClickListener {
            Toast.makeText(this, "✏ Editar", Toast.LENGTH_SHORT).show()
        }

        btnEliminar.setOnClickListener {
            Toast.makeText(this, "🗑 Eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun darMeGusta() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Inicia sesión para dar me gusta", Toast.LENGTH_SHORT).show()
            return
        }

        if (emprendimientoId == 0) {
            Toast.makeText(this, "Error: emprendimiento no identificado", Toast.LENGTH_SHORT).show()
            return
        }

        val request = ToggleLikeRequest(emprendimientoId, usuarioId)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.toggleLike(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            val mensaje = if (body.liked) "❤️ Me gusta" else "💔 Quitado"
                            Toast.makeText(
                                this@DetalleEmprendimientoActivity,
                                "$mensaje (${body.totalLikes} likes)",
                                Toast.LENGTH_SHORT
                            ).show()

                            if (body.liked) {
                                btnMeGusta.text = "❤️ Me Gusta"
                                btnMeGusta.setBackgroundColor(getColor(android.R.color.holo_red_light))
                            } else {
                                btnMeGusta.text = "🤍 Me Gusta"
                                btnMeGusta.setBackgroundColor(getColor(R.color.gray_light))
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@DetalleEmprendimientoActivity,
                            "❌ Error al guardar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DetalleEmprendimientoActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}