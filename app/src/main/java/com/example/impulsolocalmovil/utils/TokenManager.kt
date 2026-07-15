package com.example.impulsolocalmovil.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.impulsolocalmovil.models.Usuario
import com.google.gson.Gson

class TokenManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "impulso_local_prefs"
        private const val KEY_TOKEN = "api_token"
        private const val KEY_USER = "user_data"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        @Volatile
        private var instance: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return instance ?: synchronized(this) {
                instance ?: TokenManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUser(user: Usuario) {
        val json = Gson().toJson(user)
        prefs.edit().putString(KEY_USER, json).apply()
    }

    fun getUser(): Usuario? {
        val json = prefs.getString(KEY_USER, null)
        return if (json != null) Gson().fromJson(json, Usuario::class.java) else null
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // ✅ NUEVO MÉTODO
    fun isAdmin(): Boolean {
        return getUser()?.isAdmin ?: false
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}