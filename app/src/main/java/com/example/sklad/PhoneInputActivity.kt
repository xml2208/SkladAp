package com.example.sklad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.sklad.databinding.ActivityPhoneInputBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch


class PhoneInputActivity : AppCompatActivity() {
    private lateinit var vb: ActivityPhoneInputBinding
    private val skladRepo = SkladRepository.get()
    private val auth = skladRepo.auth
    private lateinit var storedVerificationId: String
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var number = ""

//    private var authStateListener =
//        AuthStateListener { firebaseAuth ->
//            val firebaseUser = firebaseAuth.currentUser
//            if (firebaseUser != null) {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//                Log.d("asd", "created MAin activity")
//            } else {
//                Log.d("asd", "created verification activity")
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        super.onCreate(savedInstanceState)
        vb = ActivityPhoneInputBinding.inflate(layoutInflater)
        setContentView(vb.root)

        vb.continueBtn.setOnClickListener {
            registerNewUser()
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("ASD", "onVerificationFailed  $e")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("ASD", "onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendingToken = token

                val intent = Intent(applicationContext, CodeInputActivity::class.java)
                intent.putExtra("storedVerificationId", verificationId)
                startActivity(intent)
            }
        }
    }

    private fun registerNewUser() {
        number = vb.phoneEt.text.trim().toString()
        if (number.isNotEmpty()) {
            number = "+998$number"
            sendVerificationCode(number)
            Toast.makeText(this, number, Toast.LENGTH_SHORT).show()
        } else {
            vb.phoneEt.error = "Enter correct mobile number"
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("ASD", "Auth started")
    }
}