package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Usuario
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvUsername: TextView
    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var etCiudad: EditText
    private lateinit var etSitioWeb: EditText
    private lateinit var etBio: EditText
    private lateinit var etDireccion: EditText
    private lateinit var switchNotificaciones: SwitchCompat
    private lateinit var switchNotificacionesEmail: SwitchCompat
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    private lateinit var tokenManager: TokenManager
    private var usuario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario)
        tvUsername = findViewById(R.id.tvUsername)
        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etTelefono = findViewById(R.id.etTelefono)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        etCiudad = findViewById(R.id.etCiudad)
        etSitioWeb = findViewById(R.id.etSitioWeb)
        etBio = findViewById(R.id.etBio)
        etDireccion = findViewById(R.id.etDireccion)
        switchNotificaciones = findViewById(R.id.switchNotificaciones)
        switchNotificacionesEmail = findViewById(R.id.switchNotificacionesEmail)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        setupToolbar()
        cargarDatosUsuario()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Editar Perfil"
    }

    private fun cargarDatosUsuario() {
        usuario = tokenManager.getUser()
        usuario?.let {
            tvNombreUsuario.text = it.name
            tvUsername.text = "@${it.username ?: it.name.lowercase().replace(" ", "")}"
            etNombre.setText(it.name)
            etEmail.setText(it.email)
            etTelefono.setText(it.telefono ?: "")
            etCiudad.setText(it.ciudad ?: "")
            etSitioWeb.setText(it.sitioWeb ?: "")
            etBio.setText(it.bio ?: "")
            etDireccion.setText(it.direccion ?: "")
            etFechaNacimiento.setText(it.fechaNacimiento ?: "")
            switchNotificaciones.isChecked = it.recibeNotificaciones ?: true
            switchNotificacionesEmail.isChecked = it.notificacionesEmail ?: true
        }
    }

    private fun setupClickListeners() {
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()

            when {
                nombre.isEmpty() -> etNombre.error = "Ingresa tu nombre"
                email.isEmpty() -> etEmail.error = "Ingresa tu email"
                else -> {
                    guardarCambios()
                }
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarCambios() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        val fechaNacimientoInput = etFechaNacimiento.text.toString().trim()
        var fechaNacimientoFormateada: String? = null

        // 1. Validar y formatear la fecha de DD/MM/YYYY a YYYY-MM-DD
        if (fechaNacimientoInput.isNotEmpty()) {
            val partes = fechaNacimientoInput.split("/")
            if (partes.size == 3) {
                val dia = partes[0].padStart(2, '0')
                val mes = partes[1].padStart(2, '0')
                val anio = partes[2]
                fechaNacimientoFormateada = "$anio-$mes-$dia"
            } else {
                etFechaNacimiento.error = "Usa el formato DD/MM/YYYY"
                return
            }
        }

        // 2. Convertir booleanos a "1" o "0" para el validador del backend
        val recibeNotifVal = if (switchNotificaciones.isChecked) "1" else "0"
        val recibeEmailVal = if (switchNotificacionesEmail.isChecked) "1" else "0"

        // 3. Crear el mapa sin enviar el apellido y con tipos puramente de String
        val campos = mutableMapOf<String, String>(
            "name" to etNombre.text.toString().trim(),
            "full_name" to etNombre.text.toString().trim(),
            "email" to etEmail.text.toString().trim(),
            "telefono" to etTelefono.text.toString().trim(),
            "ciudad" to etCiudad.text.toString().trim(),
            "sitio_web" to etSitioWeb.text.toString().trim(),
            "bio" to etBio.text.toString().trim(),
            "direccion" to etDireccion.text.toString().trim(),
            "recibe_notificaciones" to recibeNotifVal,
            "notificaciones_email" to recibeEmailVal
        )

        // Agregamos la fecha al mapa únicamente si tiene un formato válido
        if (fechaNacimientoFormateada != null) {
            campos["fecha_nacimiento"] = fechaNacimientoFormateada
        }

        android.util.Log.d("EditarPerfil", "Campos enviados: $campos")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.updateProfile(usuarioId, campos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val usuarioActual = tokenManager.getUser()
                        val usuarioActualizado = usuarioActual?.copy(
                            name = campos["name"] ?: "",
                            email = campos["email"] ?: "",
                            telefono = campos["telefono"],
                            ciudad = campos["ciudad"],
                            sitioWeb = campos["sitio_web"],
                            bio = campos["bio"],
                            direccion = campos["direccion"],
                            fechaNacimiento = fechaNacimientoInput,
                            recibeNotificaciones = switchNotificaciones.isChecked,
                            notificacionesEmail = switchNotificacionesEmail.isChecked
                        )
                        usuarioActualizado?.let { tokenManager.saveUser(it) }

                        Toast.makeText(
                            this@EditarPerfilActivity,
                            "✅ Perfil actualizado con éxito",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        android.util.Log.e("EditarPerfil", "Error: $errorBody")
                        Toast.makeText(
                            this@EditarPerfilActivity,
                            "❌ Error: $errorBody",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.util.Log.e("EditarPerfil", "Excepción: ${e.message}")
                    Toast.makeText(
                        this@EditarPerfilActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}