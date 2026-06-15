package com.example.impulsolocalmovil

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.impulsolocalmovil.adapters.UsuarioAdapter
import com.example.impulsolocalmovil.models.Usuario
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvConversaciones: RecyclerView
    private lateinit var layoutVacio: LinearLayout
    private lateinit var fabNuevoChat: FloatingActionButton
    private lateinit var btnNuevoChat: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.toolbar)
        rvConversaciones = findViewById(R.id.rvConversaciones)
        layoutVacio = findViewById(R.id.layoutVacio)
        fabNuevoChat = findViewById(R.id.fabNuevoChat)
        btnNuevoChat = findViewById(R.id.btnNuevoChat)

        setupToolbar()
        cargarConversaciones()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = ""
    }

    private fun cargarConversaciones() {
        val conversaciones = emptyList<Any>()

        if (conversaciones.isEmpty()) {
            rvConversaciones.visibility = android.view.View.GONE
            layoutVacio.visibility = android.view.View.VISIBLE
        } else {
            rvConversaciones.visibility = android.view.View.VISIBLE
            layoutVacio.visibility = android.view.View.GONE
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
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialogo_nuevo_chat)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val etBuscarUsuario = dialog.findViewById<EditText>(R.id.etBuscarUsuario)
        val rvUsuarios = dialog.findViewById<RecyclerView>(R.id.rvUsuarios)
        val btnCerrar = dialog.findViewById<Button>(R.id.btnCerrar)

        val usuarios = listOf(
            Usuario(1, "Alexis", "alexis", "alexis@gmail.com"),
            Usuario(2, "Cristian", "cristian", "cristian@test.com"),
            Usuario(3, "Maiko", "maiko", "maiko@test.com"),
            Usuario(4, "Maikol", "maikol", "maikol@test.com")
        )

        val adapter = UsuarioAdapter(usuarios) { usuario ->
            abrirConversacion(usuario)
            dialog.dismiss()
        }

        rvUsuarios.layoutManager = LinearLayoutManager(this)
        rvUsuarios.adapter = adapter

        etBuscarUsuario.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtrados = if (s.toString().isEmpty()) usuarios
                else usuarios.filter { it.nombre.contains(s.toString(), ignoreCase = true) }
                adapter.updateList(filtrados)
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        btnCerrar.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun abrirConversacion(usuario: Usuario) {
        val intent = Intent(this, ChatConversacionActivity::class.java)
        intent.putExtra("usuario_nombre", usuario.nombre)
        intent.putExtra("usuario_username", usuario.username)
        startActivity(intent)
    }
}