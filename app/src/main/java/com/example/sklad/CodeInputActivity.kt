package com.example.sklad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sklad.databinding.ActivityCodeInputBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class CodeInputActivity : AppCompatActivity() {
    private lateinit var vb: ActivityCodeInputBinding
    private val skladRepo = SkladRepository.get()
    private val auth= skladRepo.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityCodeInputBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val storedVerificationId = intent.getStringExtra("storedVerificationId")

        vb.registerBtn.setOnClickListener {
            val code = vb.codeEt.text.trim().toString()
            if (code.isNotEmpty()) {
                val credential: PhoneAuthCredential =
                    PhoneAuthProvider.getCredential(storedVerificationId!!.toString(), code)
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(this, "Successfully registered", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Invalid Code", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }
}