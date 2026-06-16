package com.example.impulsolocalmovil

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.impulsolocalmovil.fragments.AdminDashboardFragment
import com.google.android.material.navigation.NavigationView

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnMenuDrawer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        drawerLayout = findViewById(R.id.drawerLayoutAdmin)
        navigationView = findViewById(R.id.navigationViewAdmin)
        btnMenuDrawer = findViewById(R.id.btnMenuDrawer)

        setupDrawer()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerAdmin, AdminDashboardFragment())
                .commit()
        }
    }

    private fun setupDrawer() {
        btnMenuDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navAdminDashboard -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerAdmin, AdminDashboardFragment())
                        .commit()
                }
                R.id.navAdminUsuarios -> {
                    // TODO: Gestionar usuarios
                }
                R.id.navAdminModeracion -> {
                    // TODO: Moderación
                }
                R.id.navAdminAnalytics -> {
                    // TODO: Analytics
                }
                R.id.navAdminSoporte -> {
                    // TODO: Soporte
                }
                R.id.navAdminDjango -> {
                    // TODO: Abrir admin Django
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}