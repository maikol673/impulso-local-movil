package com.example.impulsolocalmovil

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.ProductoRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class AgregarProductoActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "http://172.20.10.6:8000"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var tvEmprendimiento: TextView
    private lateinit var etNombreProducto: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etStock: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var tvNombreArchivo: TextView
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    private lateinit var tokenManager: TokenManager
    private var emprendimientoId: Int = 0
    private var imagenUri: Uri? = null
    private val SELECT_IMAGE_REQUEST = 1001
    private val PERMISSION_REQUEST = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        tvEmprendimiento = findViewById(R.id.tvEmprendimiento)
        etNombreProducto = findViewById(R.id.etNombreProducto)
        etPrecio = findViewById(R.id.etPrecio)
        etDescripcion = findViewById(R.id.etDescripcion)
        etStock = findViewById(R.id.etStock)
        spinnerEstado = findViewById(R.id.spinnerEstado)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        tvNombreArchivo = findViewById(R.id.tvNombreArchivo)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        setupToolbar()
        setupSpinner()
        setupClickListeners()
        verificarPermisos()

        val nombreEmprendimiento = intent.getStringExtra("emprendimiento_nombre") ?: "Este emprendimiento"
        emprendimientoId = intent.getIntExtra("emprendimiento_id", 0)
        tvEmprendimiento.text = "Emprendimiento: $nombreEmprendimiento"
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Agregar Producto"
    }

    private fun setupSpinner() {
        val estados = arrayOf("activo", "inactivo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapter
    }

    private fun verificarPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    PERMISSION_REQUEST
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "✅ Permiso concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "❌ Permiso denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupClickListeners() {
        btnSeleccionarImagen.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                        PERMISSION_REQUEST
                    )
                    return@setOnClickListener
                }
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST
                    )
                    return@setOnClickListener
                }
            }
            seleccionarImagen()
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombreProducto.text.toString().trim()
            val precio = etPrecio.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val stock = etStock.text.toString().trim()
            val estado = spinnerEstado.selectedItem.toString()

            when {
                nombre.isEmpty() -> etNombreProducto.error = "Ingresa el nombre del producto"
                precio.isEmpty() -> etPrecio.error = "Ingresa el precio"
                descripcion.isEmpty() -> etDescripcion.error = "Ingresa la descripción"
                stock.isEmpty() -> etStock.error = "Ingresa el stock"
                else -> {
                    guardarProducto(nombre, descripcion, precio, stock, estado)
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imagenUri = data.data
            tvNombreArchivo.text = "✅ ${getFileName(imagenUri)}"
            tvNombreArchivo.setTextColor(getColor(android.R.color.holo_green_dark))
        }
    }

    private fun getFileName(uri: Uri?): String {
        if (uri == null) return "Ningún archivo seleccionado"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
            if (it.moveToFirst()) {
                return it.getString(nameIndex) ?: "archivo.jpg"
            }
        }
        return "archivo.jpg"
    }

    private fun guardarProducto(
        nombre: String,
        descripcion: String,
        precio: String,
        stock: String,
        estado: String
    ) {
        if (emprendimientoId == 0) {
            Toast.makeText(this, "Error: ID del emprendimiento no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val precioDouble = precio.toDoubleOrNull() ?: 0.0
        val stockInt = stock.toIntOrNull() ?: 0

        val request = ProductoRequest(
            nombre = nombre,
            descripcion = descripcion,
            precio = precioDouble,
            stock = stockInt,
            emprendimientoId = emprendimientoId,
            estado = estado
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.createProduct(request)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AgregarProductoActivity,
                            "✅ Producto '$nombre' agregado",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@AgregarProductoActivity,
                            "❌ Error: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AgregarProductoActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}