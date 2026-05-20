package com.example.impulsolocalmovil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sharedPref = getSharedPreferences("impulso_local_prefs", MODE_PRIVATE)
        val userEmail = sharedPref.getString("user_email", null)

        if (userEmail == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            toolbar.title = "Hola, $userEmail"
        }
    }
}