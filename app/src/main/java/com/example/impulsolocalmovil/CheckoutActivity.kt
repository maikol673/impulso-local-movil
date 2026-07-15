package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.CarritoItem
import com.example.impulsolocalmovil.models.OrdenItemRequest
import com.example.impulsolocalmovil.models.OrdenRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class CheckoutActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvTotal: TextView
    private lateinit var etDireccion: EditText
    private lateinit var etCiudad: EditText
    private lateinit var etCodigoPostal: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etNotas: EditText
    private lateinit var btnConfirmar: Button
    private lateinit var btnVolver: Button

    private lateinit var tokenManager: TokenManager
    private var total: Double = 0.0
    private var carritoItems: List<CarritoItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        tvTotal = findViewById(R.id.tvTotal)
        etDireccion = findViewById(R.id.etDireccion)
        etCiudad = findViewById(R.id.etCiudad)
        etCodigoPostal = findViewById(R.id.etCodigoPostal)
        etTelefono = findViewById(R.id.etTelefono)
        etNotas = findViewById(R.id.etNotas)
        btnConfirmar = findViewById(R.id.btnConfirmar)
        btnVolver = findViewById(R.id.btnVolver)

        setupToolbar()
        cargarResumen()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Checkout"
    }

    // ✅ Ahora carga el carrito directamente desde la API
    private fun cargarResumen() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getCart(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val carrito = response.body()
                        if (carrito != null && carrito.items.isNotEmpty()) {
                            carritoItems = carrito.items
                            total = carritoItems.sumOf { item ->
                                (item.precio.toDoubleOrNull() ?: 0.0) * item.cantidad
                            }
                            tvTotal.text = "$ ${"%.2f".format(total)}"
                        } else {
                            carritoItems = emptyList()
                            total = 0.0
                            tvTotal.text = "$ 0.00"
                        }
                    } else {
                        Toast.makeText(
                            this@CheckoutActivity,
                            "Error al cargar el carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CheckoutActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnConfirmar.setOnClickListener {
            val direccion = etDireccion.text.toString().trim()
            val ciudad = etCiudad.text.toString().trim()
            val codigoPostal = etCodigoPostal.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val notas = etNotas.text.toString().trim()

            when {
                direccion.isEmpty() -> etDireccion.error = "Ingresa tu dirección"
                ciudad.isEmpty() -> etCiudad.error = "Ingresa tu ciudad"
                codigoPostal.isEmpty() -> etCodigoPostal.error = "Ingresa el código postal"
                telefono.isEmpty() -> etTelefono.error = "Ingresa tu teléfono"
                carritoItems.isEmpty() -> {
                    Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    confirmarCompra(direccion, ciudad, codigoPostal, telefono, notas)
                }
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun confirmarCompra(
        direccion: String,
        ciudad: String,
        codigoPostal: String,
        telefono: String,
        notas: String
    ) {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Convertir CarritoItem (modelo real) a OrdenItemRequest
        val items = carritoItems.map { item ->
            OrdenItemRequest(
                productoId = item.productoId,
                cantidad = item.cantidad,
                precio = item.precio.toDoubleOrNull() ?: 0.0
            )
        }

        val request = OrdenRequest(
            usuarioId = usuarioId,
            direccionEnvio = direccion,
            ciudad = ciudad,
            codigoPostal = codigoPostal,
            telefonoContacto = telefono,
            notas = if (notas.isNotEmpty()) notas else null,
            items = items
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.createOrder(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CheckoutActivity,
                            "✅ Compra realizada con éxito",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(this@CheckoutActivity, MisOrdenesActivity::class.java))
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@CheckoutActivity,
                            "❌ Error: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CheckoutActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}