package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.impulsolocalmovil.adapters.EmprendimientoAdapter
import com.example.impulsolocalmovil.api.RetrofitClient
import com.example.impulsolocalmovil.models.Emprendimiento
import com.example.impulsolocalmovil.utils.TokenManager
import kotlinx.coroutines.*

class MiPerfilActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "http://172.20.10.6:8000"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var ivAvatar: ImageView
    private lateinit var tvNombrePerfil: TextView
    private lateinit var tvEmailPerfil: TextView
    private lateinit var tvTotalEmprendimientos: TextView
    private lateinit var tvTotalMeEncanta: TextView
    private lateinit var tvTotalVentas: TextView
    private lateinit var rvMisEmprendimientos: RecyclerView
    private lateinit var btnEditarPerfil: Button
    private lateinit var btnCambiarPassword: Button
    private lateinit var btnNuevoEmprendimiento: Button

    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: EmprendimientoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_perfil)

        tokenManager = TokenManager.getInstance(this)

        toolbar = findViewById(R.id.toolbar)
        ivAvatar = findViewById(R.id.ivAvatar)
        tvNombrePerfil = findViewById(R.id.tvNombrePerfil)
        tvEmailPerfil = findViewById(R.id.tvEmailPerfil)
        tvTotalEmprendimientos = findViewById(R.id.tvTotalEmprendimientos)
        tvTotalMeEncanta = findViewById(R.id.tvTotalMeEncanta)
        tvTotalVentas = findViewById(R.id.tvTotalVentas)
        rvMisEmprendimientos = findViewById(R.id.rvMisEmprendimientos)
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil)
        btnCambiarPassword = findViewById(R.id.btnCambiarPassword)
        btnNuevoEmprendimiento = findViewById(R.id.btnNuevoEmprendimiento)

        setupToolbar()
        cargarDatosUsuario()
        setupRecyclerView()
        cargarMisEmprendimientos()
        setupClickListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        title = "Mi Perfil"
    }

    private fun cargarDatosUsuario() {
        val user = tokenManager.getUser()

        if (user != null) {
            tvNombrePerfil.text = user.name
            tvEmailPerfil.text = user.email

            // ✅ Cargar avatar con la estructura corregida
            val avatarUrl = if (!user.avatar.isNullOrEmpty()) {
                val rutaLimpia = if (user.avatar.startsWith("/")) user.avatar else "/${user.avatar}"
                BASE_URL + rutaLimpia
            } else {
                null
            }

            Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_placeholder_perfil)
                .error(R.drawable.ic_placeholder_perfil)
                .circleCrop()
                .into(ivAvatar)

        } else {
            tvNombrePerfil.text = "Usuario"
            tvEmailPerfil.text = "usuario@email.com"
            ivAvatar.setImageResource(R.drawable.ic_placeholder_perfil)
        }

        tvTotalEmprendimientos.text = "0"
        tvTotalMeEncanta.text = "0"
        tvTotalVentas.text = "0"
    }

    private fun setupRecyclerView() {
        adapter = EmprendimientoAdapter(emptyList()) { emprendimiento ->
            val intent = Intent(this, DetalleEmprendimientoActivity::class.java)
            intent.putExtra("emprendimiento_id", emprendimiento.id)
            startActivity(intent)
        }
        rvMisEmprendimientos.layoutManager = LinearLayoutManager(this)
        rvMisEmprendimientos.adapter = adapter
    }

    private fun cargarMisEmprendimientos() {
        val usuarioId = tokenManager.getUser()?.id ?: 0

        if (usuarioId == 0) return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getMyVentures(usuarioId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val emprendimientos = response.body() ?: emptyList()
                        tvTotalEmprendimientos.text = emprendimientos.size.toString()
                        adapter.updateList(emprendimientos)
                    }
                }
            } catch (e: Exception) {
                // Error silencioso
            }
        }
    }

    private fun setupClickListeners() {
        btnEditarPerfil.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }

        btnCambiarPassword.setOnClickListener {
            startActivity(Intent(this, CambiarPasswordActivity::class.java))
        }

        btnNuevoEmprendimiento.setOnClickListener {
            startActivity(Intent(this, PublicarEmprendimientoActivity::class.java))
        }
    }
}