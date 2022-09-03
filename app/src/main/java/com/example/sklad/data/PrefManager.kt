package com.example.sklad.data

import android.content.Context

class PrefManager(val context: Context) {
    private val sharedPrefs = context.getSharedPreferences("LoginFile", Context.MODE_PRIVATE)

    fun saveLoginDetails(name: String, password: String) {
        val editor = sharedPrefs.edit()
        editor.apply {
            putString("name", name)
            putString("password", password)
        }.apply()
    }
}