package com.example.impulsolocalmovil

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.ConversacionAdapter
import com.example.impulsolocalmovil.adapters.UsuarioAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Chat
import com.example.impulsolocalmovil.models.ConversacionResumen
import com.example.impulsolocalmovil.models.Usuario
import com.example.impulsolocalmovil.utils.TokenManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvConversaciones: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var fabNuevoChat: FloatingActionButton
    private lateinit var btnNuevoChat: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var conversacionAdapter: ConversacionAdapter
    private var usuarioActualId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        tokenManager = TokenManager.getInstance(this)
        usuarioActualId = tokenManager.getUser()?.id ?: 0

        toolbar = findViewById(R.id.toolbar)
        rvConversaciones = findViewById(R.id.rvConversaciones)
        layoutVacio = findViewById(R.id.layoutVacio)
        fabNuevoChat = findViewById(R.id.fabNuevoChat)
        btnNuevoChat = findViewById(R.id.btnNuevoChat)

        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        cargarConversaciones()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mensajes"
    }

    private fun setupRecyclerView() {
        conversacionAdapter = ConversacionAdapter(emptyList()) { conversacion ->
            abrirConversacionExistente(conversacion)
        }
        rvConversaciones.layoutManager = LinearLayoutManager(this)
        rvConversaciones.adapter = conversacionAdapter
    }

    private fun cargarConversaciones() {
        if (usuarioActualId == 0) {
            layoutVacio.visibility = android.view.View.VISIBLE
            rvConversaciones.visibility = android.view.View.GONE
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getConversaciones(usuarioActualId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val conversaciones = response.body() ?: emptyList()
                        if (conversaciones.isEmpty()) {
                            rvConversaciones.visibility = android.view.View.GONE
                            layoutVacio.visibility = android.view.View.VISIBLE
                        } else {
                            rvConversaciones.visibility = android.view.View.VISIBLE
                            layoutVacio.visibility = android.view.View.GONE
                            conversacionAdapter.updateList(conversaciones)
                        }
                    } else {
                        Toast.makeText(
                            this@ChatActivity,
                            "Error al cargar conversaciones",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChatActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        fabNuevoChat.setOnClickListener {
            mostrarDialogNuevoChat()
        }

        btnNuevoChat.setOnClickListener {
            mostrarDialogNuevoChat()
        }
    }

    private fun mostrarDialogNuevoChat() {
        if (usuarioActualId == 0) {
            Toast.makeText(this, "Inicia sesión para chatear", Toast.LENGTH_SHORT).show()
            return
        }

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialogo_nuevo_chat)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val etBuscarUsuario = dialog.findViewById<EditText>(R.id.etBuscarUsuario)
        val rvUsuarios = dialog.findViewById<RecyclerView>(R.id.rvUsuarios)
        val btnCerrar = dialog.findViewById<Button>(R.id.btnCerrar)

        var usuariosCompletos = listOf<Usuario>()

        val adapter = UsuarioAdapter(emptyList()) { usuario ->
            abrirConversacion(usuario)
            dialog.dismiss()
        }

        rvUsuarios.layoutManager = LinearLayoutManager(this)
        rvUsuarios.adapter = adapter

        // ✅ Cargar usuarios reales desde la API
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getUsuariosDisponibles(usuarioActualId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val disponibles = response.body() ?: emptyList()
                        // Mapear UsuarioChat (API) -> Usuario (modelo usado por UsuarioAdapter)
                        usuariosCompletos = disponibles.map {
                            Usuario(
                                id = it.id,
                                name = it.fullName ?: it.username ?: "Usuario",
                                username = it.username ?: "",
                                email = ""
                            )
                        }
                        adapter.updateList(usuariosCompletos)
                    } else {
                        Toast.makeText(
                            this@ChatActivity,
                            "Error al cargar usuarios",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChatActivity,
                        "Error de conexión: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        etBuscarUsuario.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtrados = if (s.toString().isEmpty()) usuariosCompletos
                else usuariosCompletos.filter { it.name.contains(s.toString(), ignoreCase = true) }
                adapter.updateList(filtrados)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        btnCerrar.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    // ✅ Nuevo chat desde el diálogo (aún no hay conversacion_id)
    private fun abrirConversacion(usuario: Usuario) {
        val intent = Intent(this, ChatConversacionActivity::class.java)
        intent.putExtra("usuario_id", usuario.id)
        intent.putExtra("usuario_nombre", usuario.name)
        intent.putExtra("usuario_username", usuario.username)
        intent.putExtra("conversacion_id", 0)
        startActivity(intent)
    }

    // ✅ Abrir conversación ya existente desde la lista
    private fun abrirConversacionExistente(conversacion: ConversacionResumen) {
        val intent = Intent(this, ChatConversacionActivity::class.java)
        intent.putExtra("usuario_id", conversacion.otroUsuario.id)
        intent.putExtra(
            "usuario_nombre",
            conversacion.otroUsuario.fullName ?: conversacion.otroUsuario.username ?: "Usuario"
        )
        intent.putExtra("usuario_username", conversacion.otroUsuario.username ?: "")
        intent.putExtra("conversacion_id", conversacion.id)
        startActivity(intent)
    }
}