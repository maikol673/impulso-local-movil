package com.example.impulsolocalmovil

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.MensajeAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.MarcarLeidosRequest
import com.example.impulsolocalmovil.models.Mensaje
import com.example.impulsolocalmovil.models.MensajeRequest
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class ChatConversacionActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvMensajes: RecyclerView
    private lateinit var layoutSinMensajes: LinearLayout
    private lateinit var etMensaje: EditText
    private lateinit var btnEnviar: ImageButton

    private lateinit var tokenManager: TokenManager
    private lateinit var mensajeAdapter: MensajeAdapter

    private var usuarioActualId: Int = 0
    private var receptorId: Int = 0
    private var conversacionId: Int = 0

    // ✅ Polling simple para simular actualización casi en tiempo real
    private val handler = Handler(Looper.getMainLooper())
    private val pollingRunnable = object : Runnable {
        override fun run() {
            if (conversacionId > 0) {
                cargarMensajes(mostrarLoading = false)
            }
            handler.postDelayed(this, 5000) // cada 5 segundos
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_conversacion)

        tokenManager = TokenManager.getInstance(this)
        usuarioActualId = tokenManager.getUser()?.id ?: 0

        toolbar = findViewById(R.id.toolbar)
        rvMensajes = findViewById(R.id.rvMensajes)
        layoutSinMensajes = findViewById(R.id.layoutSinMensajes)
        etMensaje = findViewById(R.id.etMensaje)
        btnEnviar = findViewById(R.id.btnEnviar)

        val nombre = intent.getStringExtra("usuario_nombre") ?: "Usuario"
        val username = intent.getStringExtra("usuario_username") ?: "usuario"
        receptorId = intent.getIntExtra("usuario_id", 0)
        conversacionId = intent.getIntExtra("conversacion_id", 0)

        setupToolbar(nombre, username)
        setupRecyclerView()
        setupClickListeners()

        if (usuarioActualId == 0) {
            Toast.makeText(this, "Inicia sesión para chatear", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (conversacionId > 0) {
            cargarMensajes(mostrarLoading = true)
            marcarComoLeidos()
        } else {
            mostrarEstadoVacio()
        }
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(pollingRunnable, 5000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(pollingRunnable)
    }

    private fun setupToolbar(nombre: String, username: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = nombre
    }

    private fun setupRecyclerView() {
        mensajeAdapter = MensajeAdapter(emptyList(), usuarioActualId)
        val layoutManager = LinearLayoutManager(this)
        rvMensajes.layoutManager = layoutManager
        rvMensajes.adapter = mensajeAdapter
    }

    private fun mostrarEstadoVacio() {
        rvMensajes.visibility = android.view.View.GONE
        layoutSinMensajes.visibility = android.view.View.VISIBLE
    }

    private fun mostrarMensajes() {
        rvMensajes.visibility = android.view.View.VISIBLE
        layoutSinMensajes.visibility = android.view.View.GONE
    }

    private fun cargarMensajes(mostrarLoading: Boolean) {
        if (conversacionId == 0) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMensajes(conversacionId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val mensajes = response.body() ?: emptyList()
                        if (mensajes.isNotEmpty()) {
                            mostrarMensajes()
                            mensajeAdapter.updateList(mensajes)
                            rvMensajes.scrollToPosition(mensajes.size - 1)
                        } else {
                            mostrarEstadoVacio()
                        }
                    } else if (mostrarLoading) {
                        Toast.makeText(
                            this@ChatConversacionActivity,
                            "Error al cargar mensajes",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                if (mostrarLoading) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ChatConversacionActivity,
                            "Error de conexión: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun marcarComoLeidos() {
        if (conversacionId == 0 || usuarioActualId == 0) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                RetrofitClient.instance.marcarComoLeidos(
                    conversacionId,
                    MarcarLeidosRequest(usuarioActualId)
                )
            } catch (e: Exception) {
                // Error silencioso, no es crítico
            }
        }
    }

    private fun setupClickListeners() {
        btnEnviar.setOnClickListener {
            val contenido = etMensaje.text.toString().trim()

            if (contenido.isEmpty()) {
                return@setOnClickListener
            }
            if (receptorId == 0) {
                Toast.makeText(this, "Error: destinatario no identificado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            enviarMensaje(contenido)
        }
    }

    private fun enviarMensaje(contenido: String) {
        btnEnviar.isEnabled = false

        val request = MensajeRequest(
            remitenteId = usuarioActualId,
            receptorId = receptorId,
            contenido = contenido
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.enviarMensaje(request)
                withContext(Dispatchers.Main) {
                    btnEnviar.isEnabled = true
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            conversacionId = body.conversacionId
                            etMensaje.text.clear()
                            cargarMensajes(mostrarLoading = false)
                        }
                    } else {
                        Toast.makeText(
                            this@ChatConversacionActivity,
                            "❌ Error al enviar mensaje",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    btnEnviar.isEnabled = true
                    Toast.makeText(
                        this@ChatConversacionActivity,
                        "❌ Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}