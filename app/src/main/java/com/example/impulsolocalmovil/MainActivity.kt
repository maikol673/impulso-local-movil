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
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val sharedPref = getSharedPreferences("impulso_local_prefs", MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", null)
        val userName = sharedPref.getString("user_name", "Invitado")
        val isAdmin = sharedPref.getBoolean("is_admin", false)

        val headerView = navigationView.getHeaderView(0)
        val tvUserName = headerView.findViewById<TextView>(R.id.tvUserName)
        val tvUserEmail = headerView.findViewById<TextView>(R.id.tvUserEmail)

        tvUserName.text = if (userEmail != null) userName else "Invitado"
        tvUserEmail.text = if (userEmail != null) userEmail else "Inicia sesión"

        val menu = navigationView.menu

        // Mostrar/Ocultar según estado de login
        val isLoggedIn = (userEmail != null)

        menu.findItem(R.id.nav_login)?.isVisible = !isLoggedIn
        menu.findItem(R.id.nav_logout)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_admin)?.isVisible = (isLoggedIn && isAdmin)
        menu.findItem(R.id.nav_mi_perfil)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_dashboard)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_mis_ordenes)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_chat)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_mi_carrito)?.isVisible = isLoggedIn
        menu.findItem(R.id.nav_ajustes)?.isVisible = isLoggedIn

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_login -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                R.id.nav_logout -> {
                    sharedPref.edit().clear().apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                R.id.nav_admin -> {
                    // TODO: Ir a panel de administrador
                }
                R.id.nav_mi_perfil -> {
                    cargarFragment(HomeFragment())
                }
                R.id.nav_dashboard -> {
                    cargarFragment(HomeFragment())
                }
                else -> {
                    cargarFragment(HomeFragment())
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        navigationView.itemIconTintList = null


        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }

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