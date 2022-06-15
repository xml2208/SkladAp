package com.example.sklad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sklad.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var vb: ActivitySignInBinding
    private val repo = SkladRepository.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.signPhone.setOnClickListener {
            val intent = Intent(this, PhoneInputActivity::class.java)
            startActivity(intent)
        }
        vb.signEmail.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}