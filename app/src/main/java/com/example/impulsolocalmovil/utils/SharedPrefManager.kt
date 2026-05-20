package com.example.impulsolocalmovil.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.impulsolocalmovil.models.User
import com.google.gson.Gson

class SharedPrefManager private constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "impulso_local_prefs"
        private const val KEY_USER = "user"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        @Volatile
        private var instance: SharedPrefManager? = null

        fun getInstance(context: Context): SharedPrefManager {
            return instance ?: synchronized(this) {
                instance ?: SharedPrefManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun saveUser(user: User) {
        val json = Gson().toJson(user)
        prefs.edit().putString(KEY_USER, json).apply()
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
    }

    fun getUser(): User? {
        val json = prefs.getString(KEY_USER, null)
        return if (json != null) Gson().fromJson(json, User::class.java) else null
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}