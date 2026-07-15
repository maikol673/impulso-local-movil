package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.impulsolocalmovil.fragments.HomeFragment
import com.example.impulsolocalmovil.utils.TokenManager
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tokenManager = TokenManager.getInstance(this)

        // ✅ Verificar autenticación
        if (!tokenManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        // ✅ Obtener usuario desde TokenManager
        val user = tokenManager.getUser()
        val isLoggedIn = tokenManager.isLoggedIn()
        val isAdmin = tokenManager.isAdmin()  // ← USAR TokenManager
        val userName = user?.name ?: "Invitado"
        val userEmail = user?.email ?: "Inicia sesión"

        // Actualizar header del drawer
        val headerView = navigationView.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = headerView.findViewById<TextView>(R.id.tvUserEmail)

        tvUserName.text = if (isLoggedIn) userName else "Invitado"
        tvUserEmail.text = if (isLoggedIn) userEmail else "Inicia sesión"

        // Configurar menú
        val menu = navigationView.menu

        menu.findItem(R.id.nav_login)?.isVisible = !isLoggedIn
        menu.findItem(R.id.nav_logout)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_admin)?.isVisible = (isLoggedIn && isAdmin)
        menu.findItem(R.id.nav_mi_perfil)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_mis_emprendimientos)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_mis_favoritos)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_mis_ordenes)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_chat)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_mi_carrito)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_ajustes)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_mis_cursos)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_mis_eventos)?.isVisible = isLoggedIn

        // ✅ Manejar clics en el menú
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                R.id.nav_logout -> {
                    tokenManager.logout()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                R.id.nav_mi_perfil -> {
                    startActivity(Intent(this, MiPerfilActivity::class.java))
                }
                R.id.nav_mis_emprendimientos -> {
                    startActivity(Intent(this, MisEmprendimientosActivity::class.java))
                }
                R.id.nav_mis_favoritos -> {
                    startActivity(Intent(this, MisFavoritosActivity::class.java))
                }
                R.id.nav_mis_cursos -> {
                    startActivity(Intent(this, MisCursosActivity::class.java))
                }
                R.id.nav_mis_eventos -> {
                    startActivity(Intent(this, MisEventosActivity::class.java))
                }
                R.id.nav_ajustes -> {
                    startActivity(Intent(this, AjustesActivity::class.java))
                }
                R.id.nav_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                }
                R.id.nav_mi_carrito -> {
                    startActivity(Intent(this, MiCarritoActivity::class.java))
                }
                R.id.nav_mis_ordenes -> {
                    startActivity(Intent(this, MisOrdenesActivity::class.java))
                }
                R.id.nav_admin -> {
                    startActivity(Intent(this, AdminPanelActivity::class.java))
                }
                else -> {
                    cargarFragment(HomeFragment())
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        navigationView.itemIconTintList = null

        // ✅ Manejar botón atrás
        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }

        // ✅ Cargar fragmento inicial
        if (savedInstanceState == null) {
            cargarFragment(HomeFragment())
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}