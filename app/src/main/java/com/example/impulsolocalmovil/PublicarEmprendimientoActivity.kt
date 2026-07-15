package com.example.impulsolocalmovil

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class PublicarEmprendimientoActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "http://172.20.10.6:8000"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var spinnerCategoria: Spinner
    private lateinit var etUbicacion: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnPublicar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var tvNombreArchivo: TextView

    private var imagenUri: Uri? = null
    private var categoriasMap = mutableMapOf<String, Int>()
    private val SELECT_IMAGE_REQUEST = 1001
    private val PERMISSION_REQUEST = 1002

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicar_emprendimiento)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        etNombre = findViewById(R.id.etNombre)
        etDescripcion = findViewById(R.id.etDescripcion)
        spinnerCategoria = findViewById(R.id.spinnerCategoria)
        etUbicacion = findViewById(R.id.etUbicacion)
        spinnerEstado = findViewById(R.id.spinnerEstado)
        btnPublicar = findViewById(R.id.btnPublicar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        tvNombreArchivo = findViewById(R.id.tvNombreArchivo)

        setupToolbar()
        setupSpinners()
        setupClickListeners()
        verificarPermisos()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Publicar Emprendimiento"
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

    private fun setupSpinners() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getCategories()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val categorias = response.body() ?: emptyList()
                        categoriasMap.clear()
                        val nombres = categorias.map {
                            categoriasMap[it.nombre] = it.id
                            it.nombre
                        }
                        val adapter = ArrayAdapter<String>(
                            this@PublicarEmprendimientoActivity,
                            android.R.layout.simple_spinner_item,
                            nombres
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerCategoria.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val categorias = arrayOf("Tecnología", "Alimentario y bebidas", "Servicios", "Moda", "Artesanías")
                    val adapter = ArrayAdapter<String>(
                        this@PublicarEmprendimientoActivity,
                        android.R.layout.simple_spinner_item,
                        categorias
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCategoria.adapter = adapter
                }
            }
        }

        val estados = arrayOf("activo", "destacado", "nuevo")
        val estadoAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            estados
        )
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = estadoAdapter
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

        btnPublicar.setOnClickListener {
            btnPublicar.isEnabled = false

            android.util.Log.d("Publicar", "🔘 Botón Publicar presionado")
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val ubicacion = etUbicacion.text.toString().trim()
            val categoriaSeleccionada = spinnerCategoria.selectedItem.toString()
            val estado = spinnerEstado.selectedItem.toString()

            android.util.Log.d("Publicar", "Valores: nombre='$nombre', descripcion='$descripcion', ubicacion='$ubicacion'")

            when {
                nombre.isEmpty() -> {
                    android.util.Log.d("Publicar", "⚠️ Nombre vacío")
                    etNombre.error = "Ingresa el nombre"
                    btnPublicar.isEnabled = true
                }
                descripcion.isEmpty() -> {
                    android.util.Log.d("Publicar", "⚠️ Descripción vacía")
                    etDescripcion.error = "Ingresa la descripción"
                    btnPublicar.isEnabled = true
                }
                ubicacion.isEmpty() -> {
                    android.util.Log.d("Publicar", "⚠️ Ubicación vacía")
                    etUbicacion.error = "Ingresa la ubicación"
                    btnPublicar.isEnabled = true
                }
                else -> {
                    android.util.Log.d("Publicar", "✅ Validación OK, publicando...")
                    publicarEmprendimientoConImagen(nombre, descripcion, ubicacion, categoriaSeleccionada, estado)
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

    private fun publicarEmprendimientoConImagen(
        nombre: String,
        descripcion: String,
        ubicacion: String,
        categoriaNombre: String,
        estado: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categoriaId = categoriasMap[categoriaNombre] ?: 1
                val usuarioId = tokenManager.getUser()?.id ?: 1

                android.util.Log.d("Publicar", "📤 Enviando: nombre=$nombre, categoriaId=$categoriaId, usuarioId=$usuarioId")

                var imagePart: MultipartBody.Part? = null
                if (imagenUri != null) {
                    val inputStream = contentResolver.openInputStream(imagenUri!!)
                    val bytes = inputStream?.readBytes() ?: ByteArray(0)
                    val requestBody = RequestBody.create(
                        "image/*".toMediaType(),
                        bytes
                    )
                    imagePart = MultipartBody.Part.createFormData(
                        "imagen",
                        getFileName(imagenUri),
                        requestBody
                    )
                    android.util.Log.d("Publicar", "📸 Imagen adjuntada")
                }

                val response = RetrofitClient.instance.createVentureWithImage(
                    nombre = nombre,
                    descripcion = descripcion,
                    categoriaId = categoriaId,
                    ubicacion = ubicacion,
                    usuarioId = usuarioId,
                    estado = estado,
                    image = imagePart
                )

                withContext(Dispatchers.Main) {
                    android.util.Log.d("Publicar", "📥 Código: ${response.code()}")
                    android.util.Log.d("Publicar", "📥 isSuccessful: ${response.isSuccessful}")

                    if (response.isSuccessful) {
                        val emprendimiento = response.body()
                        android.util.Log.d("Publicar", "✅ Creado: ${emprendimiento?.nombre}")
                        Toast.makeText(
                            this@PublicarEmprendimientoActivity,
                            "✅ Emprendimiento publicado: ${emprendimiento?.nombre}",
                            Toast.LENGTH_LONG
                        ).show()
                        delay(1000)
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        android.util.Log.e("Publicar", "❌ Error body: $errorBody")
                        Toast.makeText(
                            this@PublicarEmprendimientoActivity,
                            "❌ Error ${response.code()}: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                        btnPublicar.isEnabled = true
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.util.Log.e("Publicar", "❌ Excepción: ${e.message}")
                    Toast.makeText(
                        this@PublicarEmprendimientoActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnPublicar.isEnabled = true
                }
            }
        }
    }
}