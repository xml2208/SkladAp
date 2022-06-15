package com.example.sklad

import android.content.Context

class PrefManager(val context: Context) {

    fun saveLoginDetails(name: String, password: String) {
        val sharedPrefs = context.getSharedPreferences("LoginFile", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.apply {
            putString("name", name)
            putString("password", password)
        }.apply()
    }
}