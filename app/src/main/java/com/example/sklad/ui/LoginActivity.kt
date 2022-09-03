package com.example.sklad.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import androidx.activity.viewModels
import com.example.sklad.data.MainViewModel
import com.example.sklad.data.SkladRepository
import com.example.sklad.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var vb: ActivityLoginBinding
    private val vm: MainViewModel by viewModels()
    private val skladRepo by lazy { SkladRepository.get() }
    private val shp by lazy { getSharedPreferences("LoginFile", Context.MODE_PRIVATE) }

    override fun onStart() {
        super.onStart()
        if (shp.contains("name") && shp.contains("password")) {
            startActivity(Intent(this, MainActivity::class.java))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.continueBtn.setOnClickListener {
            registerUser()
        }
        vb.progressBar.visibility = View.GONE
    }

    private fun registerUser() {
        vb.progressBar.visibility = View.VISIBLE
        val login = vb.loginEt.editText?.text.toString()
        val password = vb.passwordEt.editText?.text.toString()

        vm.registerUser(login, password)

        vm.result.observe(this) { isSuccessful ->
            if (isSuccessful == true) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else {
//                Toast.makeText(this@LoginActivity, "$isSuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

